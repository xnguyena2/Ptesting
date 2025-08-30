package com.example.heroku.jwt;

import com.example.heroku.model.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;

import static java.util.stream.Collectors.joining;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${authentication.auth.cookie}")
    private Boolean fromCookie;

    @Value("${authentication.auth.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${authentication.auth.secret}")
    private String secretKeyTxt;

    @Value("${authentication.auth.timeexpire}")
    private long validityInMs; // 1h

    public static final String HEADER_PREFIX = "Bearer ";
    public static final int HEADER_PREFIX_LENGTH = HEADER_PREFIX.length();

    private static final String AUTHORITIES_KEY = "roles";

    private static final String GROUP_ID_KEY = "group";

    private SecretKey secretKey;

    @PostConstruct
    public void init() throws Exception {
        if(secretKeyTxt == null)
            throw new Exception("Jwt Secret was null!!");
        if(validityInMs <= 0)
            throw new Exception("Jwt validityInMs was 0!!");
        var secret = Base64.getEncoder().encodeToString(secretKeyTxt.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String createToken(String userName, Collection<? extends GrantedAuthority> authorities, String groupID) {
        ClaimsBuilder claims = Jwts.claims().subject(userName);
        if (!authorities.isEmpty()) {
            claims.add(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        if (groupID != null) {
            claims.add(GROUP_ID_KEY, groupID);
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .claims(claims.build())
                .issuedAt(now)
                .expiration(validity)
                .signWith(this.secretKey, Jwts.SIG.HS256)
                .compact();
    }

    private String createToken(String userName, Collection<? extends GrantedAuthority> authorities, String groupID, long validityInMs) {
        ClaimsBuilder claims = Jwts.claims().subject(userName);
        if (!authorities.isEmpty()) {
            claims.add(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        }

        if (groupID != null) {
            claims.add(GROUP_ID_KEY, groupID);
        }

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .claims(claims.build())
                .issuedAt(now)
                .expiration(validity)
                .signWith(this.secretKey, Jwts.SIG.HS256)
                .compact();
    }

    public String createToken(UserDetails user){

        String groupID = null;
        if (user instanceof Users) {
            groupID = ((Users) user).getGroup_id();
        }

        String username = user.getUsername();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return createToken(username, authorities, groupID);
    }

    public String createToken(UserDetails user, long validityInMs){

        String groupID = null;
        if (user instanceof Users) {
            groupID = ((Users) user).getGroup_id();
        }

        String username = user.getUsername();
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return createToken(username, authorities, groupID, validityInMs);
    }

    public String createToken(Authentication authentication) {

        String groupID = null;
        if (authentication.getPrincipal() instanceof Users) {
            groupID = ((Users) authentication.getPrincipal()).getGroup_id();
        }

        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return createToken(username, authorities, groupID);
    }

    public Authentication getAuthentication(String token, int expirationDate) {
        Claims claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();

        Object authoritiesClaim = claims.get(AUTHORITIES_KEY);

        Object groupClaim = claims.get(GROUP_ID_KEY);

        String group_id = null;
        if (groupClaim != null) {
            group_id = groupClaim.toString();
        }

        Collection<? extends GrantedAuthority> authorities = authoritiesClaim == null ? AuthorityUtils.NO_AUTHORITIES
                : AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesClaim.toString());

        Users principal = new Users(claims.getSubject(), "", group_id, authorities, expirationDate);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public int validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token);
            //  parseClaimsJws will check expiration date. No need do here.
            Date expirationDate = claims.getPayload().getExpiration();
            log.info("expiration date: {}", expirationDate);
//            LocalDate today = LocalDate.now();
//            LocalDate localExpiDate = expirationDate.toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate();
//            ChronoUnit.DAYS.between(today, localExpiDate);
            Date now = new Date();
            long diff = expirationDate.getTime() - now.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
//            log.trace("Invalid JWT token trace.", e);
        }
        return -1;
    }


    public String resolveToken(ServerHttpRequest req) {
        if (fromCookie) {
            MultiValueMap<String, HttpCookie> cookies = req.getCookies();
            HttpCookie cookie = cookies.getFirst(accessTokenCookieName);
            if(cookie == null)
                return null;
            return cookie.getValue();
        } else {
            String bearerToken = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
                return bearerToken.substring(HEADER_PREFIX_LENGTH);
            }
        }
        return null;
    }
}
