<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List,com.shop.bean.Product" %>
<%-- 用 jsp:useBean 声明购物车 JavaBean，scope=session，第一次访问时自动创建 --%>
<jsp:useBean id="cart" class="com.shop.bean.ShoppingCart" scope="session"/>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    if (products == null) {
        response.sendRedirect(request.getContextPath() + "/products");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品列表</title>
    <style>
        body { font-family: "Microsoft YaHei", Arial; margin: 30px; }
        table { border-collapse: collapse; }
        th { background: #eef3ff; }
        td, th { border: 1px solid #ccc; padding: 8px 12px; }
        form { margin: 0; }
        .bar { margin-bottom: 15px; }
    </style>
</head>
<body>
<h2>商品列表</h2>

<div class="bar">
    <a href="${pageContext.request.contextPath}/products">刷新商品</a> &nbsp;|&nbsp;
    <a href="${pageContext.request.contextPath}/cart?action=view">
        查看购物车（<%= cart.getTotalCount() %> 件，￥<%= String.format("%.2f", cart.getTotalPrice()) %>）
    </a>
</div>

<table>
    <tr>
        <th>编号</th><th>商品名称</th><th>描述</th><th>单价</th><th>购买</th>
    </tr>
    <% for (Product p : products) { %>
    <tr>
        <td><%= p.getId() %></td>
        <td><%= p.getName() %></td>
        <td><%= p.getDescription() %></td>
        <td>￥<%= String.format("%.2f", p.getPrice()) %></td>
        <td>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="id" value="<%= p.getId() %>">
                <input type="number" name="qty" value="1" min="1" style="width:60px">
                <button type="submit">加入购物车</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>