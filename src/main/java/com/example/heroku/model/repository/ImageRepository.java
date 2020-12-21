package com.example.heroku.model.repository;

import com.example.heroku.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.stream.Stream;

public interface ImageRepository extends JpaRepository<Image, String> {
    Optional<Image> findById(String id);

    @Query(value = "SELECT i.ImgID FROM IMAGE i WHERE i.Category = 'Carousel'", nativeQuery = true)
    Stream<String> getImgID(@Param("carousel") String lastname);
}
