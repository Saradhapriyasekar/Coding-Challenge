package com.hexaware.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {

    public static String getPropertyString(String propertyFileName, String key) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertyFileName)) {
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            System.out.println("Error while loading the properties file: " + e.getMessage());
            throw new RuntimeException("Error while loading the properties file");
        }
    }

    public static Properties getProperties(String propertyFileName) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(propertyFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error while loading the properties file: " + e.getMessage());
            throw new RuntimeException("Error while loading the properties file");
        }
        return properties;
    }
}
