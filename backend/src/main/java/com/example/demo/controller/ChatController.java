package com.example.demo.controller;


import com.example.demo.service.ChatService;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/ai/customer")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 客服对话接口
    @GetMapping("/chat")
    public Result chat(
            @RequestParam(defaultValue = "default_user") String userId,
            @RequestParam String message
    ) {
        String reply = chatService.chat(userId, message);
        return Result.success(reply);
    }

    // 查询聊天历史
    @GetMapping("/history")
    public Result history(@RequestParam String userId) {
        List<ChatMessage> list = chatService.getHistory(userId);
        return Result.success(list);
    }

    // 智能分析接口（症状/图像描述分析）
    @GetMapping("/analyze")
    public Result analyze(
            @RequestParam(defaultValue = "default_user") String userId,
            @RequestParam String type,
            @RequestParam String description
    ) {
        String result = chatService.analyze(userId, type, description);
        return Result.success(result);
    }
}