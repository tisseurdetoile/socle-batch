# springbatch-example

Projet contenant des exemples pour commencer à utiliser le socle-batch et/ou springbatch.

## SampleJob

Exemple de Job minimal :
- Reader : sur une liste créer une mémoire 
- Processor : qui va attendre
- Writer : Affiche la liste à l'ecran

## SampleInseJob

Un job plus complexe qui récupere la liste des régions et des département sur le site de l'insee
et les agrèges pour en faire un seul fichier csv.

### point remarquable !

- récuperation des fichier en ssl
- Lecture la à la volé des fichier zippé (cf : UnZipBufferedReaderFactory)

### Lancement du job

### Bibliographie

La liste des regiosn francaise https://www.insee.fr/fr/statistiques/fichier/3363419/reg2018-txt.zip

### Gerrer les erreur ssl 

Il se peux que vous ayez des erreur de connection soit :

- "PKIX Path Building Failed"
- "Failed to initialize the reader"

C'est que vous n'avez pas le certificat de l'insee dans vos authorité de confiance
il faut donc récupérer le certificat SSL (vous pouvez le récupérer par votre navigateur)
Ou en mode console comme ici.

```bash
rm -f cert.pem && echo -n | openssl s_client -connect www.insee.fr:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ./insee-cert.pem
```

L'importer dans le cacerts

```bash
keytool -import -alias www.insee.fr  -cacerts -file ./insee-cert.pem
```

Autres commande utile :

```bash
keytool -list #list les certificat dans le coffre
keytool -delete  -cacerts -alias  www.insee.fr   #supprime le certicat du coffre 
```
 