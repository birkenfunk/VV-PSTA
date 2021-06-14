FROM openjdk:11-jre

ARG JAR_FILE=build/libs/actor-0.0.1-SNAPSHOT.jar

COPY $JAR_FILE /opt/app/actor-0.0.1-SNAPSHOT.jar

ENV LOG_PATH=logs/log.log
ENV LOG_LEVEL=info
ENV JSON_FILE=data.json

ENTRYPOINT ["java","-jar","/opt/app/actor-0.0.1-SNAPSHOT.jar"]
