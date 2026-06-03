package root.cyb.mh.attendancesystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import root.cyb.mh.attendancesystem.model.*;
import root.cyb.mh.attendancesystem.repository.*;

import java.time.*;
import java.util.*;

@Service
public class DemoDataService {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private AttendanceLogRepository attendanceRepository;
    @Autowired private PayslipRepository payslipRepository;
    @Autowired private PayrollService payrollService;
    @Autowired private ShiftRepository shiftRepository;
    @Autowired private EmployeeShiftRepository employeeShiftRepository;
    @Autowired private LeaveRequestRepository leaveRequestRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private static final String DEMO_PREFIX = "ACAD-";

    // ── 20 Academic Member names ──────────────────────────────────────────
    private static final String[][] ACADEMICS = {
        {"Dr. Sarah Mitchell",     "Professor",           "sarah.mitchell"},
        {"Prof. James Hawking",    "Department Head",     "james.hawking"},
        {"Dr. Priya Sharma",       "Associate Professor", "priya.sharma"},
        {"Mr. Omar Al-Rashid",     "Lecturer",            "omar.alrashid"},
        {"Dr. Elena Vasquez",      "Professor",           "elena.vasquez"},
        {"Ms. Fatima Noor",        "Teaching Assistant",  "fatima.noor"},
        {"Dr. Chen Wei",           "Associate Professor", "chen.wei"},
        {"Mr. Arjun Patel",        "Lecturer",            "arjun.patel"},
        {"Dr. Amelia Foster",      "Professor",           "amelia.foster"},
        {"Mr. Lucas Mendes",       "Teaching Assistant",  "lucas.mendes"},
        {"Dr. Aisha Kamara",       "Associate Professor", "aisha.kamara"},
        {"Ms. Yuki Tanaka",        "Lecturer",            "yuki.tanaka"},
        {"Dr. Raj Krishnamurthy",  "Department Head",     "raj.krishnamurthy"},
        {"Mr. Felix Weber",        "Teaching Assistant",  "felix.weber"},
        {"Dr. Nadia Okafor",       "Professor",           "nadia.okafor"},
        {"Ms. Zara Ahmed",         "Lecturer",            "zara.ahmed"},
        {"Dr. Marco Rossi",        "Associate Professor", "marco.rossi"},
        {"Mr. Sam Johnson",        "Teaching Assistant",  "sam.johnson"},
        {"Dr. Lin Xiaoming",       "Professor",           "lin.xiaoming"},
        {"Ms. Grace Owusu",        "Lecturer",            "grace.owusu"},
    };

    private static final String[] DEPT_NAMES = {
        "Computer Science", "Mathematics", "Physics", "Business Administration"
    };

    // ── Main entry points ─────────────────────────────────────────────────

    /** Called on-demand (sidebar button) — clears old ACAD- demo data, then re-seeds fresh. */
    public Map<String, Object> regenerate() {
        clearDemoData();
        return seedAcademicData(new Random());
    }

    /** Idempotent startup seed — only seeds if fewer than 5 demo employees exist. */
    public void seedOnStartup() {
        long existing = employeeRepository.findAll().stream()
                .filter(e -> e.getId() != null && e.getId().startsWith(DEMO_PREFIX))
                .count();
        if (existing >= 5) {
            System.out.println("[SEEDER] Demo data already exists (" + existing + " academic members). Skipping.");
            return;
        }
        seedAcademicData(new Random(42)); // fixed seed for reproducibility on startup
        System.out.println("[SEEDER] ✅ Seeded 20 academic members on startup.");
    }

    // ── Core seeding logic ────────────────────────────────────────────────

    private Map<String, Object> seedAcademicData(Random rng) {
        // 1. Departments
        List<Department> depts = new ArrayList<>();
        for (String name : DEPT_NAMES) {
            depts.add(getOrCreateDept(name));
        }

        // 2. Academic Day Shift
        Shift academicShift = getOrCreateShift("Academic Day",
                LocalTime.of(8, 0), LocalTime.of(17, 0), 15, 10);

        int seeded = 0;
        for (int i = 0; i < ACADEMICS.length; i++) {
            String[] person = ACADEMICS[i];
            String empId = String.format("ACAD-%04d", i + 1);
            String name     = person[0];
            String role     = person[1];
            String username = person[2];

            // Skip if already exists
            if (employeeRepository.existsById(empId)) continue;

            Department dept = depts.get(i % depts.size());
            double salary = salaryByRole(role, rng);

            // 3. Create Employee
            Employee emp = new Employee();
            emp.setId(empId);
            emp.setName(name);
            emp.setRole(role);
            emp.setDesignation(role);
            emp.setEmail(username.replace(".", "") + "@smartacademic.edu");
            emp.setUsername(username);
            emp.setPassword(passwordEncoder.encode("student123"));
            emp.setDepartment(dept);
            emp.setMonthlySalary(salary);
            emp.setAnnualLeaveQuota(21);
            emp.setJoiningDate(LocalDate.now().minusMonths(rng.nextInt(24) + 6));
            emp = employeeRepository.save(emp);

            // 4. Assign shift (from joining date to +2 years)
            EmployeeShift es = new EmployeeShift();
            es.setEmployee(emp);
            es.setShift(academicShift);
            es.setStartDate(emp.getJoiningDate());
            es.setEndDate(LocalDate.now().plusYears(2));
            employeeShiftRepository.save(es);

            // 5. Create login account
            if (!userRepository.findByUsername(username).isPresent()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode("student123"));
                user.setRole("EMPLOYEE");
                userRepository.save(user);
            }

            // 6. Attendance history (last 45 calendar days, skip Fri/Sat)
            generateAttendance(emp, rng, academicShift);

            // 7. Leave requests
            generateLeave(emp, rng);

            // 8. Payslips (last 3 months)
            generatePayslips(emp);

            seeded++;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("seeded", seeded);
        result.put("departments", DEPT_NAMES.length);
        result.put("shift", academicShift.getName());
        result.put("message", "✅ Generated " + seeded + " academic members with 45-day attendance, payslips & leave history.");
        return result;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private void generateAttendance(Employee emp, Random rng, Shift shift) {
        LocalDate today = LocalDate.now();

        for (int dayOffset = 44; dayOffset >= 0; dayOffset--) {
            LocalDate date = today.minusDays(dayOffset);

            // Skip weekends (Fri/Sat in Bangladesh)
            if (date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY)
                continue;

            int roll = rng.nextInt(100);
            boolean absent = roll < 12; // 12% absent
            boolean late   = !absent && roll < 22; // 10% of remaining = late

            if (absent) continue;

            LocalTime inTime  = shift.getStartTime();
            LocalTime outTime = shift.getEndTime();

            if (late) {
                inTime = inTime.plusMinutes(20 + rng.nextInt(55)); // 20–75 min late
            } else {
                // Early bird (-15 to +5 min)
                inTime = inTime.minusMinutes(rng.nextInt(15));
            }
            // Random checkout variance: -10 to +30 min
            outTime = outTime.plusMinutes(rng.nextInt(40) - 10);

            // Today: only check-in (still "working" now)
            boolean isToday = date.equals(today);

            AttendanceLog checkIn = new AttendanceLog();
            checkIn.setEmployeeId(emp.getId());
            checkIn.setTimestamp(LocalDateTime.of(date, inTime));
            checkIn.setDeviceId(1L);
            attendanceRepository.save(checkIn);

            if (!isToday) {
                AttendanceLog checkOut = new AttendanceLog();
                checkOut.setEmployeeId(emp.getId());
                checkOut.setTimestamp(LocalDateTime.of(date, outTime));
                checkOut.setDeviceId(1L);
                attendanceRepository.save(checkOut);
            }
        }
    }

    private void generateLeave(Employee emp, Random rng) {
        LocalDate today = LocalDate.now();
        String[] types = {"Sick Leave", "Casual Leave", "Annual Leave", "Emergency Leave"};

        // Past approved leave (1–2 weeks ago)
        int pastOffset = 8 + rng.nextInt(8);
        createLeave(emp,
                today.minusDays(pastOffset + 1),
                today.minusDays(pastOffset),
                types[rng.nextInt(types.length)],
                LeaveRequest.Status.APPROVED);

        // Upcoming pending leave (next 1–2 weeks)
        int futureOffset = 5 + rng.nextInt(10);
        createLeave(emp,
                today.plusDays(futureOffset),
                today.plusDays(futureOffset + 1),
                types[rng.nextInt(types.length)],
                LeaveRequest.Status.PENDING);
    }

    private void generatePayslips(Employee emp) {
        YearMonth current = YearMonth.now();
        for (int i = 1; i <= 3; i++) {
            YearMonth month = current.minusMonths(i);
            try {
                payrollService.createPayslipForEmployee(emp, month);
                payslipRepository.findByEmployeeIdAndMonth(emp.getId(), month.toString())
                        .ifPresent(slip -> {
                            slip.setStatus(Payslip.Status.PAID);
                            payslipRepository.save(slip);
                        });
            } catch (Exception e) {
                System.err.println("[SEEDER] Payslip error for " + emp.getName() + ": " + e.getMessage());
            }
        }
    }

    private double salaryByRole(String role, Random rng) {
        return switch (role) {
            case "Professor"           -> 100000 + rng.nextInt(50000);
            case "Department Head"     -> 120000 + rng.nextInt(30000);
            case "Associate Professor" ->  80000 + rng.nextInt(30000);
            case "Lecturer"            ->  60000 + rng.nextInt(20000);
            default                    ->  40000 + rng.nextInt(15000); // Teaching Assistant
        };
    }

    private Department getOrCreateDept(String name) {
        return departmentRepository.findByName(name).orElseGet(() -> {
            Department d = new Department();
            d.setName(name);
            return departmentRepository.save(d);
        });
    }

    private Shift getOrCreateShift(String name, LocalTime start, LocalTime end, int lateTol, int earlyTol) {
        return shiftRepository.findByName(name).orElseGet(() -> {
            Shift s = new Shift();
            s.setName(name);
            s.setStartTime(start);
            s.setEndTime(end);
            s.setLateToleranceMinutes(lateTol);
            s.setEarlyLeaveToleranceMinutes(earlyTol);
            return shiftRepository.save(s);
        });
    }

    private void createLeave(Employee emp, LocalDate start, LocalDate end,
                              String type, LeaveRequest.Status status) {
        LeaveRequest lr = new LeaveRequest();
        lr.setEmployee(emp);
        lr.setStartDate(start);
        lr.setEndDate(end);
        lr.setLeaveType(type);
        lr.setStatus(status);
        if (status == LeaveRequest.Status.REJECTED)
            lr.setAdminComment("Not approved at this time.");
        leaveRequestRepository.save(lr);
    }

    // ── Clear all ACAD- demo data ─────────────────────────────────────────

    public void clearDemoData() {
        List<Employee> demoEmps = employeeRepository.findAll().stream()
                .filter(e -> e.getId() != null && e.getId().startsWith(DEMO_PREFIX))
                .toList();

        for (Employee emp : demoEmps) {
            attendanceRepository.deleteAll(attendanceRepository.findByEmployeeId(emp.getId()));
            payslipRepository.deleteAll(payslipRepository.findByEmployeeIdOrderByMonthDesc(emp.getId()));
            employeeShiftRepository.deleteAll(employeeShiftRepository.findByEmployeeId(emp.getId()));

            leaveRequestRepository.deleteAll(leaveRequestRepository.findAll().stream()
                    .filter(l -> l.getEmployee() != null && l.getEmployee().getId().equals(emp.getId()))
                    .toList());

            // Remove login account
            if (emp.getUsername() != null) {
                userRepository.findByUsername(emp.getUsername()).ifPresent(userRepository::delete);
            }

            employeeRepository.delete(emp);
        }
        System.out.println("[SEEDER] 🗑️  Cleared all ACAD- demo data.");
    }
}
