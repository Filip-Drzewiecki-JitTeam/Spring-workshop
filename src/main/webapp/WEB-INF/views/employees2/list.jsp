<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employees</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <style>
        *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
            min-height: 100vh;
            color: #e0e0e0;
            padding: 40px 30px;
        }

        .page-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 36px;
        }

        .page-header h1 {
            font-size: 2.2rem;
            font-weight: 700;
            background: linear-gradient(90deg, #a78bfa, #60a5fa);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            letter-spacing: -0.5px;
        }

        .page-header .subtitle {
            font-size: 0.9rem;
            color: #9ca3af;
            margin-top: 4px;
        }

        .btn-new {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background: linear-gradient(135deg, #7c3aed, #4f46e5);
            color: #fff;
            padding: 12px 24px;
            border-radius: 12px;
            text-decoration: none;
            font-weight: 600;
            font-size: 0.9rem;
            transition: all 0.3s ease;
            box-shadow: 0 4px 20px rgba(124, 58, 237, 0.4);
        }

        .btn-new:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 30px rgba(124, 58, 237, 0.6);
            background: linear-gradient(135deg, #8b5cf6, #6366f1);
        }

        .card {
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 25px 50px rgba(0, 0, 0, 0.4);
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        thead tr {
            background: linear-gradient(90deg, rgba(124,58,237,0.3), rgba(79,70,229,0.3));
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }

        thead th {
            padding: 18px 20px;
            text-align: left;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: #a78bfa;
        }

        tbody tr {
            border-bottom: 1px solid rgba(255, 255, 255, 0.05);
            transition: background 0.2s ease, transform 0.15s ease;
            animation: fadeInRow 0.4s ease both;
        }

        tbody tr:last-child { border-bottom: none; }

        tbody tr:hover {
            background: rgba(167, 139, 250, 0.08);
        }

        @keyframes fadeInRow {
            from { opacity: 0; transform: translateX(-10px); }
            to   { opacity: 1; transform: translateX(0); }
        }

        tbody td {
            padding: 16px 20px;
            font-size: 0.9rem;
            color: #d1d5db;
            vertical-align: middle;
        }

        .id-badge {
            display: inline-block;
            background: rgba(96, 165, 250, 0.15);
            color: #60a5fa;
            border-radius: 6px;
            padding: 3px 10px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .name-cell {
            font-weight: 600;
            color: #f3f4f6;
        }

        .salary-cell {
            font-weight: 600;
            color: #34d399;
        }

        .position-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .pos-MANAGER    { background: rgba(245,158,11,0.2);  color: #fbbf24; }
        .pos-DEVELOPER  { background: rgba(59,130,246,0.2);  color: #60a5fa; }
        .pos-ANALYST    { background: rgba(139,92,246,0.2);  color: #a78bfa; }
        .pos-TESTER     { background: rgba(20,184,166,0.2);  color: #2dd4bf; }
        .pos-DEVOPS     { background: rgba(239,68,68,0.2);   color: #f87171; }
        .pos-DEFAULT    { background: rgba(107,114,128,0.2); color: #9ca3af; }

        .actions { display: flex; align-items: center; gap: 8px; }

        .action-btn {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 6px 14px;
            border-radius: 8px;
            font-size: 0.78rem;
            font-weight: 600;
            cursor: pointer;
            border: none;
            text-decoration: none;
            transition: all 0.2s ease;
        }

        .btn-view   { background: rgba(96,165,250,0.15);  color: #60a5fa; }
        .btn-edit   { background: rgba(167,139,250,0.15); color: #a78bfa; }
        .btn-delete { background: rgba(239,68,68,0.15);   color: #f87171; }

        .btn-view:hover   { background: rgba(96,165,250,0.3);  transform: translateY(-1px); }
        .btn-edit:hover   { background: rgba(167,139,250,0.3); transform: translateY(-1px); }
        .btn-delete:hover { background: rgba(239,68,68,0.3);   transform: translateY(-1px); }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #6b7280;
        }

        .empty-state .material-icons { font-size: 48px; margin-bottom: 12px; display: block; }

        .stats-bar {
            display: flex;
            gap: 20px;
            margin-bottom: 28px;
        }

        .stat-card {
            flex: 1;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(255,255,255,0.08);
            border-radius: 14px;
            padding: 18px 22px;
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .stat-icon {
            width: 44px; height: 44px;
            border-radius: 10px;
            display: flex; align-items: center; justify-content: center;
            font-size: 1.3rem;
        }

        .stat-icon.purple { background: rgba(124,58,237,0.2); }
        .stat-icon.blue   { background: rgba(59,130,246,0.2); }
        .stat-icon.green  { background: rgba(52,211,153,0.2); }

        .stat-label { font-size: 0.75rem; color: #6b7280; margin-bottom: 2px; }
        .stat-value { font-size: 1.4rem; font-weight: 700; color: #f3f4f6; }
    </style>
</head>
<body>

<div class="page-header">
    <div>
        <h1>👥 Employees</h1>
        <div class="subtitle">Manage your workforce</div>
    </div>
    <a href="${pageContext.request.contextPath}/mvc/employees/new" class="btn-new">
        <span class="material-icons" style="font-size:18px;">add</span> New Employee
    </a>
    <a href="${pageContext.request.contextPath}/mvc/employees/paged" class="btn-new" style="background:linear-gradient(135deg,#0f766e,#0e7490);box-shadow:0 4px 20px rgba(14,116,144,0.4);">
        <span class="material-icons" style="font-size:18px;">table_rows</span> Paged View
    </a>
</div>

<div class="stats-bar">
    <div class="stat-card">
        <div class="stat-icon purple">👤</div>
        <div>
            <div class="stat-label">Total Employees</div>
            <div class="stat-value">${employees.size()}</div>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon blue">🏢</div>
        <div>
            <div class="stat-label">Departments</div>
            <div class="stat-value">Active</div>
        </div>
    </div>
    <div class="stat-card">
        <div class="stat-icon green">💼</div>
        <div>
            <div class="stat-label">Status</div>
            <div class="stat-value" style="color:#34d399;">Online</div>
        </div>
    </div>
</div>

<div class="card">
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
        <c:choose>
            <c:when test="${empty employees}">
                <tr>
                    <td colspan="7">
                        <div class="empty-state">
                            <span class="material-icons">group_off</span>
                            No employees found. Add your first one!
                        </div>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="emp" items="${employees}">
                    <tr>
                        <td><span class="id-badge">#${emp.id}</span></td>
                        <td class="name-cell">${emp.name}</td>
                        <td>${emp.surname}</td>
                        <td style="color:#9ca3af;font-size:0.82rem;">${emp.personalId}</td>
                        <td class="salary-cell">$<c:out value="${emp.salary}"/></td>
                        <td>
                            <span class="position-badge pos-${emp.position}">${emp.position}</span>
                        </td>
                        <td>
                            <div class="actions">
                                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}" class="action-btn btn-view">👁 View</a>
                                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}/edit" class="action-btn btn-edit">✏️ Edit</a>
                                <form method="post"
                                      action="${pageContext.request.contextPath}/mvc/employees/${emp.id}/delete"
                                      style="display:inline;"
                                      onsubmit="return confirm('Delete ${emp.name} ${emp.surname}?');">
                                    <button type="submit" class="action-btn btn-delete">🗑 Delete</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

</body>
</html>

