package com.ks.suate01.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.ks.suate01.service.MessageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public JsonNode getAllMessages() {
        return messageService.getAllMessages();
    }
}
