package ru.stoliarenkoas.tm.webserver.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionUtil {

    private volatile static Connection connection;

    @Nullable
    public static Connection getJDBCConnection() {
        if (connection == null) {
            synchronized (DatabaseConnectionUtil.class) {
                if (connection == null) {
                    try (InputStream input = DatabaseConnectionUtil.class.getClassLoader().getResourceAsStream("application.properties")){
                        Properties prop = new Properties();
                        if (input == null) return null;
                        prop.load(input);
                        connection = DriverManager.getConnection(
                                prop.getProperty("database.url"),
                                prop.getProperty("database.login"),
                                prop.getProperty("database.password"));

                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("[DATABASE CONNECTION ESTABLISHED]");
                }
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection == null) return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
