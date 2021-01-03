package com.example.heroku.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="search_token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchToken {
    @Id
    String id;

    private String beer_second_id;
}
