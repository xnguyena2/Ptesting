package com.example.heroku.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@Slf4j
@RequiredArgsConstructor
public class JwtTokenAuthenticationFilter implements WebFilter {

    public static final List<String> permitAllPaths = Arrays.asList(
            "/address/**",
            "/beer/getall",
            "/beer/search",
            "/beer/category",
            "/beer/detail/**",
            "/clientdevice/bootstrap/*",
            "/clientdevice/bootstrapfull/*",
            "/clientdevice/bootstrapfullbenifitcurrentdate/*",
            "/order/create",
            "/package/add",
            "/package/update",
            "/package/updatenotcheckwithtransacction",
            "/package/remove",
            "/package/getbydevice",
            "/package/getmyworkiung",
            "/package/getmypackageprocessing",
            "/package/getmypackagereturn",
            "/package/getbydeviceforweb",
            "/package/getbyid",
            "/package/getbyjustid",
            "/package/getbygroup",
            "/package/getbygrouponlywithbuyer",
            "/package/countprocessingandweb/*",
            "/package/getwokingbygroup",
            "/package/searchwokingbygroup",
            "/package/getbygroupstatus",
            "/package/buyerfromwebsubmit",
            "/package/return",
            "/package/cancel",
            "/package/clean",
            "/package/getbystatusbetween",
            "/package/getbystatusproduct",
            "/auth/signin",
            "/assets/**",
            "/*");

    public static final List<String> authenPaths = Arrays.asList(
            "/auth/me",
            "/auth/account/update");

    public static final List<String> rootPaths = List.of("/root/**");

    public static final List<String> adminPaths = List.of("/*/admin/**");

    private final List<String> mustValid = Stream.of(authenPaths, rootPaths, adminPaths)
            .flatMap(List::stream)
            .toList();

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final JwtTokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        boolean isPermitAll = permitAllPaths.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
        if (isPermitAll) {
            log.info("Permit all path: {}", path);
            return chain.filter(exchange); // Proceed without authentication/authorization
        } else {
            String token = this.tokenProvider.resolveToken(request);
            if (StringUtils.hasText(token)) {
                int expirationDate = this.tokenProvider.validateToken(token);
                Authentication authentication = this.tokenProvider.getAuthentication(token, expirationDate);
                return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

            }
            return chain.filter(exchange);
        }
    }
}
