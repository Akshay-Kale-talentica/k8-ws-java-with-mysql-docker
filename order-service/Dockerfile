FROM maven:3.9-amazoncorretto-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/order-service-0.0.1-SNAPSHOT.jar order-service.jar

EXPOSE 8080

CMD ["java", "-jar", "order-service.jar"]
