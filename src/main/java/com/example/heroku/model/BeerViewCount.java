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
@Table(name="beer_view_count")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeerViewCount {

    @Id
    String id;

    private String beer_id;

    private String device_id;

    private Timestamp createat;
}
