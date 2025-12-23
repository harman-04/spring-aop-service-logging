package com.example;
import com.example.service.EmployeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import com.example.config.AppConfig;

// Main class
public class MainApp {
    static void main() {

        // Create and initialize the context using the Java-based configuration

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


        // Retrieving the EmployeeService bean from the Spring Container
        EmployeeService service = context.getBean(EmployeeService.class);

        // Call the addEmployee method to add employee
        service.addEmployee("Ram");

        // Call the deleteEmployee for delete employee
        service.deleteEmployee("Ram");

        // Trigger exception to see @AfterThrowing and @Around exception handling
        try {
            service.throwError();
        } catch (Exception e) {
            System.out.println("Caught exception in MainApp: " + e.getMessage());
        }

        context.close();
    }
}
