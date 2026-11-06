<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.shop.bean.Product"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>商品列表</title></head>
<body>
    <h2>商品列表</h2>
    <%
        String msg = (String) request.getAttribute("msg");
        if (msg != null) {
    %>
        <p style="color:green;"><%= msg %></p>
    <%
        }
    %>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>商品编号</th><th>商品名称</th><th>商品存货</th>
            <th>商品价格(元)</th><th>生产日期</th><th>操作</th>
        </tr>
        <%
            List<Product> list = (List<Product>) request.getAttribute("productList");
            if (list != null) {
                for (Product p : list) {
        %>
        <tr>
            <td><%= p.getId() %></td>
            <td><%= p.getName() %></td>
            <td><%= p.getStock() %></td>
            <td><%= String.format("%.2f", p.getPrice()) %></td>
            <td><%= p.getProduceDate() %></td>
            <td><a href="${pageContext.request.contextPath}/product?action=delete&id=<%= p.getId() %>">删除</a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
    <p><a href="${pageContext.request.contextPath}/add.jsp">继续录入商品</a></p>
</body>
</html>