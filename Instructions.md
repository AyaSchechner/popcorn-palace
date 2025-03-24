# Instructions for Popcorn Palace

## Prerequisites

Before running the application, make sure you have the following installed:

- **Java 21+**
- **Maven** (or use the bundled wrapper `./mvnw`)
- **Docker**
- **PostgreSQL** (or use Docker Compose for the preconfigured DB)
- Any Java IDE (e.g., IntelliJ IDEA, Eclipse, VS Code)

---

## Project Structure

This project contains:

- `Movie API`: Manage movies in the system.
- `Showtime API`: Manage movie showtimes.
- `Booking API`: Book tickets for specific showtimes.

---

## Setup Database (with Docker)

You can use the provided `compose.yml` file to spin up PostgreSQL.

```bash
docker-compose up -d
```

> This will create the database `popcorn-palace` and initialize it with schema + sample data from `schema.sql` and `data.sql`.

If you need to reset your DB:
```bash
psql -U popcorn-palace -d popcorn-palace -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
```

---

## Build & Run

You can run the project using the embedded Spring Boot Maven wrapper.

### Build the project:

```bash
./mvnw clean package
```

### Run the Spring Boot app:

```bash
./mvnw spring-boot:run
```

By default, the server runs on:  
`http://localhost:8080`

---

## Running Tests

To execute unit tests:

```bash
./mvnw test
```
You should see `BUILD SUCCESS` and all tests passing.

### Manual API Testing

A requestsTest.http file is included for manual testing using tools like VS Code (REST Client), IntelliJ HTTP client, or Postman.

It demonstrates:

Movie creation, update, retrieval, and deletion

Showtime creation, update, retrieval, and deletion

Ticket booking, including edge cases (e.g., duplicate seat, invalid input)

Use it to quickly validate API behavior beyond automated tests.

---


## API Endpoints Summary

### Movies

| Method | Endpoint                  | Description             |
|--------|---------------------------|-------------------------|
| GET    | `/movies/all`             | Get all movies          |
| GET    | `/movies/{id}`            | Get movie by ID         |
| POST   | `/movies`                 | Add new movie           |
| POST   | `/movies/update/{title}`  | Update movie by title   |
| DELETE | `/movies/{title}`         | Delete movie by title   |

#### Movie Constraints:

- `title`: required, non-empty
- `genre`: required, non-empty
- `duration`: must be **> 0**
- `rating`: must be **between 0 and 10**
- `releaseYear`: must be **between 1895 and 2025**

---

### Showtimes

| Method | Endpoint                          | Description                             |
|--------|-----------------------------------|-----------------------------------------|
| GET    | `/showtimes/{id}`                 | Get showtime by ID                      |
| POST   | `/showtimes`                      | Add a new showtime                      |
| POST   | `/showtimes/update/{id}`          | Update showtime by ID                   |
| DELETE | `/showtimes/{id}`                 | Delete showtime by ID                   |
| GET    | `/showtimes/all`                  | Get all showtimes (**debugging only**)  |

#### Showtime Constraints:

- `startTime` must be **before** `endTime`
- No overlapping showtimes allowed **within the same theater**
- `price`: must be non-negative

---

### Bookings (Tickets)

| Method | Endpoint         | Description              |
|--------|------------------|--------------------------|
| POST   | `/bookings`      | Book a ticket            |

#### Booking Constraints:

- Cannot book same `seatNumber` for a showtime twice
- `userId`: required
- `seatNumber`: must be a positive integer

---
 
