package mh.cyb.root.watch_employee.config;

import mh.cyb.root.watch_employee.entity.AppUser;
import mh.cyb.root.watch_employee.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final mh.cyb.root.watch_employee.repository.DomainCategoryRepository domainCategoryRepository;
    private final mh.cyb.root.watch_employee.repository.EmployeeRepository employeeRepository;
    private final mh.cyb.root.watch_employee.service.DemoDataService demoDataService;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
            mh.cyb.root.watch_employee.repository.DomainCategoryRepository domainCategoryRepository,
            mh.cyb.root.watch_employee.repository.EmployeeRepository employeeRepository,
            mh.cyb.root.watch_employee.service.DemoDataService demoDataService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.domainCategoryRepository = domainCategoryRepository;
        this.employeeRepository = employeeRepository;
        this.demoDataService = demoDataService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@productivity.com");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Default admin user created: admin / admin123");
        }

        if (employeeRepository.count() == 0) {
            demoDataService.regenerate();
            System.out.println("Automatic Student Watch mock database seeding completed on startup.");
        }

        if (domainCategoryRepository.count() == 0) {
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("github.com", "Productive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("stackoverflow.com", "Productive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("google.com", "Productive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("youtube.com", "Unproductive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("facebook.com", "Unproductive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("twitter.com", "Unproductive"));
            domainCategoryRepository
                    .save(new mh.cyb.root.watch_employee.entity.DomainCategory("linkedin.com", "Neutral"));
            System.out.println("Default domain categories seeded.");
        }
    }
}
