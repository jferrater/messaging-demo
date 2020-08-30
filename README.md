# messaging-demo
[![Build Status](https://travis-ci.com/jferrater/messaging-demo.svg?branch=master)](https://travis-ci.com/jferrater/messaging-demo)<br>

## Pre-requisites
- java 11
- docker
- docker-compose

## Quick start
1. `git clone https://github.com/jferrater/messaging-demo.git`
2. `cd messaging-demo`
3. `./gradlew bootJar`
4. `docker-compose up --build` - Run the text messaging service and a MongoDB in docker containers, respectively.
5. REST API documentation (Swagger UI): `http://localhost:8027/messaging-service/swagger-ui.html`
6. Test the API by selecting an API in the Swagger UI and click `Try it out` or use `curl`

## Development
To build the project, run ```./gradlew clean build``` on project root directory.