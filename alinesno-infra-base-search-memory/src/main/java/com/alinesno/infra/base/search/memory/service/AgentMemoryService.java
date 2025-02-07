package com.alinesno.infra.base.search.memory.service;

import cn.hutool.extra.spring.SpringUtil;
import com.alinesno.infra.base.search.memory.BaseMemoryStore;
import com.alinesno.infra.base.search.memory.bean.*;
import com.alinesno.infra.base.search.memory.utils.CodeBlockParser;
import com.alinesno.infra.base.search.memory.utils.InformationParser;
import com.alinesno.infra.base.search.memory.worker.ObservationWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AgentMemoryService {

    // 缓存每个chatScopeId的记忆数据
    private final Map<Long, List<AgentMemoryDto>> memoryCache = new ConcurrentHashMap<>();

    // 存储每个chatScopeId的定时任务
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // 线程池用于调度定时任务
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    // 为每个chatScopeId提供锁对象，确保线程安全
    private final ConcurrentHashMap<Long, Object> scopeLocks = new ConcurrentHashMap<>();

    // 最大记忆数据数量，默认为5
    @Value("${agent.memory.max-size:50}")
    private int maxMemorySize;

    @Autowired
    private BaseMemoryStore baseMemoryStore ;

    /**
     * 添加记忆数据到缓存，并根据条件触发解析。
     * @param dto 记忆数据传输对象
     */
    public void addMemoryData(AgentMemoryDto dto) {
        long chatScopeId = dto.getChatScopeId();
        String roleName = "李四" ; // dto.getSourceRoleName();

        synchronized (getLockForScope(chatScopeId)) {

            // 将数据添加到缓存
            List<AgentMemoryDto> dataList = memoryCache.computeIfAbsent(chatScopeId, k -> new ArrayList<>());
            dataList.add(dto);
            log.debug("Added memory data for chatScopeId: {}", chatScopeId);

            if (dataList.size() >= maxMemorySize) {
                // 达到阈值，立即处理并清除缓存
                processAndClear(chatScopeId, roleName , dto);
                // 取消可能存在的定时任务
                cancelScheduledTask(chatScopeId);
            } else {
                // 未达阈值，重置定时任务
                rescheduleProcessing(chatScopeId, roleName , dto);
            }
        }
    }

    /**
     * 获取指定chatScopeId的锁对象。
     * @param chatScopeId 会话范围ID
     * @return 锁对象
     */
    private Object getLockForScope(long chatScopeId) {
        return scopeLocks.computeIfAbsent(chatScopeId, k -> new Object());
    }

    /**
     * 处理并清除指定chatScopeId的缓存数据。
     * @param chatScopeId 会话范围ID
     */
    private void processAndClear(long chatScopeId, String roleName , AgentMemoryDto dto) {
        List<AgentMemoryDto> dataToProcess = memoryCache.remove(chatScopeId);
        if (dataToProcess != null && !dataToProcess.isEmpty()) {
            parseMemoryData(chatScopeId, roleName, dataToProcess , dto);
        }
    }

    /**
     * 解析记忆数据（异步执行）。
     *
     * @param chatScopeId 会话范围ID
     * @param roleName    角色名称
     * @param dataList    待解析的数据列表
     * @param dtoBean     传递参数数据
     */
    public void parseMemoryData(long chatScopeId, String roleName, List<AgentMemoryDto> dataList, AgentMemoryDto dtoBean) {
        // 实现解析逻辑，例如调用外部服务或处理数据
        System.out.println("Asynchronously parsing data for chatScopeId " + chatScopeId + ", size: " + dataList.size());

        StringBuilder memoryData = new StringBuilder();

        for (AgentMemoryDto dto : dataList) {
            memoryData
                    .append(dto.getSourceRoleName())
                    .append("对")
                    .append(dto.getTargetRoleName())
                    .append(":")
                    .append(dto.getData())
                    .append("\r\n");
        }
        log.debug("---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
        log.debug("解析:{}的记忆:{}\r\n", chatScopeId, memoryData);

//        ObservationWithTimeWorker observationWorker = SpringUtil.getBean(ObservationWithTimeWorker.class);

        ObservationWorker observationWorker = SpringUtil.getBean(ObservationWorker.class);
        String content = observationWorker.parseMemory(roleName, memoryData.toString());

        log.debug("---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
        log.debug("解析:{}的记忆内容:{}\r\n", chatScopeId, content);

        List<CodeContent> codeContentList = CodeBlockParser.parseCodeBlocks(content);

        List<EntryBean> beanList = InformationParser.parseContent(codeContentList.get(0).getContent());
        for (EntryBean entryBean : beanList) {
            log.debug("---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
            log.debug("解析:{}的记忆内容:{}", chatScopeId, entryBean);
        }

        if(!CollectionUtils.isEmpty(beanList)){

            List<MemoryNode> nodes = new ArrayList<>() ;

            for (EntryBean entryBean : beanList) {
                MemoryNode memoryNode = new MemoryNode();

                // 设置记忆节点信息
                memoryNode.setAgentId(dtoBean.getAgentId());
                memoryNode.setAgentName(dtoBean.getAgentName());

                // 使用Stream API来整合keywords和keyInformation
                List<InformationBean> informationList = entryBean.getInformation();

                String keywords = informationList.stream()
                        .map(InformationBean::getKeywords)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(", "));

                String contentStr = informationList.stream()
                        .map(InformationBean::getKeyInformation)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(". "));

                memoryNode.setKeys(keywords);
                memoryNode.setContent(contentStr);

                nodes.add(memoryNode);
            }

            baseMemoryStore.batchInsert(nodes);
        }
    }

    /**
     * 取消指定chatScopeId的定时任务。
     * @param chatScopeId 会话范围ID
     */
    private void cancelScheduledTask(long chatScopeId) {
        ScheduledFuture<?> future = scheduledTasks.remove(chatScopeId);
        if (future != null) {
            future.cancel(false);
        }
    }

    /**
     * 重新安排解析任务，1分钟后执行。
     * @param chatScopeId 会话范围ID
     */
    private void rescheduleProcessing(long chatScopeId, String roleName , AgentMemoryDto dto) {
        // 取消现有任务
        cancelScheduledTask(chatScopeId);
        // 创建新任务
        ScheduledFuture<?> newFuture = scheduler.schedule(() -> {
            synchronized (getLockForScope(chatScopeId)) {
                processAndClear(chatScopeId, roleName , dto);
                scheduledTasks.remove(chatScopeId);
            }
        }, 1, TimeUnit.MINUTES);
        scheduledTasks.put(chatScopeId, newFuture);
    }

    /**
     * 关闭线程池，释放资源。
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}