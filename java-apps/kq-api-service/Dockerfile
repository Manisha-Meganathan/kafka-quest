FROM gradle:jdk17-alpine AS BUILD
WORKDIR /home
COPY ./kq-api-service kq-api-service/
COPY ./kq-common kq-common/
WORKDIR /home/kq-api-service
RUN gradle bootJar

FROM openjdk:17-alpine
WORKDIR /home
COPY --from=BUILD /home/kq-api-service/build/libs/kq-api-service.jar app.jar
CMD ["java", "-jar", "app.jar"]