package com.servlet;

import com.dao.UserDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String autoLogin = request.getParameter("autoLogin"); // 获取自动登录复选框的值

        UserDao userDao = new UserDao();
        if (userDao.login(username, password)) {
            // 登录成功，将用户存入 Session
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", username);

            // 处理自动登录 (Cookie)
            if ("on".equals(autoLogin)) {
                Cookie userCookie = new Cookie("auto_user", username);
                Cookie pwdCookie = new Cookie("auto_pwd", password);
                userCookie.setMaxAge(60 * 60 * 24 * 7); // 保存7天
                pwdCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(userCookie);
                response.addCookie(pwdCookie);
            }
            response.sendRedirect("main.jsp"); // 跳转到主页
        } else {
            response.sendRedirect("index.jsp?error=login"); // 登录失败，回到首页
        }
    }
}