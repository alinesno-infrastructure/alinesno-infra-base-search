package com.alinesno.infra.base.search.memory.service;

import com.alinesno.infra.base.search.memory.bean.AgentMemoryDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IAgentMemoryService {

    /**
     * 批量添加记忆数据
     * @param dtos
     */
    void addBatchMemoryData(List<AgentMemoryDto> dtos , AgentMemoryDto dto);

}