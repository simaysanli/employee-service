# Employee Service

The aim of this project is to provide get, create, delete and update employees.

### Setup

#### Requirements

- Docker
- Gradle
- JDK -> Java Corretto 17
- Intellij IDEA

##### 1 Run dockerized services

The project uses Postgresql and Apache Kafka as docker containers. It takes about 3 to 5 minutes for all services to run on a computer.

Simply use:

 ```sh
$ docker-compose up  #to run containerized services
$ docker-compose down
 ```
 
 Kafka Cluster and related components can be monitored and managed by Landoop UI at `http://localhost:3030/`. This UI helps us to see topics, schemas and connectors.


![Kafka Landoop UI]()
