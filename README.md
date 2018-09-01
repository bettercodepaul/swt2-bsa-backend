[![Build Status](https://travis-ci.org/andre-lehnert/mock-backend.svg?branch=master)](https://travis-ci.org/andre-lehnert/mock-backend)
[![sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=online.bogenliga%3Abogenliga&metric=alert_status)](https://sonarcloud.io/dashboard?id=online.bogenliga%3Abogenliga)
[![sonarcloud_cov](https://sonarcloud.io/api/project_badges/measure?project=online.bogenliga%3Abogenliga&metric=coverage)](https://sonarcloud.io/dashboard?id=online.bogenliga%3Abogenliga)
[![codecov](https://codecov.io/gh/andre-lehnert/mock-backend/branch/master/graph/badge.svg)](https://codecov.io/gh/andre-lehnert/mock-backend)


# Bogenliga Online Application

This repository can be used for a basic working Spring Boot application

## Content

- Spring Boot with REST services
- Flyway

__tbd__

## Requirements

__tbd__

## How to use

You can use mvnw (shell) or mvnw.cmd script to configure Apache Maven.
Use ```mvnw``` instead of ```mvn``` commands.

1. Build project
   - go to the root directory: ```cd bogenliga```
   - run: ```mvn clean install```
2. Run database migration
    - go to the bogenliga-db-migration directory: ```cd bogenliga-db-migration```
    - run: ```mvn flyway:migrate```
3. Run Spring Boot
    - go to the bogenliga-application directory: ```cd bogenliga-application```
    - run: ```mvn spring-boot:run```


