FROM maven:3.9.0-amazoncorretto-17

COPY ./ /app

WORKDIR /app

RUN mvn clean install spring-boot:repackage

FROM openjdk:20-ea-17-jdk

COPY --from=0 /app/target/note-0.0.1-SNAPSHOT.jar /note/note.jar

WORKDIR /note

ENTRYPOINT ["java", "-jar", "/note/note.jar"]