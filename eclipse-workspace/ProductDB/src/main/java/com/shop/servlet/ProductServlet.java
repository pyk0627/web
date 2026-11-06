package com.shop.servlet;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import com.shop.bean.Product;
import com.shop.bean.ProductDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDao dao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) action = "list";

        switch (action) {
            case "add": {    // 商品录入
                Product p = new Product();
                p.setName(req.getParameter("name"));
                p.setStock(parseInt(req.getParameter("stock"), 0));
                p.setPrice(parseDouble(req.getParameter("price"), 0));
                String d = req.getParameter("produceDate");
                if (d != null && !d.isEmpty()) {
                    p.setProduceDate(Date.valueOf(d));  // date 控件格式为 yyyy-MM-dd
                }
                boolean ok = dao.addProduct(p);
                req.setAttribute("msg", ok ? "录入成功！" : "录入失败！请检查数据库连接与账号密码");
                list(req, resp);   // 录入后直接展示列表
                break;
            }
            case "delete": { // 附加功能
                dao.deleteProduct(parseInt(req.getParameter("id"), -1));
                resp.sendRedirect(req.getContextPath() + "/product?action=list");
                break;
            }
            default:
                list(req, resp);   // 展示全部商品
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Product> list = dao.findAllProducts();
        req.setAttribute("productList", list);
        req.getRequestDispatcher("/list.jsp").forward(req, resp);
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return def; }
    }

    private double parseDouble(String s, double def) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return def; }
    }
}