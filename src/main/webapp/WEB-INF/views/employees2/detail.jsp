<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Details</title>
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

        .profile-card {
            max-width: 700px;
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

        .profile-header {
            background: linear-gradient(135deg, #7c3aed, #4f46e5, #2563eb);
            padding: 40px 36px;
            display: flex;
            align-items: center;
            gap: 24px;
            position: relative;
            overflow: hidden;
        }

        .profile-header::before {
            content: '';
            position: absolute;
            top: -50%; right: -10%;
            width: 300px; height: 300px;
            background: rgba(255,255,255,0.05);
            border-radius: 50%;
        }

        .avatar {
            width: 80px; height: 80px;
            background: rgba(255,255,255,0.2);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-size: 2rem;
            border: 3px solid rgba(255,255,255,0.3);
            flex-shrink: 0;
        }

        .profile-header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            color: #fff;
        }

        .profile-header .emp-id {
            color: rgba(255,255,255,0.7);
            font-size: 0.85rem;
            margin-top: 4px;
        }

        .position-pill {
            display: inline-block;
            background: rgba(255,255,255,0.2);
            color: #fff;
            padding: 4px 14px;
            border-radius: 20px;
            font-size: 0.78rem;
            font-weight: 600;
            margin-top: 8px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .profile-body {
            padding: 32px 36px;
        }

        .section-title {
            font-size: 0.72rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1.2px;
            color: #6b7280;
            margin-bottom: 16px;
            margin-top: 28px;
        }
        .section-title:first-child { margin-top: 0; }

        .info-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 14px;
        }

        .info-item {
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.07);
            border-radius: 12px;
            padding: 14px 18px;
            transition: background 0.2s;
        }
        .info-item:hover { background: rgba(255,255,255,0.07); }

        .info-item.full { grid-column: 1 / -1; }

        .info-label {
            font-size: 0.72rem;
            color: #6b7280;
            text-transform: uppercase;
            letter-spacing: 0.8px;
            margin-bottom: 5px;
        }

        .info-value {
            font-size: 1rem;
            font-weight: 500;
            color: #f3f4f6;
        }

        .info-value.salary { color: #34d399; font-weight: 700; font-size: 1.1rem; }
        .info-value.annual { color: #60a5fa; font-weight: 700; font-size: 1.1rem; }

        .divider {
            border: none;
            border-top: 1px solid rgba(255,255,255,0.07);
            margin: 28px 0;
        }

        .action-row {
            display: flex;
            gap: 12px;
            margin-top: 32px;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            border-radius: 12px;
            font-size: 0.88rem;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            border: none;
            transition: all 0.25s ease;
        }

        .btn-primary {
            background: linear-gradient(135deg, #7c3aed, #4f46e5);
            color: #fff;
            box-shadow: 0 4px 15px rgba(124,58,237,0.35);
        }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 8px 25px rgba(124,58,237,0.5); }

        .btn-secondary {
            background: rgba(255,255,255,0.07);
            color: #9ca3af;
            border: 1px solid rgba(255,255,255,0.1);
        }
        .btn-secondary:hover { background: rgba(255,255,255,0.12); color: #e5e7eb; }
    </style>
</head>
<body>

<a href="${pageContext.request.contextPath}/mvc/employees" class="back-link">← Back to Employees</a>

<div class="profile-card">
    <div class="profile-header">
        <div class="avatar">👤</div>
        <div>
            <h1>${employee.name} ${employee.surname}</h1>
            <div class="emp-id">ID: #${employee.id} &nbsp;|&nbsp; Personal ID: ${employee.personalId}</div>
            <span class="position-pill">${employee.position}</span>
        </div>
    </div>

    <div class="profile-body">
        <div class="section-title">💰 Compensation</div>
        <div class="info-grid">
            <div class="info-item">
                <div class="info-label">Monthly Salary</div>
                <div class="info-value salary">$${employee.salary}</div>
            </div>
            <div class="info-item">
                <div class="info-label">Annual Income</div>
                <div class="info-value annual">$${employee.annualIncome}</div>
            </div>
        </div>

        <div class="section-title">🏢 Organisation</div>
        <div class="info-grid">
            <div class="info-item">
                <div class="info-label">Company</div>
                <div class="info-value">${employee.company != null ? employee.company.name : '—'}</div>
            </div>
            <div class="info-item">
                <div class="info-label">Position</div>
                <div class="info-value">${employee.position}</div>
            </div>
        </div>

        <c:if test="${employee.address != null}">
            <div class="section-title">📍 Address</div>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">City</div>
                    <div class="info-value">${employee.address.city}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">Postal Code</div>
                    <div class="info-value">${employee.address.postal}</div>
                </div>
                <div class="info-item full">
                    <div class="info-label">Street</div>
                    <div class="info-value">${employee.address.street}</div>
                </div>
            </div>
        </c:if>

        <hr class="divider"/>
        <div class="action-row">
            <a href="${pageContext.request.contextPath}/mvc/employees/${employee.id}/edit" class="btn btn-primary">✏️ Edit Employee</a>
            <a href="${pageContext.request.contextPath}/mvc/employees" class="btn btn-secondary">← Back to List</a>
        </div>
    </div>
</div>

</body>
</html>

