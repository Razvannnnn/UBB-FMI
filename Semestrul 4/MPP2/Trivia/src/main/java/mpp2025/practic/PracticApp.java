package mpp2025.practic;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {
        "mpp2025.practic",
        "mpp2025.service",
        "mpp2025.repo",
        "mpp2025.domain",
        "mpp2025.dtos",
        "mpp2025.utils",
        "mpp2025"
})
public class PracticApp {
    @Bean
    CommandLineRunner runner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Beans:");
            for (String name : ctx.getBeanDefinitionNames()) {
                System.out.println(name);
            }
        };
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(PracticApp.class, args);
    }
}
