package mh.cyb.root.watch_employee.service;

import mh.cyb.root.watch_employee.entity.*;
import mh.cyb.root.watch_employee.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DemoDataService {

    @PersistenceContext
    private EntityManager entityManager;

    private final EmployeeRepository employeeRepository;
    private final ActivityLogRepository activityLogRepository;
    private final DomainCategoryRepository domainCategoryRepository;
    private final BlockedSiteRepository blockedSiteRepository;
    private final CredentialRepository credentialRepository;
    private final CredentialGroupRepository credentialGroupRepository;
    private final CredentialGroupMappingRepository credentialGroupMappingRepository;
    private final DeviceGroupAccessRepository deviceGroupAccessRepository;
    private final EncryptionService encryptionService;

    public DemoDataService(
            EmployeeRepository employeeRepository,
            ActivityLogRepository activityLogRepository,
            DomainCategoryRepository domainCategoryRepository,
            BlockedSiteRepository blockedSiteRepository,
            CredentialRepository credentialRepository,
            CredentialGroupRepository credentialGroupRepository,
            CredentialGroupMappingRepository credentialGroupMappingRepository,
            DeviceGroupAccessRepository deviceGroupAccessRepository,
            EncryptionService encryptionService) {
        this.employeeRepository = employeeRepository;
        this.activityLogRepository = activityLogRepository;
        this.domainCategoryRepository = domainCategoryRepository;
        this.blockedSiteRepository = blockedSiteRepository;
        this.credentialRepository = credentialRepository;
        this.credentialGroupRepository = credentialGroupRepository;
        this.credentialGroupMappingRepository = credentialGroupMappingRepository;
        this.deviceGroupAccessRepository = deviceGroupAccessRepository;
        this.encryptionService = encryptionService;
    }

    private static final String[][] MOCK_EMPLOYEES = {
            // deviceId, name, department
            {"device-101", "Sarah Mitchell", "Science"},
            {"device-102", "John Doe", "Science"},
            {"device-103", "Emma Lewis", "Arts"},
            {"device-104", "Ryan Walker", "Arts"},
            {"device-105", "Mia Young", "Business"},
            {"device-106", "David Brown", "Business"},
            {"device-107", "Grace Hall", "General"},
            {"device-108", "Daniel Lee", "General"}
    };

    private static final String[][] DOMAIN_CATEGORIES = {
            {"google.com", "Productive"},
            {"wikipedia.org", "Productive"},
            {"khanacademy.org", "Productive"},
            {"github.com", "Productive"},
            {"stackoverflow.com", "Productive"},
            {"coursera.org", "Productive"},
            {"zoom.us", "Neutral"},
            {"youtube.com", "Neutral"},
            {"facebook.com", "Unproductive"},
            {"instagram.com", "Unproductive"},
            {"reddit.com", "Unproductive"},
            {"netflix.com", "Unproductive"}
    };

    private static final String[][] WEB_LOGS = {
            // domain, url, category
            {"google.com", "https://www.google.com/search?q=organic+chemistry+basics", "Productive"},
            {"wikipedia.org", "https://en.wikipedia.org/wiki/Organic_chemistry", "Productive"},
            {"khanacademy.org", "https://www.khanacademy.org/science/chemistry/organic-compounds", "Productive"},
            {"github.com", "https://github.com/topics/education", "Productive"},
            {"stackoverflow.com", "https://stackoverflow.com/questions/tagged/java", "Productive"},
            {"coursera.org", "https://www.coursera.org/learn/algorithms-part1", "Productive"},
            {"zoom.us", "https://zoom.us/j/9876543210?pwd=lecturesession", "Neutral"},
            {"youtube.com", "https://www.youtube.com/watch?v=lectures-on-physics", "Neutral"},
            {"facebook.com", "https://www.facebook.com/messages", "Unproductive"},
            {"instagram.com", "https://www.instagram.com/p/feed", "Unproductive"},
            {"reddit.com", "https://www.reddit.com/r/gaming", "Unproductive"},
            {"netflix.com", "https://www.netflix.com/watch/series-id", "Unproductive"}
    };

    @Transactional
    public Map<String, Object> regenerate() {
        // 1. Clear existing dynamic data
        activityLogRepository.deleteAll();
        deviceGroupAccessRepository.deleteAll();
        credentialGroupMappingRepository.deleteAll();
        credentialRepository.deleteAll();
        credentialGroupRepository.deleteAll();
        blockedSiteRepository.deleteAll();
        domainCategoryRepository.deleteAll();
        employeeRepository.deleteAll();
        entityManager.flush();

        Random rng = new Random();
        LocalDateTime now = LocalDateTime.now();

        // 2. Seed Employees (Students)
        List<Employee> employees = new ArrayList<>();
        for (String[] empData : MOCK_EMPLOYEES) {
            Employee emp = new Employee(empData[0], empData[1], empData[2]);
            // Random last seen within 3 hours
            emp.setLastSeen(now.minusMinutes(rng.nextInt(180)));
            employeeRepository.save(emp);
            employees.add(emp);
        }

        // 3. Seed Domain Categories
        for (String[] catData : DOMAIN_CATEGORIES) {
            DomainCategory dc = new DomainCategory(catData[0], catData[1]);
            domainCategoryRepository.save(dc);
        }

        // 4. Seed Blocked Sites
        List<BlockedSite> blockedSites = List.of(
                new BlockedSite("tiktok.com", null), // Global block
                new BlockedSite("roblox.com", "device-103"), // Emma Lewis block
                new BlockedSite("discord.com", "device-105"), // Mia Young block
                new BlockedSite("steamcommunity.com", null) // Global block
        );
        blockedSiteRepository.saveAll(blockedSites);

        // 5. Seed Credential Groups
        CredentialGroup groupPortals = new CredentialGroup("School Portals", "Main educational portals and student intranet");
        CredentialGroup groupResources = new CredentialGroup("Learning Resources", "External developer, libraries, and class environments");
        credentialGroupRepository.save(groupPortals);
        credentialGroupRepository.save(groupResources);

        // 6. Seed Credentials (Encrypted)
        List<Credential> credentials = new ArrayList<>();
        String encMain = encryptionService.encrypt("student123");
        String encLms = encryptionService.encrypt("lms-portal-pass");
        String encCoursera = encryptionService.encrypt("coursera-access-2026");
        String encGithub = encryptionService.encrypt("git-edu-devpass");

        Credential credMain = new Credential("Smart Academic Main Portal", "http://localhost:8083", "sarah.mitchell", encMain, "Academic student account login");
        credMain.setSubmittedByDeviceId("device-101");
        Credential credLms = new Credential("LMS Classroom Portal", "http://lms.school.edu", "sarah.mitchell", encLms, "LMS course submissions");
        credLms.setSubmittedByDeviceId("device-101");
        Credential credCoursera = new Credential("Coursera Class Access", "https://coursera.org", "sarah.mitchell", encCoursera, "Extra algorithms certifications");
        credCoursera.setSubmittedByDeviceId("device-101");
        Credential credGithub = new Credential("GitHub Student Dev Pack", "https://github.com", "sarah-mitchell-edu", encGithub, "Developer environment authentication");
        credGithub.setSubmittedByDeviceId("device-101");

        credentialRepository.save(credMain);
        credentialRepository.save(credLms);
        credentialRepository.save(credCoursera);
        credentialRepository.save(credGithub);

        credentials.add(credMain);
        credentials.add(credLms);
        credentials.add(credCoursera);
        credentials.add(credGithub);

        // 7. Seed Credential Group Mappings
        credentialGroupMappingRepository.save(new CredentialGroupMapping(credMain, groupPortals));
        credentialGroupMappingRepository.save(new CredentialGroupMapping(credLms, groupPortals));
        credentialGroupMappingRepository.save(new CredentialGroupMapping(credCoursera, groupResources));
        credentialGroupMappingRepository.save(new CredentialGroupMapping(credGithub, groupResources));

        // 8. Seed Device Group Access
        deviceGroupAccessRepository.save(new DeviceGroupAccess(groupPortals, "device-101"));
        deviceGroupAccessRepository.save(new DeviceGroupAccess(groupPortals, "device-102"));
        deviceGroupAccessRepository.save(new DeviceGroupAccess(groupResources, "device-101"));

        // 9. Seed 7 Days of Activity Logs
        int logsCount = 0;
        for (Employee emp : employees) {
            String email = emp.getName().toLowerCase().replace(" ", ".") + "@school.edu";
            
            // Seed logs for the last 7 days
            for (int dayOffset = 6; dayOffset >= 0; dayOffset--) {
                LocalDateTime dayStart = now.minusDays(dayOffset).withHour(8).withMinute(0).withSecond(0);
                
                // Each student has a random number of activities per day (e.g. 3 to 6)
                int activities = 3 + rng.nextInt(4);
                LocalDateTime current = dayStart;
                
                for (int act = 0; act < activities; act++) {
                    // Pick a random domain/url from our mock array
                    String[] logData = WEB_LOGS[rng.nextInt(WEB_LOGS.length)];
                    
                    // Random interval before next activity (10 to 60 mins)
                    current = current.plusMinutes(10 + rng.nextInt(50));
                    
                    // Duration of the web activity:
                    // Productive: 5 to 45 mins
                    // Neutral: 5 to 30 mins
                    // Unproductive: 2 to 15 mins
                    long durationSecs;
                    if (logData[2].equals("Productive")) {
                        durationSecs = (5 + rng.nextInt(40)) * 60L;
                    } else if (logData[2].equals("Neutral")) {
                        durationSecs = (5 + rng.nextInt(25)) * 60L;
                    } else {
                        durationSecs = (2 + rng.nextInt(13)) * 60L;
                    }
                    
                    LocalDateTime endTime = current.plusSeconds(durationSecs);
                    
                    ActivityLog log = new ActivityLog(
                            email,
                            logData[1],
                            logData[0],
                            current,
                            endTime,
                            durationSecs,
                            emp.getDeviceId()
                    );
                    activityLogRepository.save(log);
                    logsCount++;
                    
                    // Shift pointer
                    current = endTime;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("seededEmployees", employees.size());
        result.put("seededCategories", DOMAIN_CATEGORIES.length);
        result.put("seededBlockedSites", blockedSites.size());
        result.put("seededGroups", 2);
        result.put("seededCredentials", credentials.size());
        result.put("seededLogs", logsCount);
        result.put("message", "✅ Successfully generated watch metrics: " + employees.size() + " devices and " + logsCount + " log history entries!");
        return result;
    }

    @Transactional
    public void clearDemoData() {
        activityLogRepository.deleteAll();
        deviceGroupAccessRepository.deleteAll();
        credentialGroupMappingRepository.deleteAll();
        credentialRepository.deleteAll();
        credentialGroupRepository.deleteAll();
        blockedSiteRepository.deleteAll();
        domainCategoryRepository.deleteAll();
        employeeRepository.deleteAll();
        entityManager.flush();
        System.out.println("🗑️ Cleared all watch-employee demo data.");
    }
}
