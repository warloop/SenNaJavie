package org.example.forum;


import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ForumApplication {

    public static void main(String[] args) {
        Flyway flyway = Flyway.configure().dataSource("jdbc:mysql://localhost:3306/forum", "root", "password").load();

        // Repair step to fix checksum mismatch
        flyway.repair();

        // Migrate after repair
        flyway.migrate();

        SpringApplication.run(ForumApplication.class, args);
    }
}

