# ledger-service
Banking Ledger Service -

# Getting started Spring Boot

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)
- [Postgresql](https://www.postgresql.org/download/windows/)

## Running the application locally

Database Configaration
* add or change configarations acording to application.property file

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.msd.ledgerservice.LedgerServiceApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Test the application locally

After Successfully Running the Spring Boot Application navigate below URL for test API

```shell
http://localhost:8080/api/swagger-ui.html
```
Generate Access Token for access ledger Service

* test user : user
* test password : user

```shell
http://localhost:8080/api/swagger-ui.html#/auth-controller/authenticateUserUsingPOST
```


