# Bank Management System

A **console-based banking application** developed in **Java**, simulating real-world banking operations such as client management, account handling, card assignment, and transaction tracking.  
The project demonstrates principles of **object-oriented programming (OOP)**, **JDBC persistence**, and **service-based architecture**.

---

## Features

### Authentication & Sessions
- Client login with active session tracking  
- Password management and validation  
- Logout and session reset

### Client Management
- Add, view, and list clients  
- Each client has personal details (name, CNP, address, email, phone)

### Account & Card Management
- Create new bank accounts for clients  
- Assign debit or credit cards to accounts  
- Remove cards from client accounts  

### Transactions
- Create and view transactions between accounts  
- Store transaction history per client  
- Display transaction details by account or client

### Persistence Layer (JDBC + PostgreSQL)
- Full **CRUD** operations for clients, accounts, cards, and transactions  
- Data persistence using **JDBC** and **PostgreSQL**  
- Structured database interaction through `CRUD` and `JDBC` classes

### Audit Logging
- Every action (add client, transaction, etc.) is logged to a CSV file  
- Includes action name and timestamp  
- Implemented through a dedicated **Audit Service**

---

## Technologies Used
- **Java 17+**
- **PostgreSQL**
- **JDBC**
- **CSV File Logging**
- **OOP principles** (Encapsulation, Inheritance, Polymorphism)
- **Singleton Pattern** for service classes

