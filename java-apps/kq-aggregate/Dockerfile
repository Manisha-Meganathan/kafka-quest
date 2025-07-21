FROM gradle:jdk17-alpine AS BUILD
WORKDIR /home
COPY ./KESA-Aggregate KESA-Aggregate/
COPY ./KESA-Common KESA-Common/
WORKDIR /home/KESA-Aggregate
RUN gradle shadowJar

FROM openjdk:17-alpine
WORKDIR /home
COPY --from=BUILD /home/KESA-Aggregate/build/libs/KESA-Aggregate-all.jar app.jar
CMD ["java", "-jar", "app.jar", "docker"]