


## Task API - REST API for user management
### Project description
This project consists of a REST API developed with Spring Boot for the management of user Tasks, focusing on security and authentication using Spring Security. Using Java as the main language, this application offers robustness and dynamism, in line with the latest versions of Java. Dependency management is done through Gradle.
#### Configuration and Installation
##### Previous requirements
Java (Recommended version: 17)
Spring Boot (Recommended version: 3)
Gradle
IDE (developed in IntelliJ IDEA)

#### Installation and Execution
##### Clone the repository:
`git clone https://github.com/darwgom7/task-api.git`

##### Compile the project with Gradle:
`gradle build`

##### Run the application:
`gradle bootRun`

The application will run normally on port 8080.

#### API documentation

##### Login
`POST /api/users/login`
###### Request example:
```json
{
  "email": "jeo@one.org",
  "password": "HunterOne2+"
}
```
##### Register task
`POST /api/tasks`
###### Request example:
```json
{
  "title": "Complete Project",
  "completed": true
}
```
##### Get Tasks
`GET /api/tasks`

##### Get Task by ID
`GET /api/tasks/{id}`

##### Update Tasks
`PATCH /api/tasks/{id}`

##### Delete Task
`DELETE /api/tasks/{id}`
