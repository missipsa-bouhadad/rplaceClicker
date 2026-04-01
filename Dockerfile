# Étape 1 : Build
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Étape 2 : Runtime
FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat/webapps/

# On supprime TOUT le contenu par défaut de Tomcat
RUN rm -rf ./*

# On copie le WAR et on le force en ROOT.war
COPY --from=build /app/target/rplace-clicker-1.0-SNAPSHOT.war ./ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]