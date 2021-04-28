FROM openjdk:11-jre

ARG JAR_FILE=build/libs/asbeckalexander-1.0-SNAPSHOT.jar

ADD $JAR_FILE /opt/app/asbeckalexander-1.0-SNAPSHOT.jar

ENV PORT=1024
ENV LOG_PATH=logs/log.log
ENV LOG_LEVEL=info
ENV JSON_FILE=data.json

EXPOSE $PORT

ENTRYPOINT ["java","-jar","/opt/app/asbeckalexander-1.0-SNAPSHOT.jar"]