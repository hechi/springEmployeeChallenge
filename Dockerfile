FROM openjdk:8-jdk-alpine
VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} /app.jar
COPY target/EmployeeApi-0.1.jar /app.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["sh","/entrypoint.sh"]