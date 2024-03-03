FROM openjdk:17
ARG JAR_FILE
WORKDIR /usr/src/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/usr/src/app/app.jar"]
EXPOSE 8080