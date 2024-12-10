package com.alinesno.infra.base.search.memory;

import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;

import java.util.ArrayList;
import java.util.List;

public class BaseWorker {

    public List<Message> promptToMsg(String systemPrompt, String fewShot, String userQuery) {
        return promptToMsg(systemPrompt, fewShot, userQuery, true);
    }

    public List<Message> promptToMsg(String systemPrompt, String fewShot, String userQuery, boolean concatSystemPrompt) {
        String systemContent = systemPrompt.trim();
        String userContent = systemContent + "\r\n" + fewShot + "\r\n" +  userQuery ;

        Message systemMessage = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content(systemContent)
                .build();

        Message userMessage = Message.builder()
                .role(Role.USER.getValue())
                .content(userContent)
                .build() ;

        return new ArrayList<>(concatSystemPrompt?List.of(systemMessage, userMessage):List.of(userMessage));
    }

}
