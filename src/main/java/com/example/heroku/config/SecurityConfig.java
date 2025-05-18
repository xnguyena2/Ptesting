package com.example.heroku.config;


import com.example.heroku.jwt.JwtTokenAuthenticationFilter;
import com.example.heroku.jwt.JwtTokenProvider;
import com.example.heroku.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    @Value("${spring.profiles.active}")
    private String runningMode;

    ServerHttpSecurity.AuthorizeExchangeSpec corsConfig(ServerHttpSecurity.AuthorizeExchangeSpec it) {
        System.out.println("**************************************RUN IN MODE: "+this.runningMode + "**************************************");
        if (runningMode != null && runningMode.equals("dev")) {
            return
                    it
//                            .pathMatchers("/*/admin/**").hasAnyRole("ROOT", "ADMIN")
//                            .pathMatchers("/*/admin/*/**").hasAnyRole("ROOT", "ADMIN")
//                            .pathMatchers("/*/admin/*/*/**").hasAnyRole("ROOT", "ADMIN")
//                            .pathMatchers("/auth/account/update").authenticated()

                            .anyExchange().permitAll();
        }

        for (String path : JwtTokenAuthenticationFilter.authenPaths) {
            it = it.pathMatchers(path).authenticated();
        }

        for (String path : JwtTokenAuthenticationFilter.rootPaths) {
            it = it.pathMatchers(path).hasAnyRole("ROOT");
        }

        for (String path : JwtTokenAuthenticationFilter.adminPaths) {
            it = it.pathMatchers(path).hasAnyRole("ROOT", "ADMIN");
        }

        for (String path : JwtTokenAuthenticationFilter.permitAllPaths) {
            it = it.pathMatchers(path).permitAll();
        }
        return it
                //.pathMatchers(HttpMethod.DELETE, PATH_POSTS).hasRole("ADMIN")
                //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)

                .anyExchange().authenticated();
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(this::corsConfig)
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
                    return u.parseauthorities();
//                    return User
//                            .withUsername(u.getUsername()).password(u.getPassword())
//                            .authorities(u.getRoles().toArray(new String[0]))
//                            .accountExpired(!u.isActive())
//                            .credentialsExpired(!u.isActive())
//                            .disabled(!u.isActive())
//                            .accountLocked(!u.isActive())
//                            .build();
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

