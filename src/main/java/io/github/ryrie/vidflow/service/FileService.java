package io.github.ryrie.vidflow.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import io.github.ryrie.vidflow.payload.FileInfoResponse;
import io.github.ryrie.vidflow.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
@Slf4j
public class FileService {

    private final Regions clientRegion;
    private MessageDigest sh;
    @Value("${app.s3BucketName}")
    private String bucketName;
    @Value("${app.s3UploadURLExpirationTime}")
    private int s3UploadURLExpirationTime;

    public FileService() throws NoSuchAlgorithmException {
        this.clientRegion = Regions.AP_NORTHEAST_2;
        this.sh = MessageDigest.getInstance("SHA-256");
    }

    public FileInfoResponse getUploadFileRequest(UserPrincipal currentUser, String type) throws IOException {

        String objectKey = "";
        if("video".equals(type)) {
            objectKey += "videos/";
        } else if("image".equals(type)) {
            objectKey += "images/";
        } else {
            throw new UnsupportedOperationException("invalid upload type");
        }

        String hashInput = currentUser.getEmail() + System.currentTimeMillis();
        sh.update(hashInput.getBytes());
        byte[] byteData = sh.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : byteData) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        String hashed = sb.toString();
        objectKey += hashed;


        URL url = new URL("https://vidflow.ryrie.xyz");
        try {
            AmazonS3 s3Client =  AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();

            // Set the pre-signed URL to expire after one hour.
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the pre-signed URL.
            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
                    .withMethod(HttpMethod.PUT)
                    .withExpiration(expiration);
            url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

            log.info("Generated presigned URL : " + url);
        } catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for response, or the client
            // couldn't parse the response from Amazon S3
            e.printStackTrace();
        }



        return new FileInfoResponse(url.toString(), hashed);
    }

}
