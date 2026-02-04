# Tour Agency API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring](https://img.shields.io/badge/Spring%20Framework-5.3-green)
![Hibernate](https://img.shields.io/badge/Hibernate-6-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-blue)

## Overview

**Tour Agency API** is a robust RESTful backend application designed to manage the operations of a travel agency. It provides functionality for tour management, user booking systems, ratings, and favorites.

**Key Architectural Highlight:**
This project is intentionally built using **Pure Spring Framework (Spring MVC)** without Spring Boot. This approach demonstrates a deep understanding of the Spring IoC container, explicit configuration (Java-based), Servlet context lifecycle, and ORM integration.

##  Key Features

* **Authentication & Authorization:**
    * Secure login and registration using **JWT (JSON Web Tokens)**.
    * Role-Based Access Control (RBAC): `USER` and `AGENCY` roles.
    * Token refresh mechanism for enhanced security.
* **Tour Management:**
    * Agencies can create, update, and delete tours.
    * Discount system and availability tracking.
* **Booking System:**
    * Users can book available tours.
    * Automatic seat counting and validation to prevent overbooking.
* **Social Features:**
    * **Ratings:** Users can rate tours, affecting the average score dynamically.
    * **Favorites:** Users can save tours to their wishlist.
* **Architecture:**
    * Layered Architecture (Controller -> Service -> Repository -> Database).
    * Global Exception Handling with standardized JSON error responses.

## Tech Stack

* **Language:** Java 17
* **Core Framework:** Spring MVC 5.3.39 (Context, Web, Security, AOP, ORM)
* **Database:** PostgreSQL
* **ORM:** Hibernate 6.4.4 (Jakarta Persistence API)
* **Connection Pool:** HikariCP
* **Security:** Spring Security 5, JJWT (Java JWT)
* **Utilities:** Lombok, MapStruct, Jackson
* **Build Tool:** Maven
* **Deployment:** Apache Tomcat (WAR packaging)

## Getting Started

Since this is a non-Boot application, it requires an external Servlet Container (like Tomcat) to run.

### Prerequisites
* Java JDK 17+
* Maven 3+
* PostgreSQL
* Apache Tomcat 9 or 10

### 1. Database Setup
Create a PostgreSQL database named `tour_agency_db`:
sql
CREATE DATABASE tour_agency_db;
2. Configuration
Open src/main/resources/application.properties and update your database credentials:

Properties
db.url=jdbc:postgresql://localhost:5432/tour_agency_db
db.username=your_postgres_user
db.password=your_postgres_password
jwt.secret=your_secure_secret_key
3. Build the Project
Navigate to the project root and build the WAR file:
mvn clean package
This will generate tour-agency-1.0.0.war in the target/ directory.

4. Run on Tomcat
Copy the generated .war file to the webapps/ folder of your Tomcat installation.

Start the Tomcat server (bin/startup.sh or bin/startup.bat).

The API will be accessible at: http://localhost:8080/tour-agency-1.0.0/
Method,Endpoint,Description,Access
Auth,/auth/login,Login & get tokens,Public
Auth,/auth/refresh,Refresh access token,Public
Tour,/agencies/{id}/tours,Create a new tour,Agency
Tour,/tours,Get all tours,Public
Booking,/bookings/{tourId},Book a tour,User
Rating,/ratings,Rate a tour,User
