FROM maven:3.9.0-amazoncorretto-17

COPY ./ /app

WORKDIR /app

ENV MAIL_HOST ${MAIL_HOST}
ENV MAIL_USER ${MAIL_USER}
ENV MAIL_PASSWORD ${MAIL_PASSWORD}
ENV MAIL_PORT ${MAIL_PORT}
ENV DB_NAME ${DB_NAME}
ENV DB_USER ${DB_USER}
ENV DB_URL ${DB_URL}
ENV DB_PASSWORD ${DB_PASSWORD}

RUN mvn clean install spring-boot:repackage

FROM openjdk:20-ea-17-jdk

ENV MAIL_HOST ${MAIL_HOST}
ENV MAIL_USER ${MAIL_USER}
ENV MAIL_PASSWORD ${MAIL_PASSWORD}
ENV MAIL_PORT ${MAIL_PORT}
ENV DB_NAME ${DB_NAME}
ENV DB_USER ${DB_USER}
ENV DB_URL ${DB_URL}
ENV DB_PASSWORD ${DB_PASSWORD}

COPY --from=0 /app/target/note-0.0.1-SNAPSHOT.jar /note/note.jar

WORKDIR /note

ENTRYPOINT ["java", "-jar", "/note/note.jar"]