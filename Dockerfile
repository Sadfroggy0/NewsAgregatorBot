FROM openjdk:17.0
ADD /target/NewsAgregatorBot-1.0-SNAPSHOT.jar backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]