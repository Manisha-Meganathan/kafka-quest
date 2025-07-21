FROM gradle:jdk17-alpine AS BUILD
WORKDIR /home
COPY ./kq-sink-application kq-sink-application/
COPY ./kq-common kq-common/
WORKDIR /home/kq-sink-application
RUN gradle shadowJar

FROM openjdk:17-alpine
WORKDIR /home
COPY --from=BUILD /home/kq-sink-application/build/libs/kq-sink-application-all.jar app.jar
CMD ["java", "-jar", "app.jar", "docker"]