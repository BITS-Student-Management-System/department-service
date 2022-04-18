#define base docker image
FROM openjdk:8-jdk-alpine
LABEL maintainer="roopika.srinivas"
ADD target/department-service-0.0.1-SNAPSHOT.jar department-service.jar
ENTRYPOINT ["java","-jar","department-service.jar"]
