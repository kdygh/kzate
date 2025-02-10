package com.ks.suate01.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MessageService {
    private JsonNode messagesJson;

    public MessageService() {
        loadMessages();
    }

    private void loadMessages() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream inputStream = new ClassPathResource("messages.json").getInputStream();
            messagesJson = objectMapper.readTree(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public JsonNode getAllMessages() {
        return messagesJson;
    }

    public String getMessage(String category, String code) {
        return messagesJson.path(category).path("messages").path(code).asText();
    }
    
    
    public void logToFile(int userId, String message) {
        String logFilePath = "/home/azure_kz/logs/game_logs.log";  // 로그 파일 경로
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = sdf.format(new Date());
        
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write(timestamp + " [User ID: " + userId + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}