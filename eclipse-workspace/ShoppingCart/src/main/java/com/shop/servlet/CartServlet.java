package com.shop.servlet;

import com.shop.bean.Product;
import com.shop.bean.ShoppingCart;
import com.shop.dao.ProductDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ProductDao productDao = new ProductDao();

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
        resp.setContentType("text/html;charset=UTF-8");


        HttpSession session = req.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }

        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) action = "view";
        String ctx = req.getContextPath();


        switch (action) {
            case "add": {                                  
                Product p = productDao.findById(parseInt(req.getParameter("id"), -1));
                if (p != null) {
                    cart.addProduct(p, parseInt(req.getParameter("qty"), 1));
                }
                resp.sendRedirect(ctx + "/cart?action=view");
                break;
            }
            case "remove":                                 
                cart.removeProduct(parseInt(req.getParameter("id"), -1));
                resp.sendRedirect(ctx + "/cart?action=view");
                break;
            case "update":                                 
                cart.updateQuantity(parseInt(req.getParameter("id"), -1),
                                    parseInt(req.getParameter("qty"), 1));
                resp.sendRedirect(ctx + "/cart?action=view");
                break;
            case "clear":                                  
                cart.clear();
                resp.sendRedirect(ctx + "/cart?action=view");
                break;
            default:                                       
                req.getRequestDispatcher("/cart.jsp").forward(req, resp);
        }
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }
}