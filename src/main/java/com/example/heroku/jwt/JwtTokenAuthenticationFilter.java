package com.example.heroku.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
        if (StringUtils.hasText(token)) {
            int expirationDate = this.tokenProvider.validateToken(token);
            Authentication authentication = this.tokenProvider.getAuthentication(token, expirationDate);
            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        }
        return chain.filter(exchange);
    }
}
