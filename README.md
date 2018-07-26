# Spring Employee Challenge

The goal is to implement an application that handles the employees of a company. The application must expose a REST API, so that other services can communicate with it easily.

## Disclaimer

This was a challenge it is not for production usage. So if you have tips or suggestion how to make it nicer and cleaner let me know.

## Getting Started

These instructions will get you a running server with a simple user authentication.

### Prerequisites

* [JAVA](https://www.java.com/de/download/help/download_options.xml)
* [MAVEN](https://maven.apache.org/install.html)  - Dependency Management
* [DOCKER](https://docs.docker.com/install/) and [Docker Compose](https://docs.docker.com/compose/install/) - Containerization software

Developed with [IntelliJ IDEA](https://www.jetbrains.com/idea/) but you dont have to use it.

# Usage

You can start it easily with `./runDocker.sh`. This will compile the project, build an image for the application, download and configure a [postgresql](https://www.postgresql.org/) database and export API port to *8080*.
After everything is finished, the application waits 20sec for the database to be initialised. So please be patient ;)

You can reach the API under http://localhost:8080/api/v1/employee
The default user to do operations on the API is:
Username: admin
Password: 1234

#### View API with [Swagger](https://swagger.io/)

The swagger-ui can be accessed via http://localhost:8080/swagger-ui.html

## Configuration / Profiles

The application can run with three different profiles:
* **dev** - Development environment
  * with an HSQLDB (filebased - storage) and swagger-ui
* **prod** - Production environment
  * connectes to an Postgres server and the databasename should be **employeedb**
* **test** - Test environment
  * with an HSQLDB but without the swagger-ui

The default profile is **prod**, all environment variables can be found under [ressources](https://github.com/hechi/springEmployeeChallenge/tree/master/src/main/resources) and you can change it to your needs

## Running without Docker

You have to do the packaging first before you can run the application.
```
# java -jar target/EmployeeApi-0.1.jar
```

Run with another profile
```
# java -jar target/EmployeeApi-0.1.jar --spring.profiles.active=dev
```

## Package

The command `mvn package` will produce an executable jar in the **target/** folder.

# Development

You can develop it with the Intellij IDEA or run the spring MAVEN command.

## Build and run the spring application with MAVEN

```
# mvn spring-boot:run
```

## Authors

* **Andreas Hechenberger** - *Initial work* - [Hechi](https://github.com/hechi)

