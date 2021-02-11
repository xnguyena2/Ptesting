package com.example.heroku.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = this.tokenProvider.resolveToken(exchange.getRequest());
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {
            Authentication authentication = this.tokenProvider.getAuthentication(token);
            return chain.filter(exchange)
                    .subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
        }
        System.out.println("Hello");
        return chain.filter(exchange)
                .onErrorResume(AuthenticationException.class,
                        e ->
                                new ServerAuthenticationEntryPointFailureHandler(
                                        new RedirectServerAuthenticationEntryPoint("/login")
                                )
                                        .onAuthenticationFailure(new WebFilterExchange(exchange, chain), e)
                );
    }
}
