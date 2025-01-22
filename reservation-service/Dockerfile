FROM openjdk:17-jdk-slim

ADD ./build/libs/reservation-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/reservation-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080