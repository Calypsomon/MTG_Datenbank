package controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import config.Config;

@Component
public class SchemaInit implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String sql = new String(
            new ClassPathResource("schema.sql").getInputStream().readAllBytes(),
            StandardCharsets.UTF_8);

        try (Connection c = DriverManager.getConnection(Config.URL, Config.USERNAME, Config.PASSWORD);
             Statement s = c.createStatement()) {
            s.execute(sql);
        }

        new File(Config.DATA_PATH).mkdirs();
    }
}