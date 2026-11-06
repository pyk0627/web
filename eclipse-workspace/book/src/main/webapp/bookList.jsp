<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="entity.Book"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>图书信息管理</title>
</head>
<body>
<table border="1" width="720" align="center" cellpadding="6" cellspacing="2"
       style="border-collapse:collapse; text-align:center;">
    <tr>
        <th width="15%">编号</th>
        <th width="20%">作者</th>
        <th width="40%">书名</th>
        <th width="25%">操作</th>
    </tr>
    <%
        List<Book> books = (List<Book>) request.getAttribute("books");
        if (books != null && !books.isEmpty()) {
            for (Book b : books) {
    %>
    <tr>
        <td><%=b.getId()%></td>
        <td><%=b.getAuthor()%></td>
        <td><%=b.getTitle()%></td>
        <td>
            <form action="<%=request.getContextPath()%>/bookDelete" method="post" style="margin:0;">
                <input type="hidden" name="id" value="<%=b.getId()%>" />
                <input type="submit" value="删除"
                       onclick="return confirm('确定删除《<%=b.getTitle()%>》吗？');" />
            </form>
        </td>
    </tr>
    <%
            }
        } else {
    %>
    <tr><td colspan="4">暂无数据</td></tr>
    <%
        }
    %>
</table>
</body>
</html>