package com.example.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
// Marks this class as an Aspect for enable AOP features.

@Component
// Indicate that this class as a Spring-managed bean
public class EmployeeAspect {

    // Reusable pointcut for all methods in the service package
    @Pointcut("execution(* com.example.service.*.*(..))")
    public void serviceMethods(){}

    // 1. Before advice
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }

    // 2. After advice
    @After("serviceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("After method: " + joinPoint.getSignature().getName());
    }

    // 3. AfterReturning advice
    @AfterReturning(pointcut = "serviceMethods()" , returning = "result")
    public void logAfterReturning(JoinPoint joinPoint , Object result) {
        System.out.println("[AfterReturning] Method: " + joinPoint.getSignature().getName() + " returned: " + result);
    }

    // 4. AfterThrowing advice
    @AfterThrowing(pointcut = "serviceMethods()" , throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint , Throwable error) {
        System.out.println("[AfterThrowing] Method: " + joinPoint.getSignature().getName() + " throw exception: " + error);
    }

    // 5. Around advice
    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("[Around-Before] Method: " + joinPoint.getSignature().getName());
        Object result = null;
        try {
            result = joinPoint.proceed(); // proceed with method execution
            System.out.println("[Around-AfterReturning] Method: " + joinPoint.getSignature().getName());
        } catch (Throwable e) {
            System.out.println("[Around-AfterThrowing] Method: " + joinPoint.getSignature().getName() + "threw exception: " + e);
            throw e;
        }
        System.out.println("[Around-After] Method: " + joinPoint.getSignature().getName());
        return result;
    }
}
