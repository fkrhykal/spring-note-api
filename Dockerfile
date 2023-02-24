FROM maven

COPY ./ /app

WORKDIR /app

RUN mvn clean install spring:repackage

FROM openjdk:20-ea-17-jdk

COPY --from=builder ./target/note-0.0.1-SNAPSHOT.jar /app/note.jar

ENTRYPOINT ["java", "-jar", "/app/note.jar"]