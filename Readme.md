# hrbl

A Spring Boot REST API that implements a simple room booking system.
This project provides a basic API that allows users to:
 - View a meeting room’s bookings 
 - Book a meeting for a specific time slot
 - Cancel a booking


The project is built on top of the Spring Boot framework and uses an embedded h2 database to store the booking data.
In this project, the following endpoints are implemented:

| HTTP Method | Path         | Summary                                                |
|-------------|--------------|--------------------------------------------------------|
| GET         | /bookings    | Retrieves a list of bookings for a given room and date |
| POST        | /book        | Creates a new meeting booking                          |
| DELETE      | /cancel/{id} | Deletes a certain booking                              |

If you're using Postman, you can import the collection located under: src/main/resources/Hrbl.postman_collection.json

## Build the application

`mvn clean package -DskipTests`

## Run the application

You can run the application using your IDE or by executing:

`mvn spring-boot:run`

On application start up, the H2 database will be created as file under the project's root directory.
You can change the location of the file as well as the database credentials configured in `src/main/resources/application.yaml`.


`spring.datasource.url=jdbc:h2:~/h2/hrbl-db`  
`spring.datasource.username=sa`  
`spring.datasource.password=sa`

You can view the database while the application is running,  by accessing:
http://localhost:<port>/h2-console



## Tests
The project also includes several tests for the API endpoints and database integration. JUnit and Mockito frameworks have been used for unit testing.
Tests run on their own H2 database file configured in: `src/test/resources/application.yaml`
