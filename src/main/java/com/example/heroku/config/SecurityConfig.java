package com.example.heroku.config;


import com.example.heroku.jwt.JwtTokenAuthenticationFilter;
import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.repository.UserRepository;
import lombok.var;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        //.pathMatchers(HttpMethod.DELETE, PATH_POSTS).hasRole("ADMIN")
                        //.pathMatchers("/me").authenticated()
                        //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)

                        //for admin
                        .pathMatchers("/**/admin/**").hasRole("ADMIN")
                        .pathMatchers("/**/admin/**/**").hasRole("ADMIN")
                        .pathMatchers("/**/admin/**/**/**").hasRole("ADMIN")

                        .pathMatchers("/").permitAll()
                        .pathMatchers("/address/**").permitAll()
                        .pathMatchers("/beer/**").permitAll()
                        .pathMatchers("/clientdevice/**").permitAll()
                        .pathMatchers("/deviceconfig/**").permitAll()
                        .pathMatchers("/order/**").permitAll()
                        .pathMatchers("/package/**").permitAll()

                        .pathMatchers("/angular/**").permitAll()
                        .pathMatchers("/angular/asset/img/**").permitAll()
                        .pathMatchers("/adminag/**").permitAll()

                        .pathMatchers("/auth/signin").permitAll()

                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)

                /*
                //may be remove later
                .exceptionHandling()
                .authenticationEntryPoint((ServerAuthenticationEntryPoint) (exchange, e) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return Mono.empty();
                })
                .and()
                ///////////////////////
                 */

                .build();
    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
                                                               AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }

    @Bean
    public ReactiveUserDetailsService userDetailsService(UserRepository users) {

        return username -> users.findByUsername(username)
                .map(u -> {
                    u.setPassword(u.getPassword().trim());
                    return User
                            .withUsername(u.getUsername()).password(u.getPassword())
                            .authorities(u.getRoles().toArray(new String[0]))
                            .accountExpired(!u.isActive())
                            .credentialsExpired(!u.isActive())
                            .disabled(!u.isActive())
                            .accountLocked(!u.isActive())
                            .build();
                        }
                );
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService,
                                                                       PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

}

