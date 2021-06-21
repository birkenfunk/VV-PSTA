# Table of Contents

1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)

# General-info

This is a sensor witch sends every 60 sec data to the server.<br>
The values are between 0 and 30.



# Technologies

 A list of technologies used within the project:

* [Log4j2](https://logging.apache.org/log4j/2.x/) Version: 2.14.1

* [Junit](https://junit.org/junit5/) Version: 5.7.0

* [Jacoco](https://www.jacoco.org/jacoco/trunk/index.html) Version: 0.8.6

* [SonarQube](https://www.sonarqube.org/) Version: 8.8



# Installation

1. Start a MySQL Database and the [Server](inf-docker.fh-rosenheim.de/vv-inf-sose21/asbeckalexander/server)
2. Pull the [Sensor](inf-docker.fh-rosenheim.de/vv-inf-sose21/asbeckalexander/sensor) docker container
3. Start the docker container with the environment variables(ServerRegistrationURL, ServerPublishURL, SensorID)
