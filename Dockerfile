FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn install

FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=/tmp/target/*.jar
COPY --from=MAVEN_TOOL_CHAIN ${JAR_FILE} app.jar
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/classes/static/ static
ENTRYPOINT ["java","-Xms350m","-Xmx350m","-jar","/app.jar"]