FROM openjdk:21-jdk

WORKDIR /app

COPY ./build/libs/app-*.jar ./notes-app.jar

CMD ["java", "-jar", "./notes-app.jar"]