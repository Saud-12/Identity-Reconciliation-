FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/identityReconcillation-0.0.1-SNAPSHOT.jar identityReconcillation.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "identityReconcillation.jar"]