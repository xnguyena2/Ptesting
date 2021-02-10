package com.example.heroku.photo;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.NetworkIOException;
import com.dropbox.core.RetryException;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PhotoLib {
    // Adjust the chunk size based on your network speed and reliability. Larger chunk sizes will
    // result in fewer network requests, which will be faster. But if an error occurs, the entire
    // chunk will be lost and have to be re-uploaded. Use a multiple of 4MiB for your chunk size.
    private static final long CHUNKED_UPLOAD_CHUNK_SIZE = 8L << 20; // 8MiB
    private static final int CHUNKED_UPLOAD_MAX_ATTEMPTS = 5;

    private static PhotoLib instance;

    DbxClientV2 client;

    private PhotoLib() {
        ResetToken();
    }

    synchronized public static PhotoLib getInstance() {
        if (instance == null) {
            instance = new PhotoLib();
        }
        return instance;
    }
    private static final String ACCESS_TOKEN = "4rxXYlLJ2wUAAAAAAAAAAYgBK4611ExzVdvh8s6f0MQJEAC2uhTacLQOdICtoFam";

    private void ResetToken(){
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig.newBuilder("BIOMarket").build();
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    public void deleteAll(){
        try {
            ListFolderResult result = client.files().listFolder("");
            List<DeleteArg> allFile = new ArrayList();
            for (Metadata metadata : result.getEntries()) {
                System.out.println(metadata.getPathLower());
                allFile.add(new DeleteArg(metadata.getPathLower()));
            }
            client.files().deleteBatch(allFile);
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }

    public boolean uploadFile(InputStream fileStream, String dropboxPath) {
        long fileSize = 100;//for now we don't ue multiple chunk
        return chunkedUploadFile(client, fileStream, dropboxPath, fileSize);
    }

    public byte[] downloadFile(String id){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            client.files().download("/"+id).download(baos);
            return baos.toByteArray();
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return null;
    }

    /**
     * Uploads a file in a single request. This approach is preferred for small files since it
     * eliminates unnecessary round-trips to the servers.
     *
     * @param dbxClient Dropbox user authenticated client
     * @param fileStream local file to upload
     * @param dropboxPath Where to upload the file to within Dropbox
     */
    private boolean uploadFile(DbxClientV2 dbxClient, InputStream fileStream, String dropboxPath, long size) {
        try {
            IOUtil.ProgressListener progressListener = l -> printProgress(l, size);

            FileMetadata metadata = dbxClient.files().uploadBuilder(dropboxPath)
                    .withMode(WriteMode.ADD)
                    .withClientModified(new Date())
                    .uploadAndFinish(fileStream, progressListener);

            System.out.println(metadata.toStringMultiline());
        } catch (UploadErrorException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
            return false;
        } catch (DbxException ex) {
            System.err.println("Error uploading to Dropbox: " + ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.err.println("Error reading from file \"" + dropboxPath + "\": " + ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Uploads a file in chunks using multiple requests. This approach is preferred for larger files
     * since it allows for more efficient processing of the file contents on the server side and
     * also allows partial uploads to be retried (e.g. network connection problem will not cause you
     * to re-upload all the bytes).
     *
     * @param dbxClient Dropbox user authenticated client
     * @param fileStream local file to upload
     * @param dropboxPath Where to upload the file to within Dropbox
     */
    private boolean chunkedUploadFile(DbxClientV2 dbxClient, InputStream fileStream, String dropboxPath, long size) {

        System.out.println("File size: "+ size);

        // assert our file is at least the chunk upload size. We make this assumption in the code
        // below to simplify the logic.
        if (size < CHUNKED_UPLOAD_CHUNK_SIZE) {
            return uploadFile(dbxClient, fileStream, dropboxPath, size);
        }

        long uploaded = 0L;
        DbxException thrown = null;

        IOUtil.ProgressListener progressListener = new IOUtil.ProgressListener() {
            long uploadedBytes = 0;
            @Override
            public void onProgress(long l) {
                printProgress(l + uploadedBytes, size);
                if (l == CHUNKED_UPLOAD_CHUNK_SIZE) uploadedBytes += CHUNKED_UPLOAD_CHUNK_SIZE;
            }
        };

        // Chunked uploads have 3 phases, each of which can accept uploaded bytes:
        //
        //    (1)  Start: initiate the upload and get an upload session ID
        //    (2) Append: upload chunks of the file to append to our session
        //    (3) Finish: commit the upload and close the session
        //
        // We track how many bytes we uploaded to determine which phase we should be in.
        String sessionId = null;
        for (int i = 0; i < CHUNKED_UPLOAD_MAX_ATTEMPTS; ++i) {
            if (i > 0) {
                System.out.printf("Retrying chunked upload (%d / %d attempts)\n", i + 1, CHUNKED_UPLOAD_MAX_ATTEMPTS);
            }

            try {
                // if this is a retry, make sure seek to the correct offset
                fileStream.skip(uploaded);

                // (1) Start
                if (sessionId == null) {
                    sessionId = dbxClient.files().uploadSessionStart()
                            .uploadAndFinish(fileStream, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener)
                            .getSessionId();
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    printProgress(uploaded, size);
                }

                UploadSessionCursor cursor = new UploadSessionCursor(sessionId, uploaded);

                // (2) Append
                while ((size - uploaded) > CHUNKED_UPLOAD_CHUNK_SIZE) {
                    dbxClient.files().uploadSessionAppendV2(cursor)
                            .uploadAndFinish(fileStream, CHUNKED_UPLOAD_CHUNK_SIZE, progressListener);
                    uploaded += CHUNKED_UPLOAD_CHUNK_SIZE;
                    printProgress(uploaded, size);
                    cursor = new UploadSessionCursor(sessionId, uploaded);
                }

                // (3) Finish
                long remaining = size - uploaded;
                CommitInfo commitInfo = CommitInfo.newBuilder(dropboxPath)
                        .withMode(WriteMode.ADD)
                        .withClientModified(new Date())
                        .build();
                FileMetadata metadata = dbxClient.files().uploadSessionFinish(cursor, commitInfo)
                        .uploadAndFinish(fileStream, remaining, progressListener);

                System.out.println(metadata.toStringMultiline());
                return true;
            } catch (RetryException ex) {
                thrown = ex;
                // RetryExceptions are never automatically retried by the client for uploads. Must
                // catch this exception even if DbxRequestConfig.getMaxRetries() > 0.
                sleepQuietly(ex.getBackoffMillis());
                continue;
            } catch (NetworkIOException ex) {
                thrown = ex;
                // network issue with Dropbox (maybe a timeout?) try again
                continue;
            } catch (UploadSessionLookupErrorException ex) {
                if (ex.errorValue.isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                            .getIncorrectOffsetValue()
                            .getCorrectOffset();
                    continue;
                } else {
                    // Some other error occurred, give up.
                    System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                    return false;
                }
            } catch (UploadSessionFinishErrorException ex) {
                if (ex.errorValue.isLookupFailed() && ex.errorValue.getLookupFailedValue().isIncorrectOffset()) {
                    thrown = ex;
                    // server offset into the stream doesn't match our offset (uploaded). Seek to
                    // the expected offset according to the server and try again.
                    uploaded = ex.errorValue
                            .getLookupFailedValue()
                            .getIncorrectOffsetValue()
                            .getCorrectOffset();
                    continue;
                } else {
                    // some other error occurred, give up.
                    System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                    return false;
                }
            } catch (DbxException ex) {
                System.err.println("Error uploading to Dropbox: " + ex.getMessage());
                return false;
            } catch (IOException ex) {
                System.err.println("Error reading from file \"" + dropboxPath + "\": " + ex.getMessage());
                return false;
            }
        }

        // if we made it here, then we must have run out of attempts
        System.err.println("Maxed out upload attempts to Dropbox. Most recent error: " + thrown.getMessage());
        return false;
    }

    private static void printProgress(long uploaded, long size) {
        System.out.printf("Uploaded %12d / %12d bytes (%5.2f%%)\n", uploaded, size, 100 * (uploaded / (double) size));
    }

    private static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            // just exit
            System.err.println("Error uploading to Dropbox: interrupted during backoff.");
        }
    }
}
