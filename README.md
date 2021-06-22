# Table of Contents

1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)

# General-info

This is a simple Rest Api witch can manage sensors and actors.<br>
A sensor can send data to the Server. Afterwards the Server can compare the new data against the defined rules.

For More information please look at the README of the different project


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

1. Install Docker
2. Download the docker-compose.yml file or the whole project
3. Open a commandline and switch to the directory of the docker-compose file
4. Run ``docker-compose up``
5. Now docker should start a MySQL server afterwards the Server and 2 demo actors and 2 demo sensors.
6. When everything has started you can also register your own actors or sensors

