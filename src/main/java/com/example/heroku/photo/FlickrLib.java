package com.example.heroku.photo;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.github.scribejava.core.model.OAuth1RequestToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlickrLib {
    @Value("${photo.flickr.key}")
    private String flickKey;

    @Value("${photo.flickr.secret}")
    private String flicSecret;

    private static FlickrLib instance;

    private FlickrLib() {}

    synchronized public static FlickrLib getInstance() {
        if (instance == null) {
            instance = new FlickrLib();
        }
        return instance;
    }

    public void Auth() {
        System.out.println(flickKey);
        Flickr flickr = new Flickr(flickKey, flicSecret, new REST());
        Flickr.debugStream = false;
        AuthInterface authInterface = flickr.getAuthInterface();


        OAuth1RequestToken requestToken = authInterface.getRequestToken();
        System.out.println("token: " + requestToken);

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.WRITE);
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

    }
}
