FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21-temurin
# On supprime tout pour être sûr
RUN rm -rf /usr/local/tomcat/webapps/*
# On copie le contenu du dossier généré au lieu du fichier compressé
COPY --from=build /app/target/rplace-clicker-1.0-SNAPSHOT /usr/local/tomcat/webapps/ROOT
EXPOSE 8080
CMD ["catalina.sh", "run"]