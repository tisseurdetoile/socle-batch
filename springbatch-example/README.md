# Welcome to socle-batch/springbatch-example 👋
[![Documentation](https://img.shields.io/badge/documentation-yes-brightgreen.svg)](https://github.com/tisseurdetoile/socle-batch/wiki)
[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-yellow.svg)](#)
[![Twitter: tisseurdetoile](https://img.shields.io/twitter/follow/tisseurdetoile.svg?style=social)](https://twitter.com/tisseurdetoile)

> Simpliest spring-batch sample project with socle-batch API integration

### 🏠 [Homepage](https://github.com/tisseurdetoile/socle-batch)

## SampleJob

A minimal [SpringBatch Job](https://github.com/tisseurdetoile/socle-batch/blob/main/springbatch-example/src/main/java/net/tisseurdetoile/batch/job/SampleJob.java)
With :
- Reader : Iterating on an Array 
- Processor : For waiting
- Writer : Display text on screen

## SampleInseJob

A more [complex Job](https://github.com/tisseurdetoile/socle-batch/blob/main/springbatch-example/src/main/java/net/tisseurdetoile/batch/job/SampleInseeJob.java) 
wich fetch the region/departement List on the Insee website and process them into a csv file. 

### Notable point.

- Fetch file on SSL
- Read the file as a Zipped on (cf : [UnZipBufferedReaderFactory](https://github.com/tisseurdetoile/socle-batch/blob/main/springbatch-socle-tools/src/main/java/net/tisseurdetoile/batch/socle/readerfactory/UnZipBufferedReaderFactory.java))

### Troubleshooting

### Deal with SSL error

You can have SSL like these :

- "PKIX Path Building Failed"
- "Failed to initialize the reader"

It's because you don't have the ssl certificate of the INSEE in you thrusted authorities.
You have to fetch it this way in a console.

```bash
rm -f cert.pem && echo -n | openssl s_client -connect www.insee.fr:443 | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > ./insee-cert.pem
```

Add it to your  cacerts

```bash
keytool -import -alias www.insee.fr  -cacerts -file ./insee-cert.pem
```

Other useful command :

```bash
keytool -list #list les certificat dans le coffre
keytool -delete  -cacerts -alias  www.insee.fr   #supprime le certicat du coffre 
```

## Author

👤 **Le TisseurDeToile**

* Website: http://www.tisseurdetoile.net
* Twitter: [@tisseurdetoile](https://twitter.com/tisseurdetoile)
* Github: [@tisseurDeToile](https://github.com/tisseurDeToile)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!

Feel free to check [issues page](https://github.com/tisseurdetoile/socle-batch/issues). 

## Show your support

Give a ⭐️ if this project helped you!


***
_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_