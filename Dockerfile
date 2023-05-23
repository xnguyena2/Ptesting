FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-11-jdk -y
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM openjdk:11-jdk-slim

EXPOSE 8080

COPY --from=build /build/libs/gradle-getting-started-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]