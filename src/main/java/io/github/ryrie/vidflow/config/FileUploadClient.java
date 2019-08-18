package io.github.ryrie.vidflow.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

@Slf4j
public class FileUploadClient {

//    @Value("${file.upload-dir}")
    private String uploadDir = "/home/user/uploads";

    @Getter
    private String fileName;

    @Getter
    @Setter
    private long currentChunk;

    @Getter
    private long numChunks;
    private FileOutputStream fos;

    public FileUploadClient(Long userId, String extension, long numChunks) {
        this.fileName = userId + "_" + generateRandomString(15) + "." + extension;
        this.numChunks = numChunks;

        try {
            this.fos = new FileOutputStream(uploadDir + "/" + this.fileName);
        } catch(IOException e) {
            log.error("File creation failed");
            e.printStackTrace();
        }

        log.info("File: " + this.fileName + "  Upload Start");
    }

    public void pushToFile(byte[] b) {
        try {
            fos.write(b);
        } catch(IOException e) {
            log.error("File Write(pushToFile) failed");
            e.printStackTrace();
        }
    }

    private String generateRandomString(int length) {

        StringBuilder temp = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = r.nextInt(3);
            switch (rIndex) {
                case 0:
                    temp.append((char) ((int) (r.nextInt(26)) + 97));
                    break;
                case 1:
                    temp.append((char) ((int) (r.nextInt(26)) + 65));
                    break;
                case 2:
                    temp.append((r.nextInt(10)));
                    break;
            }
        }
        return temp.toString();
    }

    public void afterTransferComplete() {
        try {
            this.fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}