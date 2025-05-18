# Bitespeed Backend Task – Identity Reconciliation

Welcome to the Bitespeed Backend Challenge! This project focuses on solving the problem of **identity reconciliation** – linking users across multiple records using phone numbers and emails.

### Overview
The Identity Reconciliation service helps track users even when they use different identifiers (email or phone number). This allows businesses to maintain a unified view of customer interactions and provide consistent service.

### Objective
Design an API endpoint `/identify` that accepts a JSON payload containing `email` and/or `phoneNumber`, and returns a consolidated contact response linking all associated identities.

## Identity-Reconciliation Backend Live link
https://identity-reconciliation-u1i8.onrender.com/api/v1/swagger-ui/index.html#/

### API Specification
### Endpoint
POST /identify

### Request Body
```json
{
  "email": "example@example.com",
  "phoneNumber": "1234567890"
}
```
 ### Expected Response Format

```bash

{
  "contact": {
    "primaryContatctId": 1,
    "emails": ["example@example.com", "alt@example.com"],
    "phoneNumbers": ["1234567890"],
    "secondaryContactIds": [2, 3]
  }
}
```
### Business Logic
* If both email and phoneNumber are new, create a new contact with linkPrecedence = "primary".
* If email or phoneNumber matches an existing contact:
  * If both are already linked: return existing contact group.
  * If partially linked, create a new secondary contact linking to the oldest primary.
* If two existing primary contacts need to be merged:
  * The oldest contact would be remained as primary and the other contacts would be made secondary.

### Data Model
```
{
  "id": "number",
  "phoneNumber": "string (nullable)",
  "email": "string (nullable)",
  "linkedId": "number (nullable) (points to another Contact)",
  "linkPrecedence": "'primary' | 'secondary'",
  "createdAt": "timestamp",
  "updatedAt": "timestamp",
  "deletedAt": "timestamp (nullable)"
}
```
### Tech Stack
* Language: Java
* Framework: Spring
* Database: MySQL
* Documentation: SwaggerUI

### Getting Started

1. **Clone the repository**:
```bash
git clone https://github.com/Saud-12/Identity-Reconciliation-.git
cd Identity-Reconciliation
```

2. **Configure MySQL**:
    Set up a MySQL database and update the application.properties file with your database connection details.
   ```bash
    spring.datasource.url=jdbc:mysql://localhost:3306/your_database
    spring.datasource.username=your_username
    spring.datasource.password=your_password

3. **Build and run the application**:
   ```bash
   ./gradlew build
    ./gradlew bootRun
   
4.  **Access the API at http://localhost:8080/api/v1/contacts/identify**
