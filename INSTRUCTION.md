# Spring Workshop — Run Instructions

## Prerequisites

| Tool | Minimum version |
|------|----------------|
| Java JDK | 21 |
| Maven | 3.8+ (or use the Maven wrapper if present) |

---

## 1. Clone / open the project

```bash
git clone <repo-url>
cd Spring-workshop
```

---

## 2. Build the project

```bash
mvn clean install
```

This compiles the sources, runs tests, and packages the application into `target/spring-workshop-1.0.0.jar`.

To skip tests during build:

```bash
mvn clean install -DskipTests
```

---

## 3. Run the application

### Option A — Maven Spring Boot plugin (recommended for development)

```bash
mvn spring-boot:run
```

### Option B — Run the JAR directly

```bash
java -jar target/spring-workshop-1.0.0.jar
```

The application starts on **http://localhost:8080** by default.

---

## 4. Database

The app uses an **in-memory H2 database** — no setup required.  
On every startup the database is seeded automatically from `src/main/resources/data.sql` with:

- 2 companies (`Reversed`, `Tera`)
- 2 employees (`Bob` — CEO, `William` — Accountant)
- 2 addresses

> ⚠️ All data is lost when the application stops (in-memory database).

### H2 Console

Access the H2 web console at **http://localhost:8080/h2-console**

| Field | Value |
|-------|-------|
| JDBC URL | `jdbc:h2:mem:jitdb` |
| Username | `local` |
| Password | `local` |

---

## 5. Available endpoints

### REST API (JSON)

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/employees` | List all employees |
| GET | `/employees/{id}` | Get employee by ID |
| POST | `/employees` | Create new employee |
| PUT | `/employees/{id}` | Update employee |
| DELETE | `/employees/{id}` | Delete employee |
| GET | `/companies` | List all companies |
| GET | `/currencies` | Currency info (NBP) |
| GET | `/horses` | List horses |
| GET | `/config` | Config values |
| GET | `/scope` | Bean scope demo |

### MVC Views (JSP)

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/mvc/employees` | Employee list page |
| GET | `/mvc/employees/{id}` | Employee detail page |
| GET | `/mvc/employees/new` | New employee form |
| POST | `/mvc/employees` | Submit new employee |
| GET | `/mvc/employees/{id}/edit` | Edit employee form |
| POST | `/mvc/employees/{id}` | Submit employee update |
| POST | `/mvc/employees/{id}/delete` | Delete employee |

---

## 6. Example REST requests

### Create an employee

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "surname": "Smith",
    "personalId": "ABC123",
    "salary": 5000
  }'
```

### Update an employee

```bash
curl -X PUT http://localhost:8080/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob Updated",
    "salary": 12000,
    "companyId": 1,
    "position": "MANAGER"
  }'
```

### Delete an employee

```bash
curl -X DELETE http://localhost:8080/employees/1
```

---

## 7. Running tests

```bash
mvn test
```

---

## 8. Project structure overview

```
src/
├── main/
│   ├── java/
│   │   ├── mvc/                        # Spring MVC JSP controller
│   │   │   └── EmployeeMvcController.java
│   │   └── team/jit/
│   │       ├── Application.java        # Entry point
│   │       ├── controller/             # REST controllers
│   │       ├── service/                # Business logic
│   │       ├── repository/             # Spring Data JPA repos
│   │       ├── entity/                 # JPA entities
│   │       ├── dto/                    # Form / request DTOs
│   │       └── config/                 # App configuration
│   ├── resources/
│   │   ├── application.properties      # Main config
│   │   └── data.sql                    # Seed data
│   └── webapp/
│       └── WEB-INF/views/employees/    # JSP templates
│           ├── list.jsp
│           ├── detail.jsp
│           ├── form.jsp
│           └── edit.jsp
└── test/
    └── java/team/jit/controller/       # Controller tests
```

