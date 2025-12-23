package com.example.service;


import org.springframework.stereotype.Service;

@Service

// It is a EmployeeService class that define service method.
public class EmployeeService {

    // method for adding employee
    public void addEmployee(String name) {
        System.out.println("Adding employee: " + name);
    }

    // method for delete employee
    public void deleteEmployee(String name){
        System.out.println("Deleting employee: " + name);
    }


//     method for demonstrate the exceptional handling
    public void throwError(){
        throw new RuntimeException("Simulated exception in EmployeeService");
    }



}
