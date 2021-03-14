package com.example.heroku.photo;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    private boolean setOrigFilenameTag = true;
    private boolean replaceSpaces = true;

    Flickr flickr;
    Auth auth;

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
        flickr = new Flickr(flickKey, flicSecret, new REST());
        Flickr.debugStream = false;

        AuthInterface authInterface = flickr.getAuthInterface();

        auth = authInterface.checkToken(authenToken, secretToken);
        //RequestContext.getRequestContext().setAuth(auth);
        System.out.println("Thanks.  You probably will not have to do this every time. Auth saved for user: " + auth.getUser().getUsername() + " nsid is: "
                + auth.getUser().getId());
        System.out.println(" AuthToken: " + auth.getToken() + " tokenSecret: " + auth.getTokenSecret());
    }

    public String[] uploadfile(InputStream in, String filename) {
        RequestContext rc = RequestContext.getRequestContext();
        try {
            rc.setAuth(this.auth);
        }catch (Exception eee){
            eee.printStackTrace();
        }
        String photoId = null;
        String basefilename = null;


        // PhotosetsInterface pi = flickr.getPhotosetsInterface();
        // PhotosInterface photoInt = flickr.getPhotosInterface();
        // Map<String, Collection> allPhotos = new HashMap<String, Collection>();
        /**
         * 1 : Public 2 : Friends only 3 : Family only 4 : Friends and Family 5 : Private
         **/

        UploadMetaData metaData = new UploadMetaData();

        //public
        metaData.setPublicFlag(true);

        basefilename = filename; // "image.jpg";

        String title = basefilename;
        boolean setMimeType = true; // change during testing. Doesn't seem to be supported at this time in flickr.
        if (setMimeType) {
            if (basefilename.lastIndexOf('.') > 0) {
                title = basefilename.substring(0, basefilename.lastIndexOf('.'));
                String suffix = basefilename.substring(basefilename.lastIndexOf('.') + 1);
                // Set Mime Type if known.

                // Later use a mime-type properties file or a hash table of all known photo and video types
                // allowed by flickr.

                if (suffix.equalsIgnoreCase("png")) {
                    metaData.setFilemimetype("image/png");
                } else if (suffix.equalsIgnoreCase("mpg") || suffix.equalsIgnoreCase("mpeg")) {
                    metaData.setFilemimetype("video/mpeg");
                } else if (suffix.equalsIgnoreCase("mov")) {
                    metaData.setFilemimetype("video/quicktime");
                }
            }
        }
        System.out.println(" File : " + filename);
        System.out.println(" basefilename : " + basefilename);


        // UploadMeta is using String not Tag class.

        // Tags are getting mangled by yahoo stripping off the = , '.' and many other punctuation characters
        // and converting to lower case: use the raw tag field to find the real value for checking and
        // for download.
        if (setOrigFilenameTag) {
            List<String> tags = new ArrayList<String>();
            String tmp = basefilename;
            basefilename = makeSafeFilename(basefilename);
            tags.add("OrigFileName='" + basefilename + "'");
            metaData.setTags(tags);

            if (!tmp.equals(basefilename)) {
                System.out.println(" File : " + basefilename + " contains special characters.  stored as " + basefilename + " in tag field");
            }
        }

        // File imageFile = new File(filename);
        // InputStream in = null;
        Uploader uploader = flickr.getUploader();

        // ByteArrayOutputStream out = null;
        try {
            // in = new FileInputStream(imageFile);
            // out = new ByteArrayOutputStream();

            // int b = -1;
            /**
             * while ((b = in.read()) != -1) { out.write((byte) b); }
             **/

            /**
             * byte[] buf = new byte[1024]; while ((b = in.read(buf)) != -1) { // fos.write(read); out.write(buf, 0, b); }
             **/

            metaData.setFilename(basefilename);
            // check correct handling of escaped value

            photoId = uploader.upload(in, metaData);

            System.out.println(" File : " + filename + " uploaded: photoId = " + photoId);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
        return getUrl(photoId);
    }

    public String[] getUrl(String id) {
        String[] allImg = new String[4];
        PhotosInterface photos = flickr.getPhotosInterface();
        try {
            Photo photo = photos.getPhoto(id);
            allImg[0] = id;
            allImg[1] = photo.getThumbnailUrl();
            allImg[2] = photo.getSmall320Url();
            allImg[3] = photo.getOriginalUrl();

            return allImg;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public boolean DeleteImage(String id){
        RequestContext rc = RequestContext.getRequestContext();
        rc.setAuth(this.auth);
        PhotosInterface photos = flickr.getPhotosInterface();
        try {
        photos.delete(id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void DeleteAll(){
        try {
            RequestContext rc = RequestContext.getRequestContext();
            rc.setAuth(this.auth);
            System.out.println("Delete all img");
            PhotosInterface photos = flickr.getPhotosInterface();
            SearchParameters params = new SearchParameters();
            params.setText("test");
            PhotoList<Photo> results = photos.search(params, 0, 0);

            results.forEach(p ->
            {
                try {
                    System.out.println("Delete: "+p.getId());
                    photos.delete(p.getId());
                } catch (FlickrException e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private String makeSafeFilename(String input) {
        byte[] fname = input.getBytes();
        byte[] bad = new byte[] { '\\', '/', '"', '*' };
        byte replace = '_';
        for (int i = 0; i < fname.length; i++) {
            for (byte element : bad) {
                if (fname[i] == element) {
                    fname[i] = replace;
                }
            }
            if (replaceSpaces && fname[i] == ' ')
                fname[i] = '_';
        }
        return new String(fname);
    }
}