
# church-auth
**Build Status:**
[![Build Status](https://travis-ci.com/church-source/church-people.svg?branch=master)](https://travis-ci.com/church-source/church-auth)
**Code Coverage Status:**
[![codecov](https://codecov.io/gh/church-source/church-people/branch/master/graph/badge.svg)](https://codecov.io/gh/church-source/church-auth)

A church auth service

### Local Dev Environment
To build: `gradlew build`

To run: `gradlew bootRun`

### Docker Environment
The following will bring up the environment with using images built on dockerhub. 
1. Bring up the containers with docker-compose: `sudo docker-compose up -d`
2. Access API in port 8081 (as configured in the docker-compose.yml)
