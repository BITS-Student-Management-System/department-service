FROM openjdk:8
EXPOSE 9001
# Install maven
RUN apt-get update && apt-get install -y maven

# Creating working directory
WORKDIR /app

# Add src to working directory
ADD pom.xml /app/pom.xml
ADD src /app/src

# Build JAR
RUN mvn package -DskipTests=true

# Start app
ENTRYPOINT ["java","-jar","/app/target/department-service-0.0.1-SNAPSHOT.jar"]