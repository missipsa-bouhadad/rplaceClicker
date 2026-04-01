# Étape 1 : Build avec Maven (version Java 21)
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution avec Tomcat (version Java 21)
FROM tomcat:10.1-jdk21-temurin
COPY --from=build /app/target/rplace-clicker-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]