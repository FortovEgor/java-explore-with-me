#FROM amazoncorretto:11
#COPY target/stats-server-0.0.1-SNAPSHOT.jar stats-server.jar
#ENTRYPOINT ["java", "-jar", "/stats-server.jar"]

FROM eclipse-temurin:21-jre-jammy
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]