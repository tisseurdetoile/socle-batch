# Welcome to socle-batch 👋

[![Version](https://img.shields.io/github/v/tag/tisseurdetoile/socle-batch?style=for-the-badge)](https://github.com/tisseurdetoile/socle-batch/releases)
[![MavenCentral](https://img.shields.io/maven-central/v/net.tisseurdetoile.batch/socle-parent?style=for-the-badge)](https://search.maven.org/search?q=g:net.tisseurdetoile.batch)
[![Documentation](https://img.shields.io/badge/documentation-yes-brightgreen.svg?style=for-the-badge)](https://github.com/tisseurdetoile/socle-batch/wiki)
[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-yellow.svg?style=for-the-badge)](LICENCE)
[![Twitter: tisseurdetoile](https://img.shields.io/twitter/follow/tisseurdetoile?label=%40tisseurdetoile&logo=twitter&style=for-the-badge)](https://twitter.com/tisseurdetoile)

> A simple JSON api which launch/stop/monitor your spring-batch. With backward compatibility for spring-batch-admin

A [demo](https://socle-batch.herokuapp.com/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config) is running on heroku

## Build Status

- release : ![Java CI with Maven](https://github.com/tisseurdetoile/socle-batch/workflows/Java%20CI%20with%20Maven/badge.svg?branch=release)
- develop : ![Java CI with Maven](https://github.com/tisseurdetoile/socle-batch/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop)

## Release information

| socle-batch    | JDK |  spring-boot   | spring-batch-core |
| :------------- | :-: | :------------: | :---------------: |
| 0.6.1-SNAPSHOT | 17  |     2.6.1      |       4.3.4       |
| 0.6            | 17  |     2.6.1      |       4.3.4       |
| 0.5            | 11  |     2.4.0      |       4.3.0       |
| 0.4            | 11  | 2.2.11.RELEASE |   4.2.4.RELEASE   |
| 0.3            | 11  | 2.1.14.RELEASE |  4.1.14.RELEASE   |
| 0.2            | 11  | 2.1.2.RELEASE  |   4.1.1.RELEASE   |

## Installation

You can use the [springbatch-example](https://github.com/tisseurdetoile/socle-batch/tree/main/springbatch-example) as a starter project

### In existing spring-batch project

Add this in your pom.xml

```xml
<dependency>
    <groupId>net.tisseurdetoile.batch</groupId>
    <artifactId>springbatch-socle-jsonapi</artifactId>
    <version>0.6</version>
</dependency>
```

Add the _@EnableSpringBatchSocleApi_ annotation in your main class

```java
@SpringBootApplication
@EnableSpringBatchSocleApi
public class SampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }
}
```

## Usage

some minimal command :

- List all job : curl -s -X GET https://socle-batch.herokuapp.com/jobs/
- Launch a Job : curl -s -X POST https://socle-batch.herokuapp.com/jobs/SampleJob.json
- Monitor Job Execution : curl -s -X GET https://socle-batch.herokuapp.com/executions/1.json
- Stop a Job Execution : curl -s -X DELETE https://socle-batch.herokuapp.com/executions/1.json

For more detail see the [springbatch-example](https://github.com/tisseurdetoile/socle-batch/tree/master/springbatch-example) project

## Author

👤 **Le TisseurDeToile**

[![Keybase PGP](https://img.shields.io/keybase/pgp/tisseurdetoile?style=for-the-badge)](https://keybase.io/tisseurdetoile)

- Website: http://www.tisseurdetoile.net
- Twitter: [@tisseurdetoile](https://twitter.com/tisseurdetoile)
- Github: [@tisseurDeToile](https://github.com/tisseurDeToile)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!

Feel free to check [issues page](https://github.com/tisseurdetoile/socle-batch/issues). You can also take a look at the [contributing guide](https://github.com/tisseurdetoile/socle-batch/blob/main/CONTRIBUTING.md).

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2020 [Le TisseurDeToile](https://github.com/tisseurDeToile).

This project is [GPLv3](LICENCE) licensed.

---

_This README was generated with ❤️ by [readme-md-generator](https://github.com/kefranabg/readme-md-generator)_
