<%--
  Created by IntelliJ IDEA.
  User: Радик
  Date: 08.02.2020
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <style>
        .green {
            color: forestgreen;
        }

        .red {
            color: crimson;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>
        <th>Дата Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:set var="mealsTo" value="${requestScope.mealsTo}"/>
    <c:set var="jspFormatter" value="${requestScope.jspFormatter}"/>
    <c:forEach items="${mealsTo}" var="mealTo">
        <c:set var="rowColor" value="${mealTo.excess ?  'red' : 'green'}"/>
        <tr class="${rowColor}">
            <td>${mealTo.dateTime.format(jspFormatter)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${mealTo.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${mealTo.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
