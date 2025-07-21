FROM gradle:jdk17-alpine AS BUILD
WORKDIR /home
COPY ./KESA-Sink-Application KESA-Sink-Application/
COPY ./KESA-Common KESA-Common/
WORKDIR /home/KESA-Sink-Application
RUN gradle shadowJar

FROM openjdk:17-alpine
WORKDIR /home
COPY --from=BUILD /home/KESA-Sink-Application/build/libs/KESA-Sink-Application-all.jar app.jar
CMD ["java", "-jar", "app.jar", "docker"]