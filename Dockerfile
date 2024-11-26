FROM gradle:jdk17-alpine AS build

COPY . .
RUN gradle bootJar --no-daemon

FROM openjdk:17-alpine

EXPOSE 8080

COPY . .
COPY --from=build /build/libs/gradle-getting-started-1.0.jar app.jar

ENTRYPOINT ["java", "-Xmx64m", "-Xss1m", "-jar", "app.jar"]
# ENTRYPOINT ["java", "-jar", "app.jar"]