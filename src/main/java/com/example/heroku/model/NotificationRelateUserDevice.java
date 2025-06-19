package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="notification_relate_user_device")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRelateUserDevice extends BaseEntity {

    private String notification_second_id;

    private String user_device_id;

    private LocalDateTime createat;

    private Status status;

    public enum Status{

    }

}
