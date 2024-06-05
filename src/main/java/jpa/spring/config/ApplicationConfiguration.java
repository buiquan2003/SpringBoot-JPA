package jpa.spring.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfiguration {

    @Bean
    public FilterRegistrationBean<Manager> loggingFilter() {
        FilterRegistrationBean<Manager> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new Manager());
        registrationBean.addUrlPatterns("/api/**");

        return registrationBean;
    }
}
