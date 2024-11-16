# Library Management System

## Overview

This Library Management System is an API built using Spring Boot. It allows librarians to manage books, patrons, and borrowing records efficiently. The system provides RESTful endpoints for performing CRUD operations on books and patrons, as well as managing borrowing transactions.

## Features

- Manage books (add, update, delete, retrieve)
- Manage patrons (add, update, delete, retrieve)
- Borrow and return books
- Input validation and error handling
- JWT-based authorization
- Transaction management
  
 **Added Features**
- <b>Available Copies</b>: Each book has an availableCopies attribute to track how many copies are available for borrowing.
- <b>Patron Borrowing Limit</b>: Each patron has a numOfBorrowedBooks attribute with a maximum borrow limit set to MAX_BORROW_LIMIT = 5, ensuring that patrons cannot borrow more than 5 books at a time.

- **Database Population**
  - initialized the database with 5 rows of each book and patron tables.

- **Endpoints Testing** 
  - Implemented around 14 Test Cases for the 12 Apis

## Technologies Used

- Java
- Spring Boot
- H2 Database
- SpringBootTest, Mockito (for testing)
- Spring Cache
- Aspect-Oriented Programming (AOP)
- Maven (for dependency management)

## API Endpoints

### Book Management

- **GET** `/api/books` - Retrieve a list of all books **Paginated**.
- **GET** `/api/books/{id}` - Retrieve details of a specific book by ID.
- **POST** `/api/books` - Add a new book to the library.
- **PUT** `/api/books/{id}` - Update an existing book's information.
- **DELETE** `/api/books/{id}` - Remove a book from the library.

### Patron Management

- **GET** `/api/patrons` - Retrieve a list of all patrons **Paginated**.
- **GET** `/api/patrons/{id}` - Retrieve details of a specific patron by ID.
- **POST** `/api/patrons` - Add a new patron to the system.
- **PUT** `/api/patrons/{id}` - Update an existing patron's information.
- **DELETE** `/api/patrons/{id}` - Remove a patron from the system.

### Borrowing Management

- **POST** `/api/borrow/{bookId}/patron/{patronId}` - Allow a patron to borrow a book.
- **PUT** `/api/return/{bookId}/patron/{patronId}` - Record the return of a borrowed book by a patron.

### Authentication Management

- **POST** `/api/auth/register` - Register an admin.
- **POST** `/api/auth/login` - Login the admin.


## Setup Instructions

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/EmadHussien/Library-Management-System.git
   cd library-management-system
   cd LMS
   ```

2. **Install Dependencies:**

   For Maven:
   ```bash
   mvn clean install
   ```

   For Gradle:
   ```bash
   ./gradlew build
   ```

3. **Run the Application:**

   For Maven:
   ```bash
   mvn spring-boot:run
   ```

   For Gradle:
   ```bash
   ./gradlew bootRun
   ```

4. **Access the API:**

   The API will be available at `http://localhost:8080/api`.

## Testing

To run the tests, use:

```bash
mvn test
```
or
```bash
./gradlew test
```

## Documentation

For detailed API documentation, refer to https://documenter.getpostman.com/view/21228226/2sAY4rDjQ7.


## Contact

For inquiries, please reach out to:

- **Name:** Emad Hussien
- **Email:** emadhussien.fcis@gmail.com
- **GitHub:** https://github.com/EmadHussien
- **LinkedIn** https://www.linkedin.com/in/emadhussien98

---
