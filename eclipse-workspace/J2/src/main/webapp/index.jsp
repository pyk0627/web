<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 1. 检查 Session 是否已经登录
    String loginUser = (String) session.getAttribute("loginUser");
    if (loginUser != null) {
        response.sendRedirect("main.jsp");
        return;
    }

    // 2. 检查 Cookie 实现自动登录
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        String autoUser = null, autoPwd = null;
        for (Cookie c : cookies) {
            if ("auto_user".equals(c.getName())) autoUser = c.getValue();
            if ("auto_pwd".equals(c.getName())) autoPwd = c.getValue();
        }
        // 如果存在自动登录凭证，在后端直接验证并放行
        if (autoUser != null && autoPwd != null) {
            com.dao.UserDao dao = new com.dao.UserDao();
            if (dao.login(autoUser, autoPwd)) {
                session.setAttribute("loginUser", autoUser);
                response.sendRedirect("main.jsp");
                return;
            }
        }
    }

    // 获取提示信息
    String error = request.getParameter("error");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录与注册</title>
    <style>
        body { font-family: Arial; text-align: center; margin-top: 50px; background-color: #f0f2f5; }
        .box { background: white; padding: 30px; border-radius: 8px; display: inline-block; box-shadow: 0 4px 8px rgba(0,0,0,0.1); width: 300px;}
        input { width: 100%; padding: 8px; margin: 5px 0; box-sizing: border-box; }
        button { width: 48%; padding: 10px; margin-top: 10px; cursor: pointer; border: none; border-radius: 4px; color: white; }
        .btn-login { background-color: #1890ff; }
        .btn-register { background-color: #52c41a; }
        .msg-red { color: red; font-size: 14px; }
        .msg-green { color: green; font-size: 14px; }
    </style>
</head>
<body>
    <div class="box">
        <h2>欢迎使用系统</h2>
        <% if ("login".equals(error)) { %>
            <p class="msg-red">账号或密码错误，请重试！</p>
        <% } else if ("register".equals(error)) { %>
            <p class="msg-red">注册失败，用户名可能已存在！</p>
        <% } else if ("register".equals(success)) { %>
            <p class="msg-green">注册成功，请登录！</p>
        <% } %>

        <!-- 核心：一个表单，通过 JS 决定提交给哪个 Servlet -->
        <form id="userForm" method="post">
            <input type="text" name="username" placeholder="请输入用户名" required>
            <input type="password" name="password" placeholder="请输入密码" required>
            <div style="text-align: left; margin: 10px 0;">
                <input type="checkbox" name="autoLogin" style="width: auto;"> 开启自动登录 (7天内免密)
            </div>
            <button type="button" class="btn-login" onclick="submitForm('LoginServlet')">登 录</button>
            <button type="button" class="btn-register" onclick="submitForm('RegisterServlet')">注 册</button>
        </form>
    </div>

    <script>
        // 思考题解答：动态修改表单的 action 属性
        function submitForm(targetServlet) {
            document.getElementById('userForm').action = targetServlet;
            document.getElementById('userForm').submit();
        }
    </script>
</body>
</html>