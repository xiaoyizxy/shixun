package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.mapper.ChatMessageMapper;
import com.example.demo.service.ChatService;
import com.example.demo.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatModel chatModel;
    private final ChatMessageMapper chatMessageMapper;

    @Override
    public String chat(String userId, String userMessage) {
        // 1. 调用DeepSeek AI
        ChatClient chatClient = ChatClient.create(chatModel);
        String aiResponse = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

        // 2. 保存对话记录到MySQL
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserId(userId);
        chatMessage.setUserMessage(userMessage);
        chatMessage.setAiResponse(aiResponse);
        chatMessage.setCreateTime(LocalDateTime.now());
        chatMessageMapper.insert(chatMessage);

        return aiResponse;
    }

    @Override
    public List<ChatMessage> getHistory(String userId) {
        LambdaQueryWrapper<ChatMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ChatMessage::getUserId, userId)
               .orderByAsc(ChatMessage::getCreateTime);
        return chatMessageMapper.selectList(wrapper);
    }

    @Override
    public String analyze(String userId, String type, String description) {
        // 构建分析提示词
        String prompt = buildAnalysisPrompt(type, description);

        ChatClient chatClient = ChatClient.create(chatModel);
        String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // 保存分析记录
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserId(userId);
        chatMessage.setUserMessage("【" + type + "分析】" + description);
        chatMessage.setAiResponse(aiResponse);
        chatMessage.setCreateTime(LocalDateTime.now());
        chatMessageMapper.insert(chatMessage);

        return aiResponse;
    }

    private String buildAnalysisPrompt(String type, String description) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一位专业的智慧养老护理平台AI助手，请根据用户提供的信息进行护理相关的智能分析。\n\n");

        prompt.append("分析类型：").append(type).append("\n");
        prompt.append("用户描述：").append(description).append("\n\n");

        prompt.append("请按以下格式返回分析结果：\n");
        prompt.append("1. **分析类型**：简要说明本次分析的类型\n");
        prompt.append("2. **详细描述**：根据用户提供的信息，结合护理专业知识给出详细分析\n");
        prompt.append("3. **护理建议**：给出具体、可操作的护理建议和注意事项\n\n");
        prompt.append("请注意：用中文回答，内容要专业、实用、易懂，控制在300字以内。");

        return prompt.toString();
    }
}