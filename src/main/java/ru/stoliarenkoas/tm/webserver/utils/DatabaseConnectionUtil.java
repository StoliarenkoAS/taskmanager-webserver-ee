package ru.stoliarenkoas.tm.webserver.utils;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionUtil {

    @Nullable
    public static Connection getJDBCConnection() {
        Connection connection = null;
        try (InputStream input = DatabaseConnectionUtil.class.getClassLoader().getResourceAsStream("mysql-database.properties")){
            Properties prop = new Properties();
            if (input == null) return null;
            prop.load(input);
            Class.forName(prop.getProperty("connector.class"));
            connection = DriverManager.getConnection(
                    prop.getProperty("database.url"),
                    prop.getProperty("database.login"),
                    prop.getProperty("database.password"));

        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("[DATABASE CONNECTION ESTABLISHED]");
        return connection;
    }
    
}
