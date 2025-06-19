package com.example.heroku.model.entity;

import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    protected Long id;


    protected String group_id;
    protected LocalDateTime createat;

    public BaseEntity(BaseEntity s) {
        id = s.getId();
        group_id = s.getGroup_id();
        createat = s.getCreateat();
    }

    public void copy(BaseEntity b) {
        id = b.id;
        group_id = b.group_id;
        createat = b.createat;
    }

    public BaseEntity AutoFill() {
        this.createat = Util.getInstance().Now();
        return this;
    }

    public BaseEntity AutoFillIfNull() {
        if (this.createat == null) {
            this.createat = Util.getInstance().Now();
        }
        return this;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createat == null) {
            this.createat = Util.getInstance().Now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o))
            return false;
        BaseEntity that = (BaseEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return ProxyUtils.getUserClass(this).hashCode();
    }
}
