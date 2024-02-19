# EAN Query Microservice Documentation

## Introduction

**The EAN Query Microservice is designed to provide product details based on the European Article Number (EAN) code associated with Supplier products. 
This service exposes a REST API that allows users to query product data, including basic product information, supplier details, and destination information.**

## Business Rules

EANs follow the format PPPPPPP+NNNNN+D, where:
* PPPPPPP refers to the supplier.
* NNNNN refers to the product code.
* D refers to the destination digit.
* The last digit of the EAN determines the destination of the product:
* 1-5: Supplier's stores in Spain.
* 6: Supplier's stores in Portugal.
* 8: Warehouses.
* 9: Supplier's Offices.
* 0: Beehives.
* EANs are composed of 13 numeric digits.
* The supplier can be identified by the Hacendado number (8437008) or another supplier's number.


The project is built using Spring Boot and utilizes H2 database for data storage. 
Swagger is integrated for API documentation, and Liquibase is used for pre-configuring the database. 
Additionally, integration and unit tests have been implemented to ensure code quality and functionality.

####  Welcome to a streamlined online book shopping journey with "Online Book Shop."

## Technologies and tools:
* Java Development Kit (JDK) Version: 17
* Apache Maven Version: 3.11.0
* Spring Boot Starter Web Version: 3.1.4
* Spring Security Test Version: 3.1.4
* Spring Boot Starter Data JPA Version: 3.1.4
* Spring Boot Starter Validation Version: 3.1.4
* Hibernate Validator Version: 8.0.1.Final
* H2 Database:
* Liquibase Version: 4.20.0
* Apache Tomcat Version: 9.0.78
* JSON Web Token (JWT)
* MapStruct Version: 1.5.5.Final
* Project Lombok Version: 4.20.0

## Features:

### Authentication Endpoints

**Login: POST /api/auth/login** <br>
Summary: Login an existing user<br>
Description: Allows existing users to log in to the system.<br>

**Registration: POST /api/auth/registration**<br>
Summary: Register a new user<br>
Description: Enables new users to register and create an account in the system.<br>


### EAN Number Management Endpoints

**Find by EAN: GET /api/eans/{ean}**<br>
Summary: Find the information by EAN code<br>
Description: Retrieves product information based on the provided EAN code.<br>


### Product Management Endpoints

**Find by ID: GET /api/products/{id}**<br>
Summary: Find the product by ID<br>
Description: Retrieves product details based on the specified ID.<br>

**Find by Code: GET /api/products/code/{code}**<br>
Summary: Find the product by code<br>
Description: Retrieves product details based on the specified product code.<br>

**Get all products: GET /api/products**<br>
Summary: Get all products<br>
Description: Retrieves a list of all available products.<br>

**Delete by ID: DELETE /api/products/{id}**<br>
Summary: Delete the product by ID<br>
Description: Deletes a product from the system based on the specified ID.<br>

**Update by ID: PATCH /api/products/{id}**<br>
Summary: Update the product by ID<br>
Description: Updates the product details based on the specified ID and parameters.<br>

**Create new product: POST /api/products**<br>
Summary: Create a new product<br>
Description: Adds a new product to the system.<br>


### Supplier Management Endpoints

**Find by ID: GET /api/suppliers/{id}**<br>
Summary: Find a supplier by ID<br>
Description: Retrieves supplier details based on the specified ID.<br>

**Find by Code: GET /api/suppliers/code/{code}**<br>
Summary: Find a supplier by code<br>
Description: Retrieves supplier details based on the specified supplier code.<br>

**Get all suppliers: GET /api/suppliers**<br>
Summary: Get all suppliers<br>
Description: Retrieves a list of all available suppliers.<br>

**Delete by ID: DELETE /api/suppliers/{id}**<br>
Summary: Delete the supplier by ID<br>
Description: Deletes a supplier from the system based on the specified ID.<br>

**Update by ID: PATCH /api/suppliers/{id}**<br>
Summary: Update the supplier by ID<br>
Description: Updates the supplier details based on the specified ID and parameters.<br>

**Create new supplier: POST /api/suppliers**<br>
Summary: Create a new supplier<br>
Description: Adds a new supplier to the system.<br>


## Work with Application

**1. For use the application you do not need to have MySQL DB installed, it works with H2 DB.<br>**
**2. Clone the repository.<br>**
**3. Use IDE to build and run an application.<br>**
**4. Use Postman and/or Swagger to work with the application.<br>**

**API runs on port 8080**<br>

**Swagger will be available by URL:**
http://localhost:8080/swagger-ui/index.html#/ <br>
It provides detailed information about the available endpoints, request parameters, and response formats.

**Please feel free to check all the functionality using Swagger!**<br>

**Also, you will be able to work with the application by using Postman.**<br>

### Pre-configured Users, Suppliers, and Products:<br>

**Users:**<br>
admin@gmail.com / 1234 <br>
user@gmail.com / 1234 <br>

**Suppliers:**<br>
Supplier with number: 8437008<br>
Supplier with number: 8480000<br>
Supplier with number: 8484884<br>

**Products:**<br>
Product with number: 45905<br>
Product with number: 16007<br>
Product with number: 12345<br>

**Destination model with ENAM:**<br>
SupplierSpain (Set.of(1,2,3,4,5))<br>
SupplierPortugal (Set.of(6))<br>
Warehouses (Set.of(8))<br>
SupplierOffices (Set.of(9))<br>
Colmenas (Set.of(0))<br>
<br>
<br>
**Here is a list of the Postman request collections you can work with:<br>**
[EAN.postman_collection.json](EAN.postman_collection.json)
<br>
