# Table of Contents

1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)

# General-info

This is a simple Rest Api witch can manage sensors and actors.<br>
It has defined a Rule Engine witch will check the rules every 30 seconds.

## Database entity

Sensor:

| name | type | key | unique |
|----|----|----|----|
| SensorId | int | ✅ | ❌ |
| SensorName | String | ❌ | ❌ |
| Registrierdatumund | Datum | ❌ | ❌ |
| Location | String | ❌ | ❌ |

Actor:

| name | type | key | unique |
|----|----|----|----|
| AktorId | int | ✅ | ❌ |
| AktorName | String | ❌ | ❌ |
| Registrierdatum | Datum | ❌ | ❌ |
| Location | String | ❌ | ❌ |
| ServiceURL | String | ❌ | ❌ |
| Status | String | ❌ | ❌ |

Rule

| name | type | key | unique |
|----|----|----|----|
| RuleId | int | ✅ | ❌ |
| RuleName | String | ❌ | ✅  |
| SensorId | int | ❌ | ❌ |
| AktorId | int | ❌ | ❌ |
| Treshhold | short (1-29) | ❌ | ❌ |

SensorData

| name | type | key | unique |
| --- | --- |----|----|
| SensorDataID | int | ✅ | ❌ |
| SensorID | Sensor | ❌ | ❌ |
| Timestamp | Datum | ❌ | ❌ |
| CurrentValue | short(0-30) | ❌ | ❌ |
| TemperatureUnit | Enum Unit | ❌ | ❌ |

## Rest API

Get Requests:

| ip | Description|
| --- | --- |
| http://*ServerID*:*ServerPort*/v1/sensors | Returns a list all sensors in a json format |
| http://*ServerID*:*ServerPort*/v1/sensors/{*id*} | Returns the sensor with the given id|
| http://*ServerID*:*ServerPort*/v1/actors | Returns a list all actors in a json format |
| http://*ServerID*:*ServerPort*/v1/actors/{*id*} | Returns the actor with the given id|
| http://*ServerID*:*ServerPort*/v1/rules | Returns a list all rules in a json format |
| http://*ServerID*:*ServerPort*/v1/rules/{*id*} | Returns the rule with the given id|
| http://*ServerID*:*ServerPort*/v1/sensordata | Returns a list all sensordata in a json format |
| http://*ServerID*:*ServerPort*/v1/sensordata/{*id*} | Returns the sensordata with the given id|


Post Requests:

| ip | Description|
| --- | --- |
| http://*ServerID*:*ServerPort*/v1/sensors | Adds a sensor to the database retuns the rest ip address of the sensor|
| http://*ServerID*:*ServerPort*/v1/sensors/{*id*} | Adds a sensordata to the database and connects it with the sensor retuns the rest ip address of the sensordata|
| http://*ServerID*:*ServerPort*/v1/actors | Adds an actor to the database retuns the rest ip address of the actor|
| http://*ServerID*:*ServerPort*/v1/rules | Adds a rule to the database retuns the rest ip address of the rule|

Delete Requests:

| ip | Description|
| --- | --- |
| http://*ServerID*:*ServerPort*/v1/sensors/{*id*} | Sets the deleted status of the Sensor to true|

Put Requests:

| ip | Description|
| --- | --- |
| http://*ServerID*:*ServerPort*/v1/sensors/{*id*} | Updates the information of the sensor the id can't be changed |

# Technologies

 A list of technologies used within the project:

* [Log4j2](https://logging.apache.org/log4j/2.x/) Version: 2.14.1

* [SpringBoot](https://spring.io/projects/spring-boot) Version: 2.5.0

* [Springdoc-OpenApi](https://github.com/springdoc/springdoc-openapi) Version: 1.5.9

* [Mysql Server](https://www.mysql.com/de/) Version: 8

* [Mockito](https://site.mockito.org/) Version:3.11.1

* [Junit](https://junit.org/junit5/) Version: 5.7.0

* [Jacoco](https://www.jacoco.org/jacoco/trunk/index.html) Version: 0.8.6

* [SonarQube](https://www.sonarqube.org/) Version: 8.8



# Installation

1. Start a MySQL Database
2. Pull the [Server](inf-docker.fh-rosenheim.de/vv-inf-sose21/asbeckalexander/server) docker container
3. Start the docker container with the environment variables(SqlUrl, Username, Password)
4. Now you can add sensors and actors to the service

