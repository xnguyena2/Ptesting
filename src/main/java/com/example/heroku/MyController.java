package com.example.heroku;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;

@RestController
public class MyController {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Autowired
    private DataSource dataSource;

    class Myobject{
        String firstName;
        String lastName;

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastName() {
            return lastName;
        }

        public Myobject(String firstName, String lastName){
            setFirstName(firstName);
            setLastName(lastName);
        }
    }
    @RequestMapping("/obj")
    Myobject obj() {
        System.out.println("get obj");
        return new Myobject("abc", "fish");
    }

    @GetMapping(
            value = "/img",
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public @ResponseBody
    byte[] getImageWithMediaType() throws IOException {
        System.out.println("get img");
        String name = "iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAABmJLR0QA/wD/AP+gvaeTAAAAqElEQVRIie3UsQnCUBRA0ROxtXYCB9ABHEXF0kIQ3cPCxtpFxDEUHcDSCWKRKB/FQnwJFrnF5xPCvfAfPBr+jSy5z9AJdN+wTSM9HLALjIwwxBlWOGEcGIBJ6V1Bjn5w4EG/9BdHheStigOg/an+gzN7/fAp8vbjL9TyXM1MvqLWmQwq8j+9SxwVuyaSaeldplt4j01gZK7Ywpd0wAt0AyNXrAN9DYHcAR/nF5iD9JUSAAAAAElFTkSuQmCC";
        byte[] in = Base64.getDecoder().decode(new String(name).getBytes("UTF-8"));
        /*
        InputStream in = getClass()
                .getResourceAsStream("/images/icons8-category-25.png");*/
        return in;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> handleUpload(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        try {
            System.out.println("Is Multiple Part: " + isMultipart);

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            ServletFileUpload upload = new ServletFileUpload();
            FileItemIterator iterStream = upload.getItemIterator(request);
            System.out.println("Get file Content");
            while (iterStream.hasNext()) {
                System.out.println("next");
                FileItemStream item = iterStream.next();
                String name = item.getFieldName();

                System.out.println(name);

                IOUtils.copy(item.openStream(), output);
            }

            System.out.println("DOne");
            String encoded = Base64.getEncoder().encodeToString(output.toByteArray());
            System.out.println(encoded);

        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/uploadml")
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try (Connection connection = dataSource.getConnection()){
            String encoded = Base64.getEncoder().encodeToString(file.getBytes());

            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS IMAGE (ImgID AUTOINCREMENT PRIMARY KEY, Content TEXT)");
            //ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
            mapSqlParameterSource.addValue("content", encoded);
            jdbcTemplate.update("INSERT INTO IMAGE (Content) VALUES (:content)",mapSqlParameterSource);

            //storageService.save(file);
            //System.out.println(encoded);
            //message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return  ResponseEntity.badRequest().build();
        }
    }
}