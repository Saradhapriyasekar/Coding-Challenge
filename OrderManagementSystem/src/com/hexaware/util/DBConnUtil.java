package com.hexaware.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnUtil {

    
    public static Connection getDBConn(String fileName) {
        Connection conn = null;
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException("Properties file not found: " + fileName);
            }

            // Load properties from the file
            Properties props = new Properties();
            props.load(is);

            // Get database connection details from the properties file
            String url = props.getProperty("db.connectionString");
            String user = props.getProperty("db.username");
            String pass = props.getProperty("db.password");

            // Establish the connection
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Error while establishing database connection: " + e.getMessage());
        }
        return conn;
    }
}
