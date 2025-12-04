FROM eclipse-temurin:21-jre-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
LABEL authors="Camilo"
ENTRYPOINT ["java", "-jar", "/app.jar"]