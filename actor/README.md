# Table of Contents

1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)

# General-info

This is an actor with a Rest Api witch can change the status of the actor.


## Rest API

Post Requests:

| ip | Description|
| --- | --- |
| http://*ActorIp*:*ActorPort*/v1/sensors?status=*new_Status* | Changes the status of the actor to the new status|


# Technologies

 A list of technologies used within the project:

* [Log4j2](https://logging.apache.org/log4j/2.x/) Version: 2.14.1

* [SpringBoot](https://spring.io/projects/spring-boot) Version: 2.5.0

* [Junit](https://junit.org/junit5/) Version: 5.7.0

* [Jacoco](https://www.jacoco.org/jacoco/trunk/index.html) Version: 0.8.6

* [SonarQube](https://www.sonarqube.org/) Version: 8.8



# Installation

1. Start a MySQL Database and the [Server](inf-docker.fh-rosenheim.de/vv-inf-sose21/asbeckalexander/server)
2. Pull the [Actor](inf-docker.fh-rosenheim.de/vv-inf-sose21/asbeckalexander/actor) docker container
3. Start the docker container with the environment variables(ServerRegistrationURL, ActorID)
