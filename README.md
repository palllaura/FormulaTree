# FormulaTree

FormulaTree is a simple web application for collecting user contact details and their preferred car brands.

The application models car brands as a hierarchical tree structure and allows users to select multiple options, save their data, and update it within the same session.

## Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Docker
* Vanilla JavaScript (Planned)

## Running the Application

### 1. Start the database

```bash
docker compose up -d
```

### 2. Run the application

```bash
./gradlew bootRun
```

The application will be available at:

```
http://localhost:8080
```

## Database

* PostgreSQL is used as the primary database
* Database runs in Docker container
* Schema is managed automatically by Hibernate (initially)

## Features (Planned)

* User contact form with validation
* Hierarchical car brand selection
* Data persistence
* Session-based data editing