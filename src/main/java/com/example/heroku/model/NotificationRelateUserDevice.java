package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="notification_relate_user_device")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRelateUserDevice {
    @Id
    String id;

    private String notification_second_id;

    private String user_device_id;

    private Timestamp createat;

    private Status status;

    public enum Status{

    }

}
