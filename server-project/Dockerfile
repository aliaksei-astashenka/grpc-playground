FROM amazoncorretto:8u242
EXPOSE 8080
ARG JAR_FILE=build/libs/server-project-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} server-project-1.0-SNAPSHOT.jar
ENTRYPOINT exec java -jar /server-project-1.0-SNAPSHOT.jar