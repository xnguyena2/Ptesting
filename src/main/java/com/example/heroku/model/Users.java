package com.example.heroku.model;


import entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Data
@SuperBuilder
@NoArgsConstructor
@Table(value = "users")
public class Users extends BaseEntity implements UserDetails, CredentialsContainer {

    @Id
    String id;

    private String username;

    private String password;

    private String createby;

    @Transient
    private UserDetails user;

    public Users(String username, String password, String group_id, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, group_id, true, authorities);
    }

    public Users(String username, String password, String group_id, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        if (username != null && !"".equals(username) && password != null) {
            this.username = username;
            this.password = password;
            this.active = enabled;
            this.group_id = group_id;
            this.user = User
                    .withUsername(username)
                    .password(password)
                    .authorities(authorities)
                    .accountExpired(!isActive())
                    .credentialsExpired(!isActive())
                    .disabled(!isActive())
                    .accountLocked(!isActive())
                    .build();
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public Users parseauthorities() {
        this.user = User
                .withUsername(username)
                .password(password)
                .authorities(getRoles().toArray(new String[0]))
                .accountExpired(!isActive())
                .credentialsExpired(!isActive())
                .disabled(!isActive())
                .accountLocked(!isActive())
                .build();
        return  this;
    }

    public boolean equals(Object rhs) {
        return rhs instanceof Users && this.username.equals(((Users) rhs).username);
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        if (user == null) {
            return null;
        }
        return user.toString();
    }

    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user == null) {
            return null;
        }
        return user.getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Builder.Default()
    private boolean active = true;

    @CreatedDate
    private Timestamp createat;

    public List<String> getRoles() {
        return roles.stream().map(x -> x.replace("{","").replace("}","")).collect(Collectors.toList());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
