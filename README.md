# Sports Event API

## Overview

The **Sports Event API** is a RESTful web service built with **Spring Boot** that allows users to manage sports events. It supports CRUD operations and enforces status change restrictions for events.

## Features

- Create a new sport event
- Retrieve a list of events with optional filters by status and sport type
- Retrieve an event by its ID
- Update the status of an event with predefined restrictions
- In-memory H2 database for lightweight storage
- Unit tests using JUnit and Mockito

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **H2 Database**

## Setup & Run Instructions

### Prerequisites

Ensure you have the following installed:

- Java 17+
- Maven 3+

### Steps to Run the Application

1. Clone the repository:
   ```sh
   git clone https://github.com/surbhisaraf/sports-event-service.git
   cd sports-event-service
   ```
   Or download the zip file


2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```
4. The API will be available at `http://localhost:8080`

## API Endpoints

### Create a Sport Event

- **POST** `api/v1/sports-event`
- **Request Body:**
  ```json
  {
    "name": "Champions League Final",
    "sport": "Football",
    "status": "INACTIVE",
    "start_time": "2025-06-01T18:00:00"
  }
  ```

### Get All Events (with optional filters)

- **GET** `api/v1/sports-event/getEvents`
- **Query Parameters:** `status` (optional), `sport` (optional)
- **Example:** `api/v1/sports-event/getEvents?status=ACTIVE&sport=Football`

### Get Event by ID

- **GET** `/api/v1/sports-event/{id}`

### Update Event Status

- **PATCH** `api/v1/sports-event/{id}/statusUpdate?status=ACTIVE`

### Status Change Rules

- `INACTIVE → ACTIVE` (Only if `start_time` is in the future)
- `ACTIVE → FINISHED`
- `ACTIVE → INACTIVE`
- Other transitions are not allowed

## Running Tests

To execute unit tests, run:

```sh
mvn test
```

