# Spring AOP: Employee Service Logging Project

## Project Overview
This project demonstrates Aspect-Oriented Programming (AOP) using Spring 6 and AspectJ. It separates business logic from cross-cutting concerns by automatically applying logging and exception handling to a service layer without modifying the service code itself.



---

## Technical Specifications

### 1. AOP Implementation Styles
* **Annotation-Based**: Uses `@Aspect` and `@Before/@After` annotations directly within Java classes for fast development and high readability.
* **XML-Based**: Uses `<aop:config>` in an external XML file to map advice to business logic, allowing for a strict separation of concerns without modifying Java source code.

### 2. AspectJ Integration
The project uses `aspectjweaver` to enable proxy-based AOP. The `@EnableAspectJAutoProxy` annotation in the configuration class tells Spring to create proxies for beans that match specified pointcuts.

### 3. Advice Types Implemented

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
* **Purpose**: Centralized logging module. In XML mode, Spring ignores the annotations and uses the raw methods (`logBefore`, `logAround`, etc.) by their names.
* **Pointcut Expression**: `execution(* com.example.service.*.*(..))`
    * `*`: Any return type.
    * `com.example.service.*`: Any class in this package.
    * `.*(..)`: Any method with any number of parameters.

### MainApp.java
* **Purpose**: Entry point that initializes `AnnotationConfigApplicationContext` and triggers the service methods to demonstrate AOP in action.

### app-config.xml (XML Approach)
* **Purpose**: Declarative AOP configuration. It defines beans and maps method names to pointcut expressions.
* **Key Tags**:
    * `<aop:aspectj-autoproxy />`: Enables AOP support.
    * `<aop:pointcut>`: Defines a reusable expression for targeting methods.
    * `<aop:aspect>`: References the Aspect bean and contains the advice mappings.


---
## XML Configuration Details
To run the project in XML mode, the following configuration is used in `src/main/resources/applicationContext.xml`:

```xml
<aop:config>
    <aop:pointcut id="serviceMethods" expression="execution(* com.example.service.*.*(..))" />
    <aop:aspect id="loggingAspect" ref="employeeAspectBean">
        <aop:around method="logAround" pointcut-ref="serviceMethods" />
        <aop:before method="logBefore" pointcut-ref="serviceMethods" />
        <aop:after method="logAfter" pointcut-ref="serviceMethods" />
        <aop:after-returning method="logAfterReturning" pointcut-ref="serviceMethods" returning="result" />
        <aop:after-throwing method="logAfterThrowing" pointcut-ref="serviceMethods" throwing="error" />
    </aop:aspect>
</aop:config>
```
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

## Advice Execution Hierarchy

When multiple advice types are applied to a single method, Spring AOP follows a specific execution order:

1. **@Around (Part 1)**: The code before `joinPoint.proceed()`.
2. **@Before**: Executes immediately before the method.
3. **The Target Method**: The actual business logic runs.
4. **@Around (Part 2)**: The code after `joinPoint.proceed()`.
5. **@After**: Executes regardless of outcome (Success or Exception).
6. **@AfterReturning**: (Success only) Captures the result.
7. **@AfterThrowing**: (Exception only) Captures the error.
---

## How to Test

1.  **Success Path**: Run the application to see `addEmployee` and `deleteEmployee` logs. Observe how `@Around` wraps the execution.
2.  **Exception Path**: The `throwError()` method triggers the `@AfterThrowing` advice. Observe how the Aspect captures the `RuntimeException` and logs it before the exception reaches the `MainApp`.

## Output Sample
```
--- TEST 1: SUCCESS SCENARIO (VOID METHOD) ---
[Around-Before] Method: addEmployee
Before method: addEmployee
Adding employee: Ram
[AfterReturning] Method: addEmployee returned: null
After method: addEmployee
[Around-AfterReturning] Method: addEmployee
[Around-After] Method: addEmployee

--- TEST 2: SUCCESS SCENARIO (RETURN VALUE) ---
[Around-Before] Method: checkStatus
Before method: checkStatus
Checking status for: EMP101
[AfterReturning] Method: checkStatus returned: Active
After method: checkStatus
[Around-AfterReturning] Method: checkStatus
[Around-After] Method: checkStatus
MainApp received status: Active
[Around-Before] Method: deleteEmployee
Before method: deleteEmployee
Deleting employee: Ram
[AfterReturning] Method: deleteEmployee returned: null
After method: deleteEmployee
[Around-AfterReturning] Method: deleteEmployee
[Around-After] Method: deleteEmployee

--- TEST 3: EXCEPTION SCENARIO ---
[Around-Before] Method: throwError
Before method: throwError
[AfterThrowing] Method: throwError throw exception: java.lang.RuntimeException: Simulated exception in EmployeeService
After method: throwError
[Around-AfterThrowing] Method: throwErrorthrew exception: java.lang.RuntimeException: Simulated exception in EmployeeService
Caught exception in MainApp: Simulated exception in EmployeeService

Process finished with exit code 0

```