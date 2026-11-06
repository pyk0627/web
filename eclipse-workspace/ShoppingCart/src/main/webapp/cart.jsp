<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Collection,com.shop.bean.CartItem" %>
<%-- 取 session 中的购物车 JavaBean，若不存在则自动创建 --%>
<jsp:useBean id="cart" class="com.shop.bean.ShoppingCart" scope="session"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的购物车</title>
    <style>
        body { font-family: "Microsoft YaHei", Arial; margin: 30px; }
        table { border-collapse: collapse; }
        th { background: #eef3ff; }
        td, th { border: 1px solid #ccc; padding: 8px 12px; }
        form { margin: 0; }
        .total { font-size: 18px; color: #c00; margin-top: 15px; }
    </style>
</head>
<body>
<h2>我的购物车</h2>

<%
    Collection<CartItem> items = cart.getItems();
    if (items.isEmpty()) {
%>
    <p>购物车还是空的，<a href="${pageContext.request.contextPath}/products">去逛逛</a></p>
<%
    } else {
%>
<table>
    <tr>
        <th>商品名称</th><th>单价</th><th>数量</th><th>小计</th><th>操作</th>
    </tr>
    <% for (CartItem it : items) { %>
    <tr>
        <td><%= it.getProduct().getName() %></td>
        <td>￥<%= String.format("%.2f", it.getProduct().getPrice()) %></td>
        <td>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="<%= it.getProduct().getId() %>">
                <input type="number" name="qty" value="<%= it.getQuantity() %>" min="0" style="width:60px">
                <button type="submit">修改</button>
            </form>
        </td>
        <td>￥<%= String.format("%.2f", it.getSubtotal()) %></td>
        <td>
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="id" value="<%= it.getProduct().getId() %>">
                <button type="submit">删除</button>
            </form>
        </td>
    </tr>
    <% } %>
</table>

<p class="total">
    共 <%= cart.getTotalCount() %> 件商品，总金额：<b>￥<%= String.format("%.2f", cart.getTotalPrice()) %></b>
</p>

<form action="${pageContext.request.contextPath}/cart" method="post">
    <input type="hidden" name="action" value="clear">
    <button type="submit">清空购物车</button>
</form>
<%
    }
%>
<p><a href="${pageContext.request.contextPath}/products">继续购物</a></p>
</body>
</html>