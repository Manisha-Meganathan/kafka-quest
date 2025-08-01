FROM gradle:jdk17-alpine AS BUILD
WORKDIR /home
COPY ./kq-aggregate kq-aggregate/
COPY ./kq-common kq-common/
WORKDIR /home/kq-aggregate
RUN gradle shadowJar

FROM openjdk:17-alpine
WORKDIR /home
COPY --from=BUILD /home/kq-aggregate/build/libs/kq-aggregate-all.jar app.jar
CMD ["java", "-jar", "app.jar", "docker"]