# socle-batch

A simple JSON api for launching/monitoring you spring batch. 

## Build Status

- release 0.3 : ![Java CI with Maven](https://github.com/tisseurdetoile/socle-batch/workflows/Java%20CI%20with%20Maven/badge.svg?branch=release)
- develop 0.4-SNAPSHOT : ![Java CI with Maven](https://github.com/tisseurdetoile/socle-batch/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop)
    
## Installation

You can use the [springbatch-example](https://github.com/tisseurdetoile/socle-batch/tree/master/springbatch-example) as a starter project

### In existing spring-batch project

Add this in your pom.xml

```maven
       <dependency>
           <groupId>net.tisseurdetoile.batch</groupId>
           <artifactId>springbatch-socle-jsonapi</artifactId>
           <version>0.3</version>
       </dependency>
```


Add the @EnableSpringBatchSocleApi annotation in your main class

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

- List all job : curl -X GET http://localhost:8080/jobs/ 
- Launch a Job : curl -X POST http://localhost:8080/jobs/SampleJob.json
- Monitor Job Execution :  curl -X GET http://localhost:8080/executions/1.json
- Stop a Job Execution : curl -X DELETE http://localhost:8080/executions/1.json

For more detail see the [springbatch-example](https://github.com/tisseurdetoile/socle-batch/tree/master/springbatch-example) project

## Version information matrix

| socle-batch   |  JDK  |   spring |
| ------------- |: ---: | -------: |
| 0.4-SNAPSHOT  | 14    |  2.2.6   |
| 0.3           | 11    |  2.1.14  |
| 0.2           | 11    |  2.1.2   |

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

[GPL](https://choosealicense.com/licenses/gpl-3.0/)

## Usefull tool

- [makeAReadme](https://www.makeareadme.com/)
