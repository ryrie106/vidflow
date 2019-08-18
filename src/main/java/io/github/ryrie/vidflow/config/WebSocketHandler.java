package io.github.ryrie.vidflow.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;

@Slf4j
@Component
public class WebSocketHandler extends AbstractWebSocketHandler {

    private Map<String, FileUploadClient> clients;

    public WebSocketHandler(Map<String, FileUploadClient> clients) {
        this.clients = clients;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String uid = session.getId();
        FileUploadClient c = clients.get(uid);

        try {
            JSONObject obj = new JSONObject(message.getPayload());
            JSONObject response;
            switch(obj.getString("type")) {
                case "info":
                    clients.put(session.getId(),
                            new FileUploadClient(obj.getLong("uid"),
                                    obj.getString("extension"),
                                    obj.getLong("numChunks")));
                    response = new JSONObject();
                    response.put("type", "start");
                    session.sendMessage(new TextMessage(response.toString()));
                    break;
                case "complete":
                    c.afterTransferComplete(); // fileOutputStream을 닫는다.
                    clients.remove(uid);
                default:
                    break;
            }
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        String uid = session.getId();
        FileUploadClient c = clients.get(uid);
        c.pushToFile(message.getPayload().array());
        JSONObject response;

        // 파일 전송이 끝나면
        if(c.getCurrentChunk() == (c.getNumChunks() + 1)) {
            log.info("File: " + c.getFileName() + " Upload Complete");
            try {
                response = new JSONObject();
                response.put("type", "complete");
                response.put("fileName", c.getFileName());
                session.sendMessage(new TextMessage(response.toString()));
            } catch(JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            log.info("File: " + c.getFileName() +" " + c.getCurrentChunk() + "/" + c.getNumChunks() + " Uploaded");
            try {
                response = new JSONObject();
                response.put("type", "uploading");
                response.put("currentChunk", c.getCurrentChunk());
                c.setCurrentChunk(c.getCurrentChunk()+1);
                session.sendMessage(new TextMessage(response.toString()));
            } catch(JSONException ex) {
                ex.printStackTrace();
            }
        }
        session.sendMessage(message);
    }
}
