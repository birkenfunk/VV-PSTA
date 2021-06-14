FROM openjdk:11-jre

ARG JAR_FILE=build/libs/sensor-1.0-SNAPSHOT-all.jar

COPY $JAR_FILE /opt/app/sensor-1.0-SNAPSHOT-all.jar

ENTRYPOINT ["java","-jar","/opt/app/sensor-1.0-SNAPSHOT-all.jar"]
