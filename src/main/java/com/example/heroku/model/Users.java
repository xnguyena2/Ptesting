package com.example.heroku.model;


import com.example.heroku.request.permission.GetGroupID;
import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;


@Data
@SuperBuilder
@NoArgsConstructor
@Table(value = "users")
public class Users extends BaseEntity implements UserDetails, CredentialsContainer, GetGroupID {

    private String username;

    private String password;

    private String createby;

    protected String phone_number;

    protected String phone_number_clean;

    private Status status;


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
        correctField();
        this.active = status == Status.ACTIVE;
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

    public Users correctField() {
        if(this.status == null) {
            this.status = Status.ACTIVE;
        }
        return this;
    }

    public Users AutoFill() {
        super.AutoFill();
        this.phone_number_clean = Util.CleanPhoneNumber(phone_number);
        return this;
    }

    public Users Clean() {
        password = null;
        return this;
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

    public List<String> getRoles() {
        return roles.stream().map(x -> x.replace("{","").replace("}","")).collect(Collectors.toList());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
    @JsonIgnore
    public Map<String, Object> getJsonObject() {
        return new HashMap<String, Object>() {
            {
                put("username", getUsername());
                put("roles", AuthorityUtils.authorityListToSet(getAuthorities()));
                put("group_id", getGroup_id());
                put("phone_number", getPhone_number());
            }
        };
    }

    public enum Status {
        ACTIVE("ACTIVE"),
        DELETE("DELETE"),
        DISABLE("DISABLE");


        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static {
            for (Status sts : Status.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text) {
            try {
                Status val = lookup.get(text);
                if (val == null) {
                    return ACTIVE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ACTIVE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
