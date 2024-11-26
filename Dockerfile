FROM gradle:jdk17-alpine AS build

COPY . /home/gradle/src

WORKDIR /home/gradle/src

RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon
# RUN gradle clean build -x test

FROM openjdk:17-alpine

EXPOSE 8080

# COPY . .
COPY --from=build /home/gradle/src/build/libs/gradle-getting-started-1.0.jar app.jar

ENTRYPOINT ["java", "-Xmx64m", "-Xss1m", "-jar", "app.jar"]
# ENTRYPOINT ["java", "-jar", "app.jar"]