package ru.abramov.blog.configs;

import lombok.Getter;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan(basePackages = {
    "ru.abramov.blog.services",
    "ru.abramov.blog.repositories",
})
@Import({DataSourceConfig.class})
@Getter
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        configurer.setIgnoreUnresolvablePlaceholders(false);
        configurer.setEnvironment(new StandardEnvironment());
        return configurer;
    }
}