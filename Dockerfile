FROM maven:3.8.1-openjdk-17 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package

FROM openjdk:17.0
COPY --from=build /usr/src/app/target/NewsAgregatorBot-1.0-SNAPSHOT.jar /home/app/backend.jar
ENTRYPOINT ["java", "-jar", "/home/app/backend.jar"]

#FROM openjdk:17.0
#ADD /target/NewsAgregatorBot-1.0-SNAPSHOT.jar backend.jar
#ENTRYPOINT ["java", "-jar", "backend.jar"]

#FROM maven:3.8.5-openjdk-17-slim as builder
#
#WORKDIR /app
#
#COPY ./ .
#
#RUN mvn -e clean package
#
#COPY /target/NewsAgregatorBot-1.0-SNAPSHOT.jar /app/backend.jar
#
#######
#FROM ubuntu/jre:17_edge
#
#WORKDIR /app
#
#COPY --from=builder /app/backend.jar backend.jar
#
#ENTRYPOINT ["java", "-jar", "backend.jar"]