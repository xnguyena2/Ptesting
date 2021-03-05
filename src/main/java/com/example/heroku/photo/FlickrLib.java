package com.example.heroku.photo;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FlickrLib {
    @Value("${photo.flickr.key}")
    private String flickKey = "8294cbd4c540c0d895ec99bb8b1fcac2";

    @Value("${photo.flickr.secret}")
    private String flicSecret = "f8fa9dcd4c4642b8";

    @Value("${photo.flickr.authentoken}")
    private String authenToken = "72157718531338752-478e88fe3e96fc70";

    @Value("${photo.flickr.tokenscret}")
    private String secretToken = "fdc5a5980651383c";

    private static FlickrLib instance;

    private FlickrLib() throws FlickrException {
        this.auth();
    }

    synchronized public static FlickrLib getInstance() throws FlickrException {
        if (instance == null) {
            instance = new FlickrLib();
        }
        return instance;
    }


    public void auth() throws FlickrException {
        Flickr flickr = new Flickr(flickKey, flicSecret, new REST());
        Flickr.debugStream = false;

        AuthInterface authInterface = flickr.getAuthInterface();

        Auth auth = authInterface.checkToken(authenToken, secretToken);
        RequestContext.getRequestContext().setAuth(auth);
    }
}
