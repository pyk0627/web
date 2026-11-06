package com.servlet;

import com.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UserDao userDao = new UserDao();
        if (userDao.register(username, password)) {
            // 注册成功后，可以直接让其登录，或者跳回登录页
            response.sendRedirect("index.jsp?success=register");
        } else {
            response.sendRedirect("index.jsp?error=register");
        }
    }
}