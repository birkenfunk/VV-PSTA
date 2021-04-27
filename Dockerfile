FROM openjdk:11-jre

ARG JAR_FILE=build/libs/asbeckalexander-1.0-SNAPSHOT.jar

ADD $JAR_FILE /opt/app/asbeckalexander-1.0-SNAPSHOT.jar

EXPOSE 1024

ENTRYPOINT ["java","-jar","/opt/app/asbeckalexander-1.0-SNAPSHOT.jar"]