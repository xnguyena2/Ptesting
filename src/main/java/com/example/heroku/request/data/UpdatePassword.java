package com.example.heroku.request.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePassword implements Serializable {
    /**
     *
     */
    //private static final long serialVersionUID = -6986746375915710855L;
    private String username;
    private String oldpassword;
    private String newpassword;
    private String phone_number;
    private String group_id;
    private String title;
    private String roles_in_group;
    private String full_name;
    private String register_code;
    private boolean update_password;
    List<String> roles;
}