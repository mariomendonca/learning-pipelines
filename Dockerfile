FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y
COPY . .

RUN apt-get install maven -y
RUN mvn clean install -DskipTests


FROM eclipse-temurin:21-jdk-jammy AS runtime
WORKDIR /app

COPY --from=build target/learning-pipelines-0.0.1-SNAPSHOT.jar /app/learning-pipelines.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/learning-pipelines.jar"]
