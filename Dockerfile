FROM openjdk:17-jdk-alpine

WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ./build/libs/AchivementsServer-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]