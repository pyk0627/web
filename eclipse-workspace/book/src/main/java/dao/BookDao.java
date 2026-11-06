package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Book;
import util.DBUtil;

public class BookDao {

    /** 查询全部图书 */
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT id, author, title FROM book ORDER BY id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setAuthor(rs.getString("author"));
                b.setTitle(rs.getString("title"));
                list.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 按编号删除图书（PreparedStatement 占位符防 SQL 注入） */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);                 // 设置第1个?参数
            return ps.executeUpdate() > 0;    // 返回受影响行数
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}