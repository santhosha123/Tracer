package com.example.microserviceone.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.ram")
@Import({MessagingConfiguration.class})
public class AppConfig {
}
