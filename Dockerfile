FROM gradle:7.6.1-jdk17 AS builder

RUN mkdir -p /app/source
COPY . /app/source

WORKDIR /app/source

RUN gradle clean build -x test

FROM eclipse-temurin:17-jre

COPY --from=builder /app/source/build/libs/spring-cloud-gcp-notification-service-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]