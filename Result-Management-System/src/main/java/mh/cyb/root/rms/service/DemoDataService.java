package mh.cyb.root.rms.service;

import mh.cyb.root.rms.entity.*;
import mh.cyb.root.rms.entity.Class;
import mh.cyb.root.rms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DemoDataService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherAssignmentRepository teacherAssignmentRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private static final String[][] STUDENTS_LIST = {
        // Name, Roll, Class Name
        {"John Doe", "101", "Class 10"},
        {"Jane Smith", "102", "Class 10"},
        {"Mike Johnson", "103", "Class 10"},
        {"Emily Davis", "104", "Class 10"},
        {"Alex Garcia", "105", "Class 10"},
        {"Sophia Martinez", "106", "Class 10"},
        {"Liam Robinson", "107", "Class 10"},
        {"Chloe Taylor", "108", "Class 10"},
        
        {"Sarah Wilson", "201", "Class 9"},
        {"David Brown", "202", "Class 9"},
        {"Daniel Lee", "203", "Class 9"},
        {"Olivia Harris", "204", "Class 9"},
        
        {"James Clark", "301", "Class 11"},
        {"Emma Lewis", "302", "Class 11"},
        {"Ryan Walker", "303", "Class 11"},
        {"Grace Hall", "304", "Class 11"},
        
        {"Lucas Allen", "401", "Class 12"},
        {"Mia Young", "402", "Class 12"},
        {"Ethan King", "403", "Class 12"},
        {"Isabella Wright", "404", "Class 12"}
    };

    private static final String[][] TEACHERS_LIST = {
        // Name, Email, Phone
        {"Dr. Sarah Johnson", "sarah.johnson@school.edu", "+1-555-0101"},
        {"Prof. Michael Chen", "michael.chen@school.edu", "+1-555-0102"},
        {"Ms. Emily Davis", "emily.davis@school.edu", "+1-555-0103"},
        {"Mr. Robert Wilson", "robert.wilson@school.edu", "+1-555-0104"}
    };

    @Transactional
    public Map<String, Object> regenerate() {
        // 1. Clear existing dynamic data
        marksRepository.deleteAll();
        attendanceRepository.deleteAll();
        teacherAssignmentRepository.deleteAll();
        studentRepository.deleteAll();
        examRepository.deleteAll();
        subjectRepository.deleteAll();
        classRepository.deleteAll();
        sessionRepository.deleteAll();

        Random rng = new Random();

        // 2. Re-create sessions
        Session session2024 = new Session("2024-25", LocalDate.of(2024, 4, 1), LocalDate.of(2025, 3, 31));
        session2024.setActive(true);
        Session session2025 = new Session("2025-26", LocalDate.of(2025, 4, 1), LocalDate.of(2026, 3, 31));
        sessionRepository.save(session2024);
        sessionRepository.save(session2025);

        // 3. Re-create classes
        Map<String, Class> classes = new HashMap<>();
        String[] classNames = {"Class 9", "Class 10", "Class 11", "Class 12"};
        for (String cName : classNames) {
            Class cls = new Class(cName, cName + " - Secondary Academic Level");
            classRepository.save(cls);
            classes.put(cName, cls);
        }

        // 4. Ensure teachers exist
        List<Teacher> teachers = new ArrayList<>();
        for (String[] tData : TEACHERS_LIST) {
            Optional<Teacher> optT = teacherRepository.findAll().stream()
                    .filter(t -> t.getEmail().equalsIgnoreCase(tData[1]))
                    .findFirst();
            if (optT.isPresent()) {
                teachers.add(optT.get());
            } else {
                Teacher t = new Teacher(tData[0], tData[1], tData[2]);
                teacherRepository.save(t);
                teachers.add(t);
            }
        }

        // 5. Re-create subjects
        List<Subject> subjects = new ArrayList<>();
        
        // Class 10 Subjects
        Subject math10 = new Subject("Mathematics", classes.get("Class 10"), 100);
        math10.setOptional(false);
        Subject english10 = new Subject("English", classes.get("Class 10"), 100);
        english10.setOptional(false);
        Subject science10 = new Subject("Science", classes.get("Class 10"), 100);
        science10.setOptional(false);
        Subject compSci10 = new Subject("Computer Science", classes.get("Class 10"), 100);
        compSci10.setOptional(true);
        
        // Class 9 Subjects
        Subject math9 = new Subject("Mathematics", classes.get("Class 9"), 100);
        Subject english9 = new Subject("English", classes.get("Class 9"), 100);
        
        // Class 11 Subjects
        Subject physics11 = new Subject("Physics", classes.get("Class 11"), 100);
        Subject chemistry11 = new Subject("Chemistry", classes.get("Class 11"), 100);
        
        // Class 12 Subjects
        Subject literature12 = new Subject("English Literature", classes.get("Class 12"), 100);
        Subject calculus12 = new Subject("Advanced Calculus", classes.get("Class 12"), 100);

        List<Subject> allSubjects = List.of(
            math10, english10, science10, compSci10,
            math9, english9,
            physics11, chemistry11,
            literature12, calculus12
        );
        for (Subject sub : allSubjects) {
            subjectRepository.save(sub);
            subjects.add(sub);
        }

        // 6. Assign Teachers to Subjects
        for (int i = 0; i < subjects.size(); i++) {
            Teacher t = teachers.get(i % teachers.size());
            TeacherAssignment ta = new TeacherAssignment(t, subjects.get(i), session2024);
            teacherAssignmentRepository.save(ta);
        }

        // 7. Re-create exams
        Exam midterm = new Exam("Midterm Exam", LocalDate.of(2024, 10, 15), session2024);
        Exam quiz1 = new Exam("Quiz 1", LocalDate.of(2024, 12, 1), session2024);
        Exam finalExam = new Exam("Final Exam", LocalDate.of(2025, 3, 15), session2024);
        
        examRepository.save(midterm);
        examRepository.save(quiz1);
        examRepository.save(finalExam);
        List<Exam> examsList = List.of(midterm, quiz1, finalExam);

        // 8. Re-create 20 students
        List<Student> students = new ArrayList<>();
        for (String[] sData : STUDENTS_LIST) {
            Student student = new Student(sData[0], sData[1], sData[2], session2024);
            studentRepository.save(student);
            students.add(student);
        }

        // 9. Generate Marks
        int marksSeededCount = 0;
        for (Student student : students) {
            // Find subjects matching the student's class
            List<Subject> studentSubjects = new ArrayList<>();
            for (Subject sub : subjects) {
                if (sub.getClassName().equalsIgnoreCase(student.getClassName())) {
                    studentSubjects.add(sub);
                }
            }

            // Assign marks based on student performance profile
            String roll = student.getRollNumber();
            double minFactor, maxFactor;
            
            if (roll.equals("101")) { // Top topper
                minFactor = 0.90; maxFactor = 1.00;
            } else if (roll.equals("103")) { // Failing compulsory candidate
                minFactor = 0.25; maxFactor = 0.45;
            } else if (roll.equals("102") || roll.equals("202") || roll.equals("302")) { // Average
                minFactor = 0.55; maxFactor = 0.78;
            } else { // Mixed/Good
                minFactor = 0.65; maxFactor = 0.95;
            }

            for (Exam exam : examsList) {
                for (Subject sub : studentSubjects) {
                    // Random obtained marks based on profile
                    double factor = minFactor + (maxFactor - minFactor) * rng.nextDouble();
                    int obtained = (int) Math.round(sub.getMaxMarks() * factor);
                    obtained = Math.max(0, Math.min(sub.getMaxMarks(), obtained));
                    
                    // Simulate teacher entry
                    Teacher evaluator = teachers.get(rng.nextInt(teachers.size()));
                    
                    Marks marksEntry = new Marks(student, sub, exam, obtained, exam.getExamDate());
                    marksEntry.setEnteredBy(evaluator);
                    marksEntry.setEnteredDate(LocalDateTime.now().minusDays(rng.nextInt(10)));
                    marksRepository.save(marksEntry);
                    marksSeededCount++;
                }
            }
        }

        // 10. Generate 30 days of attendance history (Bangladesh skip Fri/Sat)
        int attendanceSeededCount = 0;
        LocalDate today = LocalDate.now();
        
        for (Student student : students) {
            for (int offset = 29; offset >= 0; offset--) {
                LocalDate date = today.minusDays(offset);
                
                // Skip weekends (Friday & Saturday)
                if (date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                    continue;
                }
                
                // 90% Present, 7% Late, 3% Absent
                double roll = rng.nextDouble();
                AttendanceStatus status;
                if (roll < 0.03) {
                    status = AttendanceStatus.ABSENT;
                } else if (roll < 0.10) {
                    status = AttendanceStatus.LATE;
                } else {
                    status = AttendanceStatus.PRESENT;
                }
                
                Attendance att = new Attendance(student, session2024, date, status);
                if (status == AttendanceStatus.LATE) {
                    att.setRemarks("Arrived " + (5 + rng.nextInt(35)) + " minutes late.");
                } else if (status == AttendanceStatus.ABSENT) {
                    att.setRemarks("Unexcused absence.");
                } else {
                    att.setRemarks("On time.");
                }
                
                attendanceRepository.save(att);
                attendanceSeededCount++;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("seededStudents", students.size());
        result.put("seededClasses", classNames.length);
        result.put("seededSubjects", subjects.size());
        result.put("seededExams", examsList.size());
        result.put("seededMarksEntries", marksSeededCount);
        result.put("seededAttendanceEntries", attendanceSeededCount);
        result.put("message", "✅ Successfully generated " + students.size() + " students with 30-day attendance and complete exam marks!");
        return result;
    }

    @Transactional
    public void clearDemoData() {
        marksRepository.deleteAll();
        attendanceRepository.deleteAll();
        teacherAssignmentRepository.deleteAll();
        studentRepository.deleteAll();
        examRepository.deleteAll();
        subjectRepository.deleteAll();
        classRepository.deleteAll();
        sessionRepository.deleteAll();
        System.out.println("🗑️ Cleared all RMS demo data.");
    }
}
