# HEROKU INFORMATION

## INFORMATION

Application publiée sur [Heroku](https://socle-batch.herokuapp.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

## COMMANDES UTILES

Pour **builder** à partir du repertoire de travail

`mvn clean compile package spring-boot:repackage`

Pour **déployer** sur Heroku à partir du repertoire de travail

`heroku deploy:jar springbatch-example/target/springbatch-example-webapp.jar --app socle-batch --jdk 17`

Consulter les **logs**

`heroku logs --tail --app socle-batch`

Lancer en local site [ici](http://localhost:5000/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

`heroku local web -f Procfile`