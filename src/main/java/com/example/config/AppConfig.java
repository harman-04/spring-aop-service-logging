package com.example.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration

@ComponentScan(basePackages = "com.example")
// This annotation scan all spring components (beas, services, aspects).

@EnableAspectJAutoProxy
// This is used to enable support for handling components marked with @Aspect
public class AppConfig {
}
