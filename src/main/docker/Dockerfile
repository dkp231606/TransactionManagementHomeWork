FROM openjdk:21-jdk-slim

#RUN mkdir -p /app
#
#WORKDIR /app

ARG JAR_FILE=target/bank-transaction-management-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /app.jar

EXPOSE 80

CMD ["java", "-jar", "app.jar"]