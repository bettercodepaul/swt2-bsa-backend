[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=exxcellent_swt2-bsa-backend&metric=alert_status)](https://sonarcloud.io/dashboard?id=exxcellent_swt2-bsa-backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=exxcellent_swt2-bsa-backend&metric=coverage)](https://sonarcloud.io/dashboard?id=exxcellent_swt2-bsa-backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=exxcellent_swt2-bsa-backend&metric=code_smells)](https://sonarcloud.io/dashboard?id=exxcellent_swt2-bsa-backend)


# Bogenliga Application WS21


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
