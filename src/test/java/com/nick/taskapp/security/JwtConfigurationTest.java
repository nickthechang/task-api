package com.nick.taskapp.security;

import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.Reader;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigurationTest {
    private Properties load(String name) throws Exception {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(Path.of("src/main/resources", name))) {
            properties.load(reader);
        }
        return properties;
    }

    @Test
    void productionSourcesNeverContainAStaticSigningKey() throws Exception {
        for (String name : new String[] {
                "application.properties", "application-dev.properties", "application-prod.properties"
        }) {
            assertEquals("${JWT_SECRET}", load(name).getProperty("jwt.secret"), name);
        }
    }

    @Test
    void defaultStartupDoesNotForceDevProfileOrLogSqlParameters() throws Exception {
        Properties base = load("application.properties");
        assertNull(base.getProperty("spring.profiles.active"));
        assertEquals("false", base.getProperty("spring.jpa.show-sql"));
        assertEquals("OFF", base.getProperty("logging.level.org.hibernate.orm.jdbc.bind"));
    }
}
