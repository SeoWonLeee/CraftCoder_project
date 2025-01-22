FROM openjdk:17-jdk-slim

ADD ./build/libs/subscription-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/subscription-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081