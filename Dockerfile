FROM maven

COPY ./ /app/

RUN mvn clean install spring:repackage

FROM openjdk:20-ea-17-jdk

COPY --from=0 /app/target/note-0.0.1-SNAPSHOT.jar /note/note.jar

WORKDIR /note

ENTRYPOINT ["java", "-jar", "/app/note.jar"]