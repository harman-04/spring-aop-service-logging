# Spring AOP: Employee Service Logging Project

## Project Overview
This project demonstrates Aspect-Oriented Programming (AOP) using Spring 6 and AspectJ. It separates business logic from cross-cutting concerns by automatically applying logging and exception handling to a service layer without modifying the service code itself.



---

## Technical Specifications

### 1. AspectJ Integration
The project uses `aspectjweaver` to enable proxy-based AOP. The `@EnableAspectJAutoProxy` annotation in the configuration class tells Spring to create proxies for beans that match specified pointcuts.

### 2. Advice Types Implemented

* **@Before**: Executes before the target method starts. Used for initial logging or security checks.
* **@After**: Executes regardless of whether the method succeeds or fails (similar to a finally block).
* **@AfterReturning**: Executes only if the method finishes successfully. It provides access to the returned value.
* **@AfterThrowing**: Executes only if the method throws an exception. Useful for centralized error logging.
* **@Around**: The most powerful advice. It wraps the entire method execution, allowing for pre-processing, execution control via `proceed()`, and post-processing.

---

## File and Class Detailed Reference

### AppConfig.java
* **Purpose**: Java-based configuration for the Spring container.
* **@ComponentScan**: Scans for `@Service` and `@Aspect` components.
* **@EnableAspectJAutoProxy**: Mandatory annotation to enable AOP support.

### EmployeeService.java
* **Purpose**: Contains the core business logic (Add, Delete, Error Simulation).
* **Note**: This class remains completely clean of logging code. It is unaware that an Aspect is watching it.

### EmployeeAspect.java
* **Purpose**: Centralized logging module.
* **Pointcut Expression**: `execution(* com.example.service.*.*(..))`
    * `*`: Any return type.
    * `com.example.service.*`: Any class in this package.
    * `.*(..)`: Any method with any number of parameters.

### MainApp.java
* **Purpose**: Entry point that initializes `AnnotationConfigApplicationContext` and triggers the service methods to demonstrate AOP in action.



---

## Dependencies (pom.xml)

* **spring-aop**: Core Spring AOP framework.
* **aspectjweaver**: Necessary for the AspectJ expression language used in pointcuts.
* **spring-context**: Manages the application lifecycle and dependency injection.
* **Java 25**: Utilized for modern language features and performance.

---

## Execution Flow Analysis

1.  **Context Loading**: Spring scans for `@Aspect` and `@Service` beans.
2.  **Proxy Creation**: Spring creates a Proxy object for `EmployeeService` because it matches the Aspect's pointcut.
3.  **Method Call**: When `service.addEmployee()` is called, the Proxy intercepts the call.
4.  **Advice Execution**: The Proxy executes `@Before`, then the actual method, then `@AfterReturning` and `@After`.

---

## How to Test

1.  **Success Path**: Run the application to see `addEmployee` and `deleteEmployee` logs. Observe how `@Around` wraps the execution.
2.  **Exception Path**: The `throwError()` method triggers the `@AfterThrowing` advice. Observe how the Aspect captures the `RuntimeException` and logs it before the exception reaches the `MainApp`.

## Output Sample
```
[Around-Before] Method: addEmployee
Before method: addEmployee
Adding employee: Ram
[AfterReturning] Method: addEmployee returned: null
After method: addEmployee
[Around-AfterReturning] Method: addEmployee
[Around-After] Method: addEmployee
[Around-Before] Method: deleteEmployee
Before method: deleteEmployee
Deleting employee: Ram
[AfterReturning] Method: deleteEmployee returned: null
After method: deleteEmployee
[Around-AfterReturning] Method: deleteEmployee
[Around-After] Method: deleteEmployee
[Around-Before] Method: throwError
Before method: throwError
[AfterThrowing] Method: throwError throw exception: java.lang.RuntimeException: Simulated exception in EmployeeService
After method: throwError
[Around-AfterThrowing] Method: throwErrorthrew exception: java.lang.RuntimeException: Simulated exception in EmployeeService
Caught exception in MainApp: Simulated exception in EmployeeService

Process finished with exit code 0
```