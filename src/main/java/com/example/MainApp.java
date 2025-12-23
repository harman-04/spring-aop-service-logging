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


        System.out.println("--- TEST 1: SUCCESS SCENARIO (VOID METHOD) ---");
        // Call the addEmployee method to add employee
        service.addEmployee("Ram");


        System.out.println("\n--- TEST 2: SUCCESS SCENARIO (RETURN VALUE) ---");
        // This will trigger @AfterReturning with the result "Active"
        String status = service.checkStatus("EMP101");
        System.out.println("MainApp received status: " + status);

        // Call the deleteEmployee for delete employee
        service.deleteEmployee("Ram");

        System.out.println("\n--- TEST 3: EXCEPTION SCENARIO ---");
        // Trigger exception to see @AfterThrowing and @Around exception handling
        try {
            service.throwError();
        } catch (Exception e) {
            System.out.println("Caught exception in MainApp: " + e.getMessage());
        }

        context.close();
    }
}
