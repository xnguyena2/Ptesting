package com.example.heroku.model;

import com.example.heroku.converter.Base64byteConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="IMAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @Column(name="ImgID")
    private String ImgID;


    @Column(name="Content")
    @Convert(converter = Base64byteConverter.class)
    private byte[] content;

    @Column(name="Category")
    private String category;
}
