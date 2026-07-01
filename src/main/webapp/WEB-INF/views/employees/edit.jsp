<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Employee</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        label { display: inline-block; width: 120px; font-weight: bold; }
        input, select { margin-bottom: 8px; padding: 4px; width: 220px; }
        .error { color: red; font-size: 0.85em; }
        .btn { padding: 6px 16px; cursor: pointer; }
        fieldset { margin-top: 10px; border: 1px solid #ccc; padding: 10px; }
        .current { color: #555; font-size: 0.9em; margin-bottom: 12px; }
    </style>
</head>
<body>
<h1>Edit Employee</h1>
<div class="current">
    Editing: <strong>${employee.name} ${employee.surname}</strong> (ID: ${employee.id})
</div>
<form:form method="post"
           action="${pageContext.request.contextPath}/mvc/employees/${employee.id}"
           modelAttribute="employeeUpdateForm">
    <div>
        <label>Name:</label>
        <form:input path="name" value="${employee.name}"/>
        <form:errors path="name" cssClass="error"/>
    </div>
    <div>
        <label>Salary:</label>
        <form:input path="salary" value="${employee.salary}"/>
        <form:errors path="salary" cssClass="error"/>
    </div>
    <div>
        <label>Position:</label>
        <form:select path="position">
            <form:option value="" label="-- Select --"/>
            <c:forEach var="pos" items="${positions}">
                <form:option value="${pos}" label="${pos}"/>
            </c:forEach>
        </form:select>
    </div>
    <div>
        <label>Company ID:</label>
        <form:input path="companyId" value="${employee.company != null ? employee.company.id : ''}"/>
    </div>
    <fieldset>
        <legend>Address</legend>
        <div>
            <label>City:</label>
            <form:input path="address.city"
                        value="${employee.address != null ? employee.address.city : ''}"/>
        </div>
        <div>
            <label>Street:</label>
            <form:input path="address.street"
                        value="${employee.address != null ? employee.address.street : ''}"/>
        </div>
        <div>
            <label>Postal:</label>
            <form:input path="address.postal"
                        value="${employee.address != null ? employee.address.postal : ''}"/>
        </div>
    </fieldset>
    <br/>
    <button type="submit" class="btn">Update</button>
    <a href="${pageContext.request.contextPath}/mvc/employees/${employee.id}">Cancel</a>
</form:form>
</body>
</html>

