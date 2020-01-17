# jmsapp1
### How to launch project from command line interface
Check maven installed on your PC, then run:
1. `mvn clean test` - this command performs JmsApp1Test unit-test;
2. `mvn clean spring-boot:run` - this command launches spring boot rest application. Embedded Tomcat server will be started on port 8080 by default.
You can use `curl` for testing api
To add records:
`curl -i -X POST -H 'Content-type:application/json' -d '{"message":"First Message"}' http://localhost:8080/api/messages`
The same way use to add others.
To delete a record use:
`curl -i -X DELETE -H 'Content-type:application/json'-d '{"message":"First Message"}' http://localhost:8080/api/messages`
You can inspect `PHMessageController.java` to discover all api functions for this project.
