package com.eldritch.staticdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try (InputStream input = ConfigService.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Unable to find " + fileName);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load " + fileName, e);
        }
        return properties;
    }
}
