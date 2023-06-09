# Employee Service

The employee service to get, create, delete and update employees.

### Setup

#### Requirements

- Docker
- Gradle
- Java 17
- Intellij IDEA

#### 1. Run dockerized services

The project uses Postgresql and Apache Kafka as docker containers. It takes about 3 to 5 minutes for all services to run on a computer.

Simply use:

 ```sh
$ docker-compose up  #to run containerized services
 ```

#### 2. REST endpoints with Swagger UI

The employee endpoints can be monitored using `http://localhost:8080/swagger-ui/index.html`.

##### Get Endpoints

The list of employees can be retrieved using the Get request section of Swagger UI. The get request does not require authentication.

![Swagger_UI_GET](assets/get_request.png)

The specific employee can be retrieved by id as shown below.

![Swagger_UI_GETBYID](assets/get_by_id.png)

#### 3. Authentication for Create, Update and Delete Endpoints with Spring Security and JWT Token

The create, update and delete requests require authentication.

![Swagger_UI_other_requests](assets/authenticated_requests.png)

To authorize create, update and delete endpoints:

Firstly, first name, last name, email and password should be entered for registration. 

![Swagger_UI_other_requests](assets/register.png)

Secondly, The provided email and password should be entered to below section to get JWT token.

![Swagger_UI_other_requests](assets/authentication.png)

After this operation, the token will be generated and entered to this section.

![Swagger_UI_authorization](assets/authorization.png)

The employee can be created as below after authorization. After creating employee, update and delete operation can be done by employee id.

![Swagger_UI_post](assets/post.png)

#### 4. Kafka Implementation

Kafka Cluster and related components can be monitored and managed by Landoop UI at `http://localhost:3030/`. This UI helps us to see topics, schemas and connectors.

![Kafka Landoop UI](assets/landoop_ui.png)

In create and update endpoints, the created or updated employee is written to created kafka topic. Kafka topic can be easily seen in the Landoop UI.

#### 5. Exception(Error) Handling for Endpoints
Spring Boot provides a good implementation for exception handling for RESTful Services. **@ControllerAdvice** is a specialization of the @Component annotation which allows to handle exceptions across the whole application in one handling component.

The email field is unique so same email cannot be assigned to new or updated employee. It handles by **@ControllerAdvice**.

![Swagger_UI_email](assets/duplicate_email_issue.png)

The employee not found is handled by **@ControllerAdvice**.

![Swagger_UI_email](assets/employee_not_found.png)

#### 6. Stop and Remove Containers

Simply use:

 ```sh
$ docker-compose down
 ```

#### 7. Coulda/Woulda/Shoulda

- I could have implemented more integration and unit tests with separate directory for unit tests and integration tests.
- I would have used **@PreAuthorize** annotation to manage authentication of specific endpoints.
- I would have improved kafka implementation.
- I would have added tests for security and kafka implementation.
- I would have implemented front-end part to enhance user experience.

##### Additional Notes
- Secret key was added in JwtServices for local tests. In real world project it should be stored in the Environment Variable and injected to remote machines while deploying the service.
