🔒 PRODUCT INVENTORY REST API

A secure Spring Boot REST API for managing product inventory with Basic Authentication, role-based access control, and comprehensive product management features.

 🌟 Features

- 🔐 Authentication & Authorization**
  - Basic Authentication with Spring Security
  - Role-based access (ADMIN/USER)
  - Password encryption with BCrypt

- 📦 Product Management**
  - Full CRUD operations for products (ADMIN only)
  - Fetch multiple products by IDs (USER access)
  - Secure endpoints with `@PreAuthorize`

 🛠️ Tech Stack

- Backend: Spring Boot, Spring Security, JPA/Hibernate
- Database: MySQL
- Authentication: Basic Auth with BCrypt
- Testing: JUnit 5, Mockito


---

📡 API Endpoints

👥 Auth Endpoints
`POST /api/newUser`  
Register a new user (auto-assigns USER role)

📦 Product Endpoints (ADMIN Only)
`POST /api/product/add`  
Add new product  

`PUT /api/product/update/{id}`  
Update existing product  

`DELETE /api/product/delete/{id}`  
Delete product  

🔍 Product Endpoints (USER Access)
`GET /api/product/get`  
Get all products  

`GET /api/product/getById?ids=1,2,3`  
Get products by multiple IDs  

