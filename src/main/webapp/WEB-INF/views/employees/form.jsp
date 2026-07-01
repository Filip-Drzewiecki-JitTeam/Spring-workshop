<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>New Employee</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        label { display: inline-block; width: 120px; font-weight: bold; }
        input { margin-bottom: 8px; padding: 4px; width: 220px; }
        .error { color: red; font-size: 0.85em; }
        .btn { padding: 6px 16px; cursor: pointer; }
        fieldset { margin-top: 10px; border: 1px solid #ccc; padding: 10px; }
    </style>
</head>
<body>
<h1>New Employee</h1>
<form:form method="post" action="${pageContext.request.contextPath}/mvc/employees"
           modelAttribute="employeeForm">
    <div>
        <label>Name:</label>
        <form:input path="name"/>
        <form:errors path="name" cssClass="error"/>
    </div>
    <div>
        <label>Surname:</label>
        <form:input path="surname"/>
        <form:errors path="surname" cssClass="error"/>
    </div>
    <div>
        <label>Personal ID:</label>
        <form:input path="personalId"/>
        <form:errors path="personalId" cssClass="error"/>
    </div>
    <div>
        <label>Salary:</label>
        <form:input path="salary"/>
        <form:errors path="salary" cssClass="error"/>
    </div>
    <fieldset>
        <legend>Address (optional)</legend>
        <div>
            <label>City:</label>
            <form:input path="address.city"/>
        </div>
        <div>
            <label>Street:</label>
            <form:input path="address.street"/>
        </div>
        <div>
            <label>Postal:</label>
            <form:input path="address.postal"/>
        </div>
    </fieldset>
    <br/>
    <button type="submit" class="btn">Save</button>
    <a href="${pageContext.request.contextPath}/mvc/employees">Cancel</a>
</form:form>
</body>
</html>

