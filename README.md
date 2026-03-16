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
Le fichier `persistence.xml` n'est pas inclus dans le projet.
Vous devez le créer à partir du fichier exemple `persistence.xml.example` en remplaçant les valeurs 
`ton_user` et `ton_password` par vos identifiants MySQL

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
```powershell
chemin_de_tomcat\bin\startup.bat
```

### 7. Ouvrir le navigateur sur 
```
http://localhost:8080
```

### 8. Vérification
Sur MySQL Workbench, vérifier que la table `players` a été crée automatiquement dans `rplace_clicker_db`.
