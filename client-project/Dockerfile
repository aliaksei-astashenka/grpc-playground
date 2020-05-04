FROM amazoncorretto:8u242
EXPOSE 8080
ARG JAR_FILE=build/libs/client-project-1.0-SNAPSHOT.jar
ADD ${JAR_FILE} client-project-1.0-SNAPSHOT.jar
ENTRYPOINT exec java -jar /client-project-1.0-SNAPSHOT.jar