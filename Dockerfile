FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY target/classes/static/ static
ENTRYPOINT ["java","-Xms350m","-Xmx350m","-jar","/app.jar"]