FROM gradle:7.2-jdk11 AS build

WORKDIR /app
COPY . .

RUN gradle clean build --no-daemon

FROM openjdk:11-jre-slim

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/
RUN mv *.jar app.jar

CMD ["java", "-Xmx256m", "-jar", "app.jar"]
