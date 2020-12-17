package com.example.heroku.util;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Util {

    private static Util instance;

    private Util(){
    }

    synchronized public static Util getInstance() {
        if (instance == null)
        {
            instance = new Util();
        }
        return instance;
    }

    public String GenerateID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public String HashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }
}
