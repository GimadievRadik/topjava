<%--
  Created by IntelliJ IDEA.
  User: Радик
  Date: 10.02.2020
  Time: 21:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Let's edit meal</title>
</head>
<body>

<form method="POST" action='meals' name="editMeal">
    <c:set var="meal" value="${requestScope.meal}"/>
    Дата и время приема : <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}" />"/>
    <br/>
    Описание : <input type="text" name="description" value="<c:out value="${meal.description}" />"/> <br/>
    Калории : <input required type="number" name="calories" value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>

</body>
</html>
