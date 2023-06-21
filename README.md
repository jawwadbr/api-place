<h1 align="center">
  API Place Manager Challenge
</h1>

API to manage places (CRUD) from this [Challenge](https://github.com/RocketBus/quero-ser-clickbus/tree/master/testes/backend-developer) for back-end developers. It's a simple challenge to test my skills on building APIs.
## Technologies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://docs.spring.io/spring-data/data-jpa/docs/current/reference/html/#repositories)
- [Slugify](https://github.com/slugify/slugify)
- [H2 Database](https://www.h2database.com/html/main.html)

## How to run

### Locally
- Clone the repository git
- Build the project:
```
./mvnw clean package 
```
- Execute project:
```
java -jar target/challenge-0.0.1-SNAPSHOT.jar
```
The API will be available at [localhost:8080](localhost:8080).

## API Endpoints

To make the HTTP requests below, [Postman](https://www.postman.com) tool was used:

- GET /api/places
```
[
    {
        "name": "Tampa City",
        "city": "Tampa City",
        "state": "Florida",
        "slug": "tampa-city",
        "createdAt": "2023-06-21T19:26:21.49374",
        "updatedAt": "2023-06-21T19:26:21.49374"
    }
]
```
- GET /api/places/{id}
```
{
    "name": "Tampa City",
    "city": "Tampa City",
    "state": "Florida",
    "slug": "tampa-city",
    "createdAt": "2023-06-21T19:26:21.49374",
    "updatedAt": "2023-06-21T19:26:21.49374"
}
```
- POST /api/places
```
{
    "name": "Tampa City",
    "city": "Tampa City",
    "state": "Florida"
}
```
- PATCH /api/places/{id}
```
{
    "name": "New Tampa City",
    "city": "New Tampa City",
    "state": "New Florida"
}

Status 200 OK

{
    "name": "New Tampa City",
    "city": "New Florida",
    "state": "new-tampa-city",
    "slug": "New Tampa City",
    "createdAt": "2023-06-21T19:46:38.076203",
    "updatedAt": "2023-06-21T19:47:03.137749"
}
```
- DELETE /api/places/{id}