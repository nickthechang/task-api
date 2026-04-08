//main class on top
package com.nick.taskapp;


//maven downloaded org.springframework.boot. two imports from that.

//used for SpringApplication.run
import org.springframework.boot.SpringApplication;
//used for @SpringBootApplication
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Entry point of your app. @Configuration; @EngableAuttoConfiguration;@ComponentScan -> Scan this package and everyting below it. package = com.nick.taskapp
@SpringBootApplication
public class TaskappApplication {
	//maven runs main method	
	public static void main(String[] args) {
		//BTS creates Application Context scans project for @restcontroller, @service, etc. Creates those objects(beans) wires them together(DI) and starts the webserver(Tomcat)
		SpringApplication.run(TaskappApplication.class, args);
	}

}


/*
Java runs this first and then spring takes over. Spring scans the project because of @SpringBootApplication:scan this package and everything inside it. 
looks at controller, service, model and it looks for @restcontroller, @service, @repository. 
does not go to one file specifically. it prepares everyting first then waits for a request.
*/