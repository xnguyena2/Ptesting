package com.example.heroku.model.joinwith;

import com.example.heroku.model.Users;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinUserInfo extends Users {


    // UsersInfo

    protected String child_username;
    protected String user_fullname;
    private String phone;
    private String title;
    private String client_roles;
    protected LocalDateTime child_createat;

    private int token_expiration;


    public UserJoinUserInfo Clean() {
        super.Clean();
        return this;
    }
}
