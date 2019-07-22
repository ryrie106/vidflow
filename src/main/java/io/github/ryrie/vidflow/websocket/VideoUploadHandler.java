package io.github.ryrie.vidflow.websocket;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

public class VideoUploadHandler extends AbstractWebSocketHandler {

    private Map<String, Client> clients = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        try {
            JSONObject obj = new JSONObject(message.getPayload());
            JSONObject response;
            switch(obj.getString("type")) {

                case "VIDEOFILE_INFO":
                    clients.put(session.getId(),
                            new Client(obj.getLong("userid"),
                                    obj.getString("extension"),
                                    obj.getLong("fileSize"),
                                    obj.getLong("numChunks")));
                    response = new JSONObject();
                    response.put("type", "TRANSFER_START");
                    System.out.println("Transfer Start from : " + session.getId() + "/" + obj.getLong("userid"));
                    session.sendMessage(new TextMessage(response.toString()));
                    break;
                default:
                    break;
            }
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        Client c = clients.get(session.getId());
        c.pushToFile(message.getPayload().array());
        JSONObject response;

        // 파일 전송이 끝나면
        if(c.getCurrentChunk() == (c.getNumChunks() + 1)) {
            System.out.println("Elapsed time: " +  (System.currentTimeMillis() - c.getStartedTime() + "ms"));
            try {
                response = new JSONObject();
                response.put("type", "TRANSFER_COMPLETE");
                response.put("fileName", c.getFileName());
                c.afterTransferComplete(); // fileOutputStream을 닫는다.
                session.sendMessage(new TextMessage(response.toString()));
                clients.remove(session.getId());
            } catch(JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println(c.getFileName() + " Transferring..:" + c.getCurrentChunk() + "/" + c.getNumChunks() + " " +
                    ((float)c.getCurrentChunk() + 1) * 100 / c.getNumChunks() + "% Completed");
            try {
                response = new JSONObject();
                response.put("type", "PROGRESS_INFO");
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
