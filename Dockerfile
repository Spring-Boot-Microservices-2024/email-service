FROM openjdk:24

WORKDIR /app

COPY build/libs/email-service-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
