<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String user = (String) session.getAttribute("loginUser");
    if (user == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>系统主页</title>
    <style>body { text-align: center; margin-top: 100px; font-family: sans-serif; }</style>
</head>
<body>
    <h1>欢迎回来，<%= user %>！</h1>
    <p>您已成功进入系统内部。</p>
    <!-- 注销功能：清除 Session -->
   
</body>
</html>