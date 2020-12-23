package com.example.heroku.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresqlConfig {
    /* waitting 2.4
    @Bean
    ReactiveAuditorAware<User> reactiveAuditorAware() {
        System.out.println("Get auditor");
        return () -> ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast);
    }
    */
}