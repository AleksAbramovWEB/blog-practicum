package ru.abramov.blog.test.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.abramov.blog.configs.WebConfig;

@Configuration
@Import(value = {TestDataSourceConfig.class, WebConfig.class})
@ComponentScan(basePackages = {
        "ru.abramov.blog.services",
        "ru.abramov.blog.repositories",
})
public class TestConfig {

    @Bean
    public MockMvc mockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties.test"));
        configurer.setIgnoreUnresolvablePlaceholders(false);
        configurer.setEnvironment(new StandardEnvironment());
        return configurer;
    }
}
