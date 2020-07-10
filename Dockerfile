# Stage 1
FROM openjdk:8-jdk-alpine as build

WORKDIR /app

COPY gradlew .
COPY .gradle .gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

RUN ./mvnw package -DskipTests

# Stage 2
FROM openjdk:8-jre-alpine

WORKDIR /app
ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD
ENV url=$SPRING_DATASOURCE_URL
ENV username=$SPRING_DATASOURCE_USERNAME
ENV password=$SPRING_DATASOURCE_PASSWORD

COPY --from=build /app/target/vidflow-*.jar vidflow.jar
ENTRYPOINT ["java", "-jar", "vidflow.jar", "--spring.profiles.active=dev", "--spring.datasource.url=${url}", "--spring.datasource.username=${username}", "--spring.datasource.password=${password}"]