FROM adoptopenjdk/openjdk14:ubi
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} department-service.jar
ENTRYPOINT ["java","-jar","/department-service.jar"]
EXPOSE 9001