<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employees — Paged</title>
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

        /* ── Header ── */
        .page-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 28px;
        }
        .page-header h1 {
            font-size: 2.2rem;
            font-weight: 700;
            background: linear-gradient(90deg, #2dd4bf, #60a5fa);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            letter-spacing: -0.5px;
        }
        .page-header .subtitle { font-size: 0.9rem; color: #9ca3af; margin-top: 4px; }

        .btn-back {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            color: #9ca3af;
            text-decoration: none;
            font-size: 0.88rem;
            transition: color 0.2s;
        }
        .btn-back:hover { color: #a78bfa; }

        /* ── Filter card ── */
        .filter-card {
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 16px;
            padding: 24px 28px;
            margin-bottom: 24px;
        }
        .filter-card h2 {
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1.2px;
            color: #6b7280;
            margin-bottom: 18px;
        }
        .filter-row {
            display: flex;
            gap: 16px;
            align-items: flex-end;
            flex-wrap: wrap;
        }
        .filter-group {
            display: flex;
            flex-direction: column;
            gap: 6px;
            flex: 1;
            min-width: 160px;
        }
        .filter-group label {
            font-size: 0.78rem;
            font-weight: 600;
            color: #9ca3af;
            text-transform: uppercase;
            letter-spacing: 0.6px;
        }
        .filter-group input {
            background: rgba(255,255,255,0.07);
            border: 1px solid rgba(255,255,255,0.12);
            border-radius: 10px;
            padding: 10px 14px;
            font-size: 0.9rem;
            color: #f3f4f6;
            font-family: inherit;
            outline: none;
            transition: border-color 0.2s, background 0.2s;
        }
        .filter-group input::placeholder { color: #4b5563; }
        .filter-group input:focus {
            border-color: #60a5fa;
            background: rgba(96,165,250,0.08);
        }
        .filter-actions {
            display: flex;
            gap: 10px;
            align-items: center;
            padding-bottom: 1px;
        }
        .btn-search {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: linear-gradient(135deg, #0f766e, #0e7490);
            color: #fff;
            padding: 10px 22px;
            border-radius: 10px;
            border: none;
            font-size: 0.88rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.25s;
            font-family: inherit;
            box-shadow: 0 4px 14px rgba(14,116,144,0.35);
        }
        .btn-search:hover { transform: translateY(-1px); box-shadow: 0 6px 20px rgba(14,116,144,0.5); }
        .btn-clear {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            background: rgba(255,255,255,0.07);
            color: #9ca3af;
            padding: 10px 18px;
            border-radius: 10px;
            border: 1px solid rgba(255,255,255,0.1);
            font-size: 0.88rem;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: all 0.2s;
            font-family: inherit;
        }
        .btn-clear:hover { background: rgba(255,255,255,0.12); color: #e5e7eb; }

        /* active filter pills */
        .active-filters {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
            margin-top: 14px;
        }
        .filter-pill {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            background: rgba(96,165,250,0.12);
            border: 1px solid rgba(96,165,250,0.25);
            color: #60a5fa;
            border-radius: 20px;
            padding: 3px 12px;
            font-size: 0.78rem;
            font-weight: 500;
        }

        /* ── Results summary ── */
        .results-bar {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 14px;
            padding: 0 4px;
        }
        .results-bar .count { font-size: 0.85rem; color: #9ca3af; }
        .results-bar .count strong { color: #f3f4f6; }
        .size-select {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 0.82rem;
            color: #9ca3af;
        }
        .size-select select {
            background: rgba(255,255,255,0.07);
            border: 1px solid rgba(255,255,255,0.12);
            border-radius: 8px;
            padding: 5px 10px;
            color: #f3f4f6;
            font-size: 0.82rem;
            cursor: pointer;
            outline: none;
        }

        /* ── Table ── */
        .card {
            background: rgba(255,255,255,0.05);
            backdrop-filter: blur(20px);
            border: 1px solid rgba(255,255,255,0.1);
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 25px 50px rgba(0,0,0,0.4);
        }
        table { width: 100%; border-collapse: collapse; }
        thead tr {
            background: linear-gradient(90deg, rgba(14,116,144,0.3), rgba(15,118,110,0.3));
            border-bottom: 1px solid rgba(255,255,255,0.1);
        }
        thead th {
            padding: 18px 20px;
            text-align: left;
            font-size: 0.75rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
            color: #2dd4bf;
        }
        tbody tr {
            border-bottom: 1px solid rgba(255,255,255,0.05);
            transition: background 0.2s;
            animation: fadeInRow 0.3s ease both;
        }
        tbody tr:last-child { border-bottom: none; }
        tbody tr:hover { background: rgba(45,212,191,0.05); }
        @keyframes fadeInRow {
            from { opacity: 0; transform: translateX(-8px); }
            to   { opacity: 1; transform: translateX(0); }
        }
        tbody td { padding: 15px 20px; font-size: 0.88rem; color: #d1d5db; vertical-align: middle; }

        .id-badge {
            display: inline-block;
            background: rgba(96,165,250,0.15);
            color: #60a5fa;
            border-radius: 6px;
            padding: 3px 10px;
            font-size: 0.8rem;
            font-weight: 600;
        }
        .name-cell { font-weight: 600; color: #f3f4f6; }
        .salary-cell { font-weight: 600; color: #34d399; }

        .position-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 0.72rem;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }
        .pos-MANAGER   { background: rgba(245,158,11,0.2);  color: #fbbf24; }
        .pos-DEVELOPER { background: rgba(59,130,246,0.2);  color: #60a5fa; }
        .pos-ANALYST   { background: rgba(139,92,246,0.2);  color: #a78bfa; }
        .pos-TESTER    { background: rgba(20,184,166,0.2);  color: #2dd4bf; }
        .pos-DEVOPS    { background: rgba(239,68,68,0.2);   color: #f87171; }

        .actions { display: flex; align-items: center; gap: 8px; }
        .action-btn {
            display: inline-flex;
            align-items: center;
            gap: 4px;
            padding: 5px 12px;
            border-radius: 8px;
            font-size: 0.76rem;
            font-weight: 600;
            cursor: pointer;
            border: none;
            text-decoration: none;
            transition: all 0.2s;
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

        /* ── Pagination ── */
        .pagination-bar {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
            margin-top: 28px;
            flex-wrap: wrap;
        }
        .page-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 38px;
            height: 38px;
            padding: 0 12px;
            border-radius: 10px;
            font-size: 0.85rem;
            font-weight: 600;
            text-decoration: none;
            border: 1px solid rgba(255,255,255,0.1);
            background: rgba(255,255,255,0.05);
            color: #9ca3af;
            transition: all 0.2s;
            cursor: pointer;
        }
        .page-btn:hover:not(.disabled):not(.active) {
            background: rgba(45,212,191,0.12);
            border-color: #2dd4bf;
            color: #2dd4bf;
        }
        .page-btn.active {
            background: linear-gradient(135deg, #0f766e, #0e7490);
            border-color: transparent;
            color: #fff;
            box-shadow: 0 4px 14px rgba(14,116,144,0.4);
        }
        .page-btn.disabled {
            opacity: 0.3;
            cursor: not-allowed;
            pointer-events: none;
        }
        .page-info {
            text-align: center;
            margin-top: 14px;
            font-size: 0.8rem;
            color: #4b5563;
        }
    </style>
</head>
<body>

<%-- ── Header ── --%>
<div class="page-header">
    <div>
        <h1>📋 Employees — Paged</h1>
        <div class="subtitle">Filtered &amp; paginated view</div>
    </div>
    <a href="${pageContext.request.contextPath}/mvc/employees" class="btn-back">← Back to full list</a>
</div>

<%-- ══════════════════════════════════════════════
     FILTER FORM
     Uses GET so filters appear in the URL — bookmarkable, shareable.
     Each input stays populated with the current filter value from the model.
     ══════════════════════════════════════════════ --%>
<div class="filter-card">
    <h2>🔍 Filters</h2>
    <form method="get" action="${pageContext.request.contextPath}/mvc/employees/paged">

        <%-- preserve page size when re-filtering --%>
        <input type="hidden" name="size"  value="${page.size}"/>
        <input type="hidden" name="page"  value="0"/><%-- reset to first page on new search --%>

        <div class="filter-row">
            <div class="filter-group">
                <label for="name">Name / Surname</label>
                <input type="text" id="name" name="name"
                       value="${name}"
                       placeholder="e.g. John"/>
            </div>

            <div class="filter-group">
                <label for="salaryMin">Min Salary</label>
                <input type="number" id="salaryMin" name="salaryMin"
                       value="${salaryMin}"
                       placeholder="e.g. 3000" min="0" step="100"/>
            </div>

            <div class="filter-group">
                <label for="salaryMax">Max Salary</label>
                <input type="number" id="salaryMax" name="salaryMax"
                       value="${salaryMax}"
                       placeholder="e.g. 10000" min="0" step="100"/>
            </div>

            <div class="filter-actions">
                <button type="submit" class="btn-search">
                    <span class="material-icons" style="font-size:16px;">search</span> Search
                </button>
                <a href="${pageContext.request.contextPath}/mvc/employees/paged" class="btn-clear">
                    <span class="material-icons" style="font-size:15px;">close</span> Clear
                </a>
            </div>
        </div>

        <%-- Active filter pills — visual feedback showing what is active --%>
        <c:if test="${not empty name or not empty salaryMin or not empty salaryMax}">
            <div class="active-filters">
                <c:if test="${not empty name}">
                    <span class="filter-pill">👤 name: <strong>${name}</strong></span>
                </c:if>
                <c:if test="${not empty salaryMin}">
                    <span class="filter-pill">💰 min: <strong>$${salaryMin}</strong></span>
                </c:if>
                <c:if test="${not empty salaryMax}">
                    <span class="filter-pill">💰 max: <strong>$${salaryMax}</strong></span>
                </c:if>
            </div>
        </c:if>

    </form>
</div>

<%-- ── Results summary bar ── --%>
<div class="results-bar">
    <div class="count">
        Showing
        <strong>${page.number * page.size + 1}–${page.number * page.size + page.numberOfElements}</strong>
        of <strong>${page.totalElements}</strong> employees
        (page <strong>${page.number + 1}</strong> of <strong>${page.totalPages}</strong>)
    </div>

    <%-- Page size switcher — keeps current filters, resets to page 0 --%>
    <div class="size-select">
        Rows per page:
        <select onchange="changeSize(this.value)">
            <option value="5"  ${page.size ==  5 ? 'selected' : ''}>5</option>
            <option value="10" ${page.size == 10 ? 'selected' : ''}>10</option>
            <option value="20" ${page.size == 20 ? 'selected' : ''}>20</option>
            <option value="50" ${page.size == 50 ? 'selected' : ''}>50</option>
        </select>
    </div>
</div>

<%-- ══════════════════════════════════════════════
     EMPLOYEE TABLE
     page.content — the List<Employee> for the current page
     ══════════════════════════════════════════════ --%>
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
            <c:when test="${empty page.content}">
                <tr>
                    <td colspan="7">
                        <div class="empty-state">
                            <span class="material-icons">manage_search</span>
                            No employees match your filters.
                        </div>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="emp" items="${page.content}">
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
                                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}"       class="action-btn btn-view">👁 View</a>
                                <a href="${pageContext.request.contextPath}/mvc/employees/${emp.id}/edit"  class="action-btn btn-edit">✏️ Edit</a>
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

<%-- ══════════════════════════════════════════════
     PAGINATION CONTROLS
     filterParams model attribute holds "&name=...&salaryMin=...&salaryMax=..."
     so every page link preserves the active filters automatically.

     Page numbers are 0-based in Spring Page, but shown as 1-based to users.
     Window: show 2 pages before and after the current page (max 5 visible at once).
     ══════════════════════════════════════════════ --%>
<c:if test="${page.totalPages > 1}">
    <c:set var="cur"       value="${page.number}"/>
    <c:set var="total"     value="${page.totalPages}"/>
    <c:set var="size"      value="${page.size}"/>
    <c:set var="baseUrl"   value="${pageContext.request.contextPath}/mvc/employees/paged"/>

    <%-- Window calculation: 2 pages around current, clamped to [0, totalPages-1] --%>
    <c:set var="winStart"  value="${cur > 2 ? cur - 2 : 0}"/>
    <c:set var="winEnd"    value="${cur + 2 < total - 1 ? cur + 2 : total - 1}"/>

    <div class="pagination-bar">

        <%-- ← Prev --%>
        <a class="page-btn ${page.first ? 'disabled' : ''}"
           href="${baseUrl}?page=${cur - 1}&size=${size}${filterParams}">
            ‹ Prev
        </a>

        <%-- First page shortcut if window doesn't include it --%>
        <c:if test="${winStart > 0}">
            <a class="page-btn" href="${baseUrl}?page=0&size=${size}${filterParams}">1</a>
            <c:if test="${winStart > 1}">
                <span class="page-btn disabled" style="border:none;background:none;color:#4b5563;">…</span>
            </c:if>
        </c:if>

        <%-- Page number window --%>
        <c:forEach var="i" begin="${winStart}" end="${winEnd}">
            <a class="page-btn ${i == cur ? 'active' : ''}"
               href="${baseUrl}?page=${i}&size=${size}${filterParams}">
                ${i + 1}
            </a>
        </c:forEach>

        <%-- Last page shortcut if window doesn't include it --%>
        <c:if test="${winEnd < total - 1}">
            <c:if test="${winEnd < total - 2}">
                <span class="page-btn disabled" style="border:none;background:none;color:#4b5563;">…</span>
            </c:if>
            <a class="page-btn" href="${baseUrl}?page=${total - 1}&size=${size}${filterParams}">${total}</a>
        </c:if>

        <%-- Next → --%>
        <a class="page-btn ${page.last ? 'disabled' : ''}"
           href="${baseUrl}?page=${cur + 1}&size=${size}${filterParams}">
            Next ›
        </a>

    </div>

    <div class="page-info">
        Page ${cur + 1} of ${total} &nbsp;·&nbsp; ${page.totalElements} total results
    </div>
</c:if>

<script>
    /**
     * Rows-per-page switcher.
     * Reads the current URL search params, updates "size", resets to page 0,
     * and navigates — preserving all active filters.
     */
    function changeSize(newSize) {
        const params = new URLSearchParams(window.location.search);
        params.set('size', newSize);
        params.set('page', '0');
        window.location.href = '${pageContext.request.contextPath}/mvc/employees/paged?' + params.toString();
    }
</script>

</body>
</html>

