FROM adoptopenjdk/openjdk11

ADD spring_gateway/. /application
WORKDIR /application
RUN ./gradlew clean build --exclude-task test
WORKDIR /application/build/libs
ENTRYPOINT ["sh", "-c", "java -jar spring_gateway-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080