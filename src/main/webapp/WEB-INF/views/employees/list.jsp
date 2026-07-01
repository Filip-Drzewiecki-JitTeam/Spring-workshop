<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employees</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background-color: #f2f2f2; }
        a { text-decoration: none; color: #0066cc; }
        a:hover { text-decoration: underline; }
        .btn { padding: 5px 10px; cursor: pointer; }
    </style>
</head>
<body>
<h1>Employees</h1>
<a href="${pageContext.request.contextPath}/mvc/employees/new">+ New Employee</a>
<br/><br/>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Surname</th>
        <th>Personal ID</th>
        <th>Salary</th>
        <th>Position</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="emp" items="${employees}">
        <tr>
            <td>${emp.id}</td>
            <td>${emp.name}</td>
            <td>${emp.surname}</td>
            <td>${emp.personalId}</td>
            <td>${emp.salary}</td>
            <td>${emp.position}</td>
            <td>
                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}">View</a> |
                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}/edit">Edit</a> |
                <form method="post"
                      action="${pageContext.request.contextPath}/mvc/employees/${emp.id}/delete"
                      style="display:inline;"
                      onsubmit="return confirm('Delete employee ${emp.name} ${emp.surname}?');">
                    <button type="submit" class="btn">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

