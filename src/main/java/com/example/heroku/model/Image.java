package com.example.heroku.model;

import com.example.heroku.converter.Base64byteConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="IMAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    String id;

    @Column(name="Content")
    private byte[] content;

    @Column(name="Category")
    private String category;

    @Column(name="Createat")
    private LocalDateTime createat;
}
