FROM node:current-alpine3.11
COPY src/main/vuepress /tmp/
WORKDIR /tmp/
RUN npm install -g vuepress && \
    npm install -g @vuepress/plugin-blog && \
    npm install -D vuepress && \
    npm install -D @vuepress/plugin-blog && \
    vuepress build docs

FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY --from=0 /tmp/docs/.vuepress/dist /tmp/src/main/resources/static/
WORKDIR /tmp/
RUN mvn install

FROM openjdk:8-jdk-alpine
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=/tmp/target/*.jar
COPY --from=MAVEN_TOOL_CHAIN ${JAR_FILE} app.jar
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/classes/static/ static
ENTRYPOINT ["java","-Xms350m","-Xmx350m","-jar","/app.jar"]