package com.example.demo.service;

import com.example.demo.entity.ChatMessage;

import java.util.List;

public interface ChatService {
    // 单轮对话
    String chat(String userId, String userMessage);

    // 查询历史记录
    List<ChatMessage> getHistory(String userId);

    // 智能分析（症状、图像描述等）
    String analyze(String userId, String type, String description);
}