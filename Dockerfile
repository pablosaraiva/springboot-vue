# Build the VuePress static site from the committed lockfile
FROM node:20-alpine AS vuepress-build
COPY src/main/vuepress/package.json src/main/vuepress/package-lock.json /tmp/
WORKDIR /tmp/
RUN npm ci --no-audit --no-fund
COPY src/main/vuepress/docs /tmp/docs
RUN npx vuepress build docs

# Build the Spring Boot jar, embedding the generated static site
FROM maven:3.9-eclipse-temurin-17 AS maven-build
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY --from=vuepress-build /tmp/docs/.vuepress/dist /tmp/src/main/resources/static/
WORKDIR /tmp/
RUN mvn -B install

# Runtime image (JRE only, maintained base)
FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=/tmp/target/*.jar
COPY --from=maven-build ${JAR_FILE} app.jar
COPY --from=maven-build /tmp/target/classes/static/ static
ENTRYPOINT ["java","-Xms350m","-Xmx350m","-jar","/app.jar"]