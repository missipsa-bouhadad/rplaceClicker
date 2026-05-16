# r/Place Clicker

## Prérequis
- Java 21
- Maven
- MySQL (MySQL Workbench)
- Apache Tomcat 10


## Installation
### 1. Cloner le projet
```powershell
git clone https://git.univ-pau.fr/mdarricau004/cookie.git
```

### 2. Créer la base de données
Ouvrir MySQL Workbench et créer une base de données nomée `rplace_clicker_db`

### 3. Configurer la connexion a la base de données
Dans le fichier `JpaInitializer.java`, remplacez `dbUser` par votre nom d'utilisateur MySQL (par défaut "root") et `dbPassword` par votre mot de passe MySQL (par défaut "root").

### 4. Compiler le projet
```powershell
mvn package
```

### 5. Déployer sur Tomcat
**Windows :**

Si c'est le premier déploiement, supprimer l'ancien ROOT :
```powershell
Remove-Item -Recurse -Force chemin_de_tomcat\webapps\ROOT
```
Puis copier le WAR vers Tomcat
```powershell
copy target\rplace-clicker-1.0-SNAPSHOT.war chemin_de_tomcat\webapps\ROOT.war
```

### 6. Lancer Tomcat
Aller dans le dossier `bin` de votre Tomcat et double-cliquer sur `startup.bat`.

### 7. Ouvrir le navigateur sur 
```
http://localhost:8080
```

### 8. Vérification
Sur MySQL Workbench, vérifier que les tables ont été créées automatiquement dans `rplace_clicker_db`.