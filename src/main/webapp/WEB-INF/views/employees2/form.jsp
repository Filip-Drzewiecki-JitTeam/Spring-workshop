<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>New Employee</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
            min-height: 100vh;
            color: #e0e0e0;
            padding: 40px 30px;
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            color: #9ca3af;
            text-decoration: none;
            font-size: 0.88rem;
            margin-bottom: 28px;
            transition: color 0.2s;
        }
        .back-link:hover { color: #a78bfa; }

        .form-card {
            max-width: 600px;
            margin: 0 auto;
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 24px;
            overflow: hidden;
            box-shadow: 0 30px 60px rgba(0,0,0,0.5);
            animation: slideUp 0.4s ease;
        }

        @keyframes slideUp {
            from { opacity: 0; transform: translateY(30px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        .form-header {
            background: linear-gradient(135deg, #7c3aed, #4f46e5);
            padding: 30px 36px;
        }

        .form-header h1 {
            font-size: 1.6rem;
            font-weight: 700;
            color: #fff;
        }

        .form-header p {
            color: rgba(255,255,255,0.7);
            font-size: 0.88rem;
            margin-top: 4px;
        }

        .form-body { padding: 32px 36px; }

        .section-title {
            font-size: 0.7rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1.2px;
            color: #6b7280;
            margin-bottom: 16px;
            margin-top: 28px;
        }
        .section-title:first-child { margin-top: 0; }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group label {
            display: block;
            font-size: 0.82rem;
            font-weight: 500;
            color: #9ca3af;
            margin-bottom: 7px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .form-group input {
            width: 100%;
            background: rgba(255,255,255,0.07);
            border: 1px solid rgba(255,255,255,0.12);
            border-radius: 10px;
            padding: 12px 16px;
            color: #f3f4f6;
            font-family: 'Inter', sans-serif;
            font-size: 0.95rem;
            transition: all 0.25s ease;
            outline: none;
        }

        .form-group input:focus {
            border-color: #7c3aed;
            background: rgba(124,58,237,0.1);
            box-shadow: 0 0 0 3px rgba(124,58,237,0.2);
        }

        .form-group input::placeholder { color: #4b5563; }

        .error {
            display: block;
            color: #f87171;
            font-size: 0.78rem;
            margin-top: 5px;
        }

        .address-section {
            background: rgba(255,255,255,0.03);
            border: 1px solid rgba(255,255,255,0.07);
            border-radius: 14px;
            padding: 20px;
            margin-top: 8px;
        }

        .address-section .section-title { margin-top: 0; }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
        }

        .form-actions {
            display: flex;
            align-items: center;
            gap: 14px;
            margin-top: 32px;
        }

        .btn-submit {
            flex: 1;
            background: linear-gradient(135deg, #7c3aed, #4f46e5);
            color: #fff;
            border: none;
            border-radius: 12px;
            padding: 14px 28px;
            font-family: 'Inter', sans-serif;
            font-size: 0.95rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.25s ease;
            box-shadow: 0 4px 20px rgba(124,58,237,0.35);
        }
        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 30px rgba(124,58,237,0.55);
        }

        .btn-cancel {
            color: #6b7280;
            text-decoration: none;
            font-size: 0.88rem;
            font-weight: 500;
            padding: 14px 20px;
            border-radius: 12px;
            transition: all 0.2s;
            background: rgba(255,255,255,0.05);
            border: 1px solid rgba(255,255,255,0.08);
        }
        .btn-cancel:hover { color: #e5e7eb; background: rgba(255,255,255,0.1); }
    </style>
</head>
<body>

<a href="${pageContext.request.contextPath}/mvc/employees" class="back-link">← Back to Employees</a>

<div class="form-card">
    <div class="form-header">
        <h1>✨ New Employee</h1>
        <p>Fill in the details to add a new team member</p>
    </div>

    <div class="form-body">
        <form:form method="post" action="${pageContext.request.contextPath}/mvc/employees"
                   modelAttribute="employeeForm">

            <div class="section-title">👤 Personal Information</div>

            <div class="form-row">
                <div class="form-group">
                    <label>First Name</label>
                    <form:input path="name" placeholder="e.g. John"/>
                    <form:errors path="name" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label>Surname</label>
                    <form:input path="surname" placeholder="e.g. Doe"/>
                    <form:errors path="surname" cssClass="error"/>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Personal ID</label>
                    <form:input path="personalId" placeholder="e.g. 9001010001"/>
                    <form:errors path="personalId" cssClass="error"/>
                </div>
                <div class="form-group">
                    <label>Monthly Salary ($)</label>
                    <form:input path="salary" placeholder="e.g. 5000"/>
                    <form:errors path="salary" cssClass="error"/>
                </div>
            </div>

            <div class="address-section">
                <div class="section-title">📍 Address (optional)</div>
                <div class="form-group">
                    <label>City</label>
                    <form:input path="address.city" placeholder="e.g. Warsaw"/>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Street</label>
                        <form:input path="address.street" placeholder="e.g. Main St 1"/>
                    </div>
                    <div class="form-group">
                        <label>Postal Code</label>
                        <form:input path="address.postal" placeholder="e.g. 00-001"/>
                    </div>
                </div>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit">🚀 Save Employee</button>
                <a href="${pageContext.request.contextPath}/mvc/employees" class="btn-cancel">Cancel</a>
            </div>

        </form:form>
    </div>
</div>

</body>
</html>

