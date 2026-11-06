<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>商品录入</title></head>
<body>
    <h2>商品录入</h2>
    <form action="${pageContext.request.contextPath}/product?action=add" method="post">
        <table>
            <tr><td>商品名称</td><td><input type="text" name="name" required></td></tr>
            <tr><td>商品存货</td><td><input type="number" name="stock" min="0" value="0" required></td></tr>
            <tr><td>商品价格</td><td><input type="number" name="price" step="0.01" min="0" required></td></tr>
            <tr><td>生产日期</td><td><input type="date" name="produceDate" required></td></tr>
            <tr><td colspan="2"><input type="submit" value="提交录入"></td></tr>
        </table>
    </form>
    <a href="${pageContext.request.contextPath}/product?action=list">查看商品列表</a>
</body>
</html>