FROM eclipse-temurin:21.0.2_17-jdk-jammy

RUN mkdir -p /app

WORKDIR /app

ARG JAR_FILE=target/bank-transaction-management-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app.jar

EXPOSE 3000

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
