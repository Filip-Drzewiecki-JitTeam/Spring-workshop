<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Details</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; }
        td { padding: 6px 12px; border: 1px solid #ccc; }
        td:first-child { font-weight: bold; background: #f2f2f2; }
        a { text-decoration: none; color: #0066cc; }
    </style>
</head>
<body>
<h1>Employee Details</h1>
<table>
    <tr><td>ID</td><td>${employee.id}</td></tr>
    <tr><td>Name</td><td>${employee.name}</td></tr>
    <tr><td>Surname</td><td>${employee.surname}</td></tr>
    <tr><td>Personal ID</td><td>${employee.personalId}</td></tr>
    <tr><td>Salary</td><td>${employee.salary}</td></tr>
    <tr><td>Annual Income</td><td>${employee.annualIncome}</td></tr>
    <tr><td>Position</td><td>${employee.position}</td></tr>
    <tr><td>Company</td><td>${employee.company != null ? employee.company.name : '-'}</td></tr>
    <c:if test="${employee.address != null}">
        <tr><td>City</td><td>${employee.address.city}</td></tr>
        <tr><td>Street</td><td>${employee.address.street}</td></tr>
        <tr><td>Postal</td><td>${employee.address.postal}</td></tr>
    </c:if>
</table>
<br/>
<a href="${pageContext.request.contextPath}/mvc/employees/${employee.id}/edit">Edit</a> |
<a href="${pageContext.request.contextPath}/mvc/employees">Back to list</a>
</body>
</html>

