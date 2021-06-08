FROM openjdk:11-jre

ARG JAR_FILE=build/libs/server-1.0-SNAPSHOT.jar

COPY $JAR_FILE /opt/app/server-1.0-SNAPSHOT.jar

ENV PORT=9000
ENV LOG_PATH=logs/log.log
ENV LOG_LEVEL=info
ENV JSON_FILE=data.json

EXPOSE $PORT
EXPOSE 3306

ENTRYPOINT ["java","-jar","/opt/app/server-1.0-SNAPSHOT.jar"]
