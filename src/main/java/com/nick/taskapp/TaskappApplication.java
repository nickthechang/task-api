package com.nick.taskapp;

//used for SpringApplication.run
import org.springframework.boot.SpringApplication;
//used for @SpringBootApplication
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * Entry point of the Spring Boot application.
 *
 * @SpringBootApplication is required to run a Spring Boot app.
 * It combines:
 * - component scanning (finds controllers, services, etc.)
 * - auto-configuration (sets up Spring based on dependencies)
 *
 * Spring starts scanning from this package (com.nick.taskapp)
 * and registers all beans needed for the application.
 */
@SpringBootApplication
public class TaskappApplication {
	/*
     * Starts the application by creating the Spring context
     * and launching the embedded web server.
     */
	public static void main(String[] args) {
		SpringApplication.run(TaskappApplication.class, args);
	}

}


/*
Java runs this first and then spring takes over. Spring scans the project because of @SpringBootApplication:scan this package and everything inside it. 
looks at controller, service, model and it looks for @restcontroller, @service, @repository. 
does not go to one file specifically. it prepares everyting first then waits for a request.
*/


/*
Entry point of the entire backend. When running the app this is the file Java starts with.
No business logic.
Job: 
Start SpringBoot
create application context
scan for components
wire dependencies together start the embedded webserver

Connects everything indirectly. Doesn't call TaskController or TaskService Manually
@SpringBootApplication tells Spring to scan the package: com.nick.taskapp and everyting under it meaning it'll find all the @ annotations.
Then Spring creates and wires the beans together.

Builds app then waits for a request. Startup layer.
*/