package ru.abramov.blog.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "ru.abramov.blog.services",
    "ru.abramov.blog.repositories",
})
public class AppConfig {
}