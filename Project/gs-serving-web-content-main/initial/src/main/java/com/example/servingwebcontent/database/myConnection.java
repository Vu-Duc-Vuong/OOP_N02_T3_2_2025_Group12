package com.example.servingwebcontent.database;

import java.sql.Connection;
import java.sql.DriverManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class myConnection {
    @Value(value = "${app.database.url:}")
    private String altUrl;
    @Value(value = "${app.database.driver:}")
    private String altDriver;
    @Value(value = "${app.database.user:}")
    private String altUser;
    @Value(value = "${app.database.password:}")
    private String altPassword;

    @Value(value = "${spring.datasource.url:}")
    private String stdUrl;
    @Value(value = "${spring.datasource.driver-class-name:}")
    private String stdDriver;
    @Value(value = "${spring.datasource.username:}")
    private String stdUser;
    @Value(value = "${spring.datasource.password:}")
    private String stdPassword;

    private boolean hasAlt() {
        return altUrl != null && !altUrl.isBlank();
    }

    public Connection getConnection() {
        final String url = hasAlt() ? altUrl : stdUrl;
        final String driver = hasAlt() ? altDriver : stdDriver;
        final String user = hasAlt() ? altUser : stdUser;
        final String pass = hasAlt() ? altPassword : stdPassword;

        System.out.println("[myConnection] Mode=" + (hasAlt() ? "app.database" : "spring.datasource"));
        System.out.println("[myConnection] URL=" + url);
        System.out.println("[myConnection] User=" + user);

        try {
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("[myConnection] ERROR: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
