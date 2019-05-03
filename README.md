[![Build Status](https://travis-ci.org/exxcellent/swt2-bsa-backend.svg?branch=master)](https://travis-ci.org/exxcellent/swt2-bsa-backend)
[![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=de.bogenliga.application&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.bogenliga.application)
[![sonarcloud_cov](https://sonarcloud.io/api/project_badges/measure?project=de.bogenliga.application&metric=coverage)](https://sonarcloud.io/dashboard?id=de.bogenliga.application)


# Bogenliga Application


## Content

- Spring Boot with REST services
- Flyway


## How to use

You can use mvnw (shell) or mvnw.cmd script to configure Apache Maven.
Use ```mvnw``` instead of ```mvn``` commands.

1. Build project
   - go to the root directory: ```cd bogenliga```
   - run: ```mvn clean install```
2. Run database migration
    - go to the bogenliga-db-migration directory: ```cd bogenliga-db-migration```
    - run: ```mvn clean flyway:clean flyway:migrate -PLOCAL``` 
3. Run Spring Boot
    - go to the bogenliga-application directory: ```cd bogenliga-application```
    - run: ```mvn spring-boot:run```
4. Open Swagger API documentation
    - http://localhost:9000/swagger-ui.html
   
## References

- https://flywaydb.org/documentation/migrations
- https://flywaydb.org/documentation/maven/
- https://flywaydb.org/documentation/command/migrate
