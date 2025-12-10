For Better view please read it in code tab
User Management System (RBAC)

A Spring Boot–based RBAC application that manages user registration, login, and role assignments, 
with JWT security and Docker-ready deployment including MySQL and RabbitMQ.


Spring Boot · JWT · RBAC · MySQL · RabbitMQ · Docker

This project is a Role-Based Access Control (RBAC) system implementing:

User Registration
Login with JWT Authentication
Role Assignment
Admin-Only Endpoints
Event Publishing via RabbitMQ
Docker + Docker Compose deployment
Layered Architecture (Controller → Service → Repository)

Project Structure
src/main/java/com.example.rbac.user_management_rbac
├── controller
├── dto
├── service
├── repository
├── security
├── events
└── model

LOCAL SETUP (without Docker)
1 Install Dependencies
 Java 17
 Maven
 MySQL running locally

Create user_management database:
CREATE DATABASE user_management;

Update application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/user_management
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

2️ Run Application Locally
Using Maven:
./mvnw spring-boot:run

DOCKER SETUP (recommended)
This application comes with a docker-compose.yml that runs:

✔ Spring Boot App
✔ MySQL
✔ RabbitMQ

1️ Build Application JAR
mvn clean package -DskipTests

2️ Start All Services
docker-compose up --build

This launches three containers:

Service	Container Name	Ports
MySQL	rbac-mysql	3307 → 3306
RabbitMQ	rbac-rabbitmq	5672, 15672
Spring Boot App	rbac-app	8080

Database Schema Setup
Tables are created automatically via JPA.
Main tables:

USERS
id | username | email | password | enabled

ROLES
id | name

USERS_ROLES (Mapping Table)
user_id | role_id

To assign ADMIN to first user:
INSERT INTO roles(name) VALUES ('ROLE_ADMIN');
INSERT INTO users_roles(user_id, role_id) VALUES (1,1);

API Endpoints

Register User
POST /api/users/register
{
"username": "john",
"email": "john@gmail.com",
"password": "password123"
}

Login
POST /api/users/login
{
"email": "john@gmail.com",
"password": "password123"
}

Get Profile (Authenticated)
GET /api/users/me

Headers:Authorization: Bearer <token>

Create Role (Admin Only)
Use Admin token only

POST /api/roles

{ "name": "ROLE_MANAGER" }

Assign Role to User (Admin Only)

POST /api/users/{id}/roles

{ "roles": ["ROLE_ADMIN"] }

Admin Stats Endpoint

GET /api/admin/stats

Returns mock stats:

{
"totalUsers": 10,
"activeAdmins": 2,
"timestamp": "2025-12-10T12:00:00"
}

RabbitMQ Events

RabbitMQ dashboard:
http://localhost:15672
User: guest
Pass: guest
