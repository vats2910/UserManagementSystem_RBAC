# **User Management System (RBAC)**  
### **Spring Boot · JWT · RBAC · MySQL · RabbitMQ · Docker**

A production-ready Role-Based Access Control (RBAC) system built using Spring Boot, featuring secure JWT authentication, user management, role assignments, admin-only access, event publishing via RabbitMQ, and full Docker Compose deployment with MySQL and RabbitMQ.

---

## 🚀 **Features**

- **User Registration**
- **JWT-based Login & Authentication**
- **Role Assignment (RBAC)**
- **Admin-Only Protected Endpoints**
- **RabbitMQ Event Publishing**
- **Docker + Docker Compose Deployment**
- **Layered Architecture (Controller → Service → Repository)**

---

## 📂 **Project Structure**

```text
src/main/java/com.example.rbac.user_management_rbac
 ├── controller
 ├── dto
 ├── service
 ├── repository
 ├── security
 ├── events
 └── model
```

## 🖥️ Local Setup (Without Docker)

### 1️⃣ Install Dependencies

- Java 17  
- Maven  
- MySQL running locally  

Create the `user_management` database:

```sql
CREATE DATABASE user_management;
```
### Update `application.properties`

Add the following configuration inside:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_management
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
## ▶️ Run Application Locally

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```
## 🐳 DOCKER SETUP (Recommended)

This application comes with a `docker-compose.yml` that runs:

✔ **Spring Boot App**  
✔ **MySQL**  
✔ **RabbitMQ**

### 1️⃣ Build Application JAR

```bash
mvn clean package -DskipTests
```
### 2️⃣ Start All Services

Run the following command:

    docker-compose up --build

This launches three containers:

| Service          | Container Name   | Ports        |
|------------------|------------------|--------------|
| MySQL            | rbac-mysql       | 3307 → 3306  |
| RabbitMQ         | rbac-rabbitmq    | 5672, 15672  |
| Spring Boot App  | rbac-app         | 8080         |

## 🗄️ Database Schema Setup

Tables are created automatically via **JPA/Hibernate**.

### Main Tables

#### `USERS`

Fields:

- `id`
- `username`
- `email`
- `password`
- `enabled`

Example structure:

```text
USERS
-----
id | username | email           | password      | enabled

ROLES
-----
id | name

USERS_ROLES (Mapping Table)
-----
user_id | role_id
```
### To assign ADMIN role to the first user:

```sql
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
INSERT INTO users_roles(user_id, role_id) VALUES (1, 1);
```
## 📡 API Endpoints

---

### 🧾 Register User

**Endpoint:**
POST /api/users/register

**Request Body:**

```json
{
  "username": "john",
  "email": "john@gmail.com",
  "password": "password123"
}
```
### 🔐 Login

**Endpoint:**
POST /api/users/login


**Request Body:**

```json
{
  "email": "john@gmail.com",
  "password": "password123"
}
```
### 👤 Get Profile (Authenticated)

**Endpoint:**
GET /api/users/me

Returns the user's id, username, email, and assigned roles

### 🛠 Create Role (Admin Only)

Use **Admin token only**

**Endpoint:**

POST /api/roles


**Request Body:**

```json
{
  "name": "ROLE_MANAGER"
}
```
### 🎭 Assign Role to User (Admin Only)

**Endpoint:**
POST  /api/users/{id}/roles


**Request Body:**

```json
{
  "roles": ["ROLE_ADMIN"]
}
```
### 📊 Admin Stats Endpoint

**Endpoint:**

GET /api/admin/stats


**Example Response:**

```json
{
  "totalUsers": 10,
  "activeAdmins": 2,
  "timestamp": "2025-12-10T12:00:00"
}
```
### 📨 RabbitMQ Events

RabbitMQ Dashboard:

http://localhost:15672

**Default Credentials:**

User: guest
Pass: guest









