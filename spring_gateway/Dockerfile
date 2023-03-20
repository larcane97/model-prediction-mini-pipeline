FROM adoptopenjdk/openjdk11

ADD . /application
WORKDIR /application
RUN ./gradlew clean build
WORKDIR /application/build/libs
ENTRYPOINT ["sh", "-c", "java -jar hello-spring-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080