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
    List<String> Roles;
}