FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/meetime-challenge.jar meetime-challenge.jar

EXPOSE 8080

CMD ["java", "-jar", "meetime-challenge.jar"]