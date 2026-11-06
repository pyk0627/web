package com.shop.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** 商品数据操作 JavaBean：封装对 product 表的增、查、删 */
public class ProductDao implements Serializable {
    private static final long serialVersionUID = 1L;

    /* 数据库连接参数（提供 getter/setter，符合 JavaBean 规范，便于注入） */
    private String driver   = "com.mysql.cj.jdbc.Driver";
    private String url      = "jdbc:mysql://localhost:3306/productdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";
    private String username = "root";
    private String password = "123456";   // ←← 改成你自己的 MySQL 密码

    public String getDriver() { return driver; }
    public void setDriver(String driver) { this.driver = driver; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    private Connection getConnection() throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

  
    public boolean addProduct(Product p) {
        String sql = "INSERT INTO product(name, stock, price, produce_date) VALUES(?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getStock());
            ps.setDouble(3, p.getPrice());
            ps.setDate(4, p.getProduceDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Product> findAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, name, stock, price, produce_date FROM product ORDER BY id";
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setStock(rs.getInt("stock"));
                p.setPrice(rs.getDouble("price"));
                p.setProduceDate(rs.getDate("produce_date"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}