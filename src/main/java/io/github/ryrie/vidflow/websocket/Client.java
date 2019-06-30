package io.github.ryrie.vidflow.websocket;

import io.github.ryrie.vidflow.service.PostService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private String VIDEO_DIR = "/home/ubuntu/www/videos";

    @Getter
    private String fileName;
    private String extension;
    private long fileSize;

    @Getter
    @Setter
    private long currentChunk;

    @Getter
    private long numChunks;
    private FileOutputStream fos;

    @Getter
    private long startedTime;

    public Client(Long userId, String extension, long fileSize, long numChunks) {
        this.fileName = userId.toString() + "_" + generateRandomString(15);
        this.extension = extension;
        this.fileSize = fileSize;
        this.numChunks = numChunks;
        this.startedTime = System.currentTimeMillis();

        try {
            this.fos = new FileOutputStream(VIDEO_DIR + "/" + this.fileName + "." + this.extension);
        } catch(IOException e) {
            logger.error("File creation failed");
            e.printStackTrace();
        }

        System.out.println("------Upload File Info------");
        System.out.println("Name: " + this.fileName);
        System.out.println("Ext : " + this.extension);
        System.out.println("Size: " + ((float)this.fileSize / (1024 * 1024)) + "MB");
        System.out.println("----------------------------");
    }

    public void pushToFile(byte[] b) {
        try {
            fos.write(b);
        } catch(IOException e) {
            logger.error("File Write(pushToFile) failed");
            e.printStackTrace();
        }
    }

    private String generateRandomString(int length) {

        StringBuffer temp = new StringBuffer();
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    temp.append((rnd.nextInt(10)));
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