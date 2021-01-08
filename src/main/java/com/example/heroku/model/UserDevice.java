package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="user_device")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDevice {

    @Id
    String id;

    private String device_id;

    private String user_first_name;

    private String user_last_name;

    private Status status;

    private Timestamp createat;

    public UserDevice AutoFill(){
        this.createat = new Timestamp(new Date().getTime());
        return this;
    }

    public enum Status {
        ACTIVE,
        BLOCK
    }

}
