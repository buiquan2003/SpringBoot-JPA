Spring Boot JPA REST API
📌 Tổng quan
Dự án Spring Boot JPA REST API là một ứng dụng backend được xây dựng nhằm minh họa cách phát triển RESTful API với Spring Boot và Spring Data JPA, kết nối cơ sở dữ liệu và triển khai bằng Docker.

Dự án phù hợp cho:

Sinh viên học Java Backend
Ứng tuyển vị trí Java Backend Intern / Fresher
Làm portfolio cá nhân
🎯 Mục tiêu dự án
Hiểu và áp dụng mô hình Layered Architecture
Xây dựng RESTful API chuẩn
Thực hành Spring Data JPA (CRUD, mapping entity)
Quản lý dependency bằng Gradle
Triển khai ứng dụng bằng Docker & Docker Compose
🛠 Công nghệ sử dụng
Java 17
Spring Boot
Spring Data JPA (Hibernate)
RESTful API
Gradle
Docker & Docker Compose
MySQL / PostgreSQL (tùy cấu hình)
📂 Kiến trúc & cấu trúc thư mục
Dự án được tổ chức theo mô hình Controller – Service – Repository: src/main/java/ ├── controller/ // Xử lý request/response ├── service/ // Business logic ├── repository/ // Spring Data JPA Repository ├── entity/ // JPA Entity (mapping DB) ├── dto/ // Data Transfer Object (nếu có) ├── exception/ // Xử lý exception (nếu có) └── config/ // Cấu hình (Swagger, etc.)

⚙️ Cấu hình ứng dụng
🔹 application.properties
properties spring.datasource.url=jdbc:mysql://localhost:3306/dbshop spring.datasource.username=root spring.datasource.password=quanbui2003 spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect spring.jpa.show-sql=true

spring.jpa.hibernate.ddl-auto=none spring.datasource.driver-class-name =com.mysql.cj.jdbc.Driver spring.datasource.hikari.minimum-idle=5 spring.datasource.hikari.maximum-pool-size=10 spring.datasource.hikari.idle-timeout=600000 spring.datasource.hikari.connection-timeout=30000 spring.datasource.hikari.validation-timeout=5000 spring.datasource.hikari.max-lifetime=1800000 spring.datasource.hikari.connection-test-query=select 1

Chạy bằng Gradle
./gradlew bootRun

Ứng dụng chạy tại:
http://localhost:8080

Chạy bằng Docker Compose
docker-compose up --build

API Documentation (Swagger / OpenAPI)
Chạy project: http://localhost:8080/swagger-ui.html Swagger cho phép: Xem danh sách API Test API trực tiếp trên trình duyệt Xem request/response mẫu

Chức năng chính:
CRUD dữ liệu với Spring Data JPA Xây dựng RESTful API chuẩn Kết nối và thao tác cơ sở dữ liệu Quản lý dependency bằng Gradle Triển khai nhanh bằng Docker

Hướng phát triển trong tương lai
Áp dụng DTO & Validation (@NotNull, @Size, ...) Xử lý Exception toàn cục với @ControllerAdvice Phân trang & sắp xếp dữ liệu Thêm Authentication & Authorization (JWT) Viết Unit Test & Integration Test Áp dụng CI/CD

Ghi chú
Dự án được xây dựng với mục đích học tập và thực hành Spring Boot JPA. Có thể mở rộng để phát triển thành các hệ thống quản lý hoặc ứng dụng backend thực tế.

Tác giả
Bùi Quân GitHub: https://github.com/buiquan2003
