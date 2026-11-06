package com.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/user_db?serverTimezone=UTC&useSSL=false";
    private static final String USER = "root";      // 你的MySQL账号
    private static final String PASSWORD = "123456"; // 你的MySQL密码

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8.0+ 驱动
        } catch (ClassNotFoundException e) {
            try { Class.forName("com.mysql.jdbc.Driver"); } catch (Exception ex) {}
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}