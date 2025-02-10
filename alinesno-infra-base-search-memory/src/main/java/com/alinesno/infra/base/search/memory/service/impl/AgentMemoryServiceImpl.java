package com.alinesno.infra.base.search.memory.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alinesno.infra.base.search.memory.BaseMemoryStore;
import com.alinesno.infra.base.search.memory.bean.AgentMemoryDto;
import com.alinesno.infra.base.search.memory.bean.CodeContent;
import com.alinesno.infra.base.search.memory.bean.InformationBean;
import com.alinesno.infra.base.search.memory.bean.MemoryNode;
import com.alinesno.infra.base.search.memory.service.IAgentMemoryService;
import com.alinesno.infra.base.search.memory.utils.CodeBlockParser;
import com.alinesno.infra.base.search.memory.utils.InformationParser;
import com.alinesno.infra.base.search.memory.worker.ObservationWorker;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * AgentMemoryServiceImpl类
 * @author luoxiaodong
 */
@Slf4j
@Service
public class AgentMemoryServiceImpl implements IAgentMemoryService {

    // 缓存每个chatScopeId的记忆数据
    private final Map<Long, List<AgentMemoryDto>> memoryCache = new ConcurrentHashMap<>();

    // 存储每个chatScopeId的定时任务
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // 线程池用于调度定时任务
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    // 为每个chatScopeId提供锁对象，确保线程安全
    private final ConcurrentHashMap<Long, Object> scopeLocks = new ConcurrentHashMap<>();

    @Autowired
    private ThreadPoolTaskExecutor customTaskExecutor;

    // 最大记忆数据数量，默认为5
    @Value("${agent.memory.max-size:50}")
    private int maxMemorySize;

    @Autowired
    private BaseMemoryStore baseMemoryStore ;

//    /**
//     * 添加记忆数据到缓存，并根据条件触发解析。
//     * @param dto 记忆数据传输对象
//     */
//    public void addMemoryData(AgentMemoryDto dto) {
//        long chatScopeId = dto.getChatScopeId();
//
//        // 获取到对话里面的人物和角色信息
//
//        String roleName = dto.getAgentName() ;
//
//        synchronized (getLockForScope(chatScopeId)) {
//
//            // 将数据添加到缓存
//            List<AgentMemoryDto> dataList = memoryCache.computeIfAbsent(chatScopeId, k -> new ArrayList<>());
//            dataList.add(dto);
//            log.debug("Added memory data for chatScopeId: {}", chatScopeId);
//
//            // 获取到对话的人物信息
//            Map<Long , String> agents = new HashMap<>();
//
//            for(AgentMemoryDto am: dataList){
//                if(am.getTargetRoleId() != am.getAgentId()){
//                    agents.put(am.getTargetRoleId() , am.getTargetRoleName()) ;
//                }
//            }
//
//            if (dataList.size() >= maxMemorySize) {
//
//                log.debug("Memory cache reached the threshold, processing and clearing...");
//
//                    for(Long agentId : agents.keySet()){
//                        // 达到阈值，立即处理并清除缓存
//                        processAndClear(chatScopeId, agents.get(agentId) , dto);
//                        // 取消可能存在的定时任务
//                        cancelScheduledTask(chatScopeId);
//                    }
//
//            } else {
//                // 未达阈值，重置定时任务
//                for(Long agentId : agents.keySet()) {
//                    rescheduleProcessing(agentId, agents.get(agentId), dto);
//                }
//            }
//        }
//    }

    @Override
    public void addBatchMemoryData(List<AgentMemoryDto> dtos , AgentMemoryDto dtoBean) {
        // 获取到对话的人物信息
        Map<Long , String> agents = new HashMap<>();

        for(AgentMemoryDto am: dtos){
            if(am.getTargetRoleId() != dtoBean.getAgentId()){
                agents.put(am.getTargetRoleId() , am.getTargetRoleName()) ;
            }
        }

        for(Long agentId : agents.keySet()){
            List<AgentMemoryDto> dataToProcess = dtos.stream().filter(dto -> dto.getTargetRoleId() == agentId).toList();

            customTaskExecutor.execute(() -> {
                parseMemoryData(agentId, agents.get(agentId) , dataToProcess, dtoBean);
            });
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
            customTaskExecutor.execute(() -> {
                parseMemoryData(chatScopeId, roleName, dataToProcess, dto);
            });
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
        log.debug("解析对{}的记忆:{}\r\n", roleName, memoryData);

        ObservationWorker observationWorker = SpringUtil.getBean(ObservationWorker.class);
        String content = observationWorker.parseMemory(roleName, memoryData.toString());

        log.debug("---->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>..");
        log.debug("解析对{}的记忆内容:{}\r\n", roleName, content);

        List<CodeContent> codeContentList = CodeBlockParser.parseCodeBlocks(content);

        List<InformationBean> beanList = InformationParser.parseContent(codeContentList.get(0).getContent());
        for (InformationBean entryBean : beanList) {
            log.debug("解析对{}记忆内容:{}", roleName, entryBean);
        }

        if(!CollectionUtils.isEmpty(beanList)){
            List<MemoryNode> nodes = getMemoryNodes(dtoBean, beanList);
            baseMemoryStore.batchInsert(nodes);
        }
    }

    @NotNull
    private static List<MemoryNode> getMemoryNodes(AgentMemoryDto dtoBean, List<InformationBean> beanList) {
        List<MemoryNode> nodes = new ArrayList<>() ;

        for (InformationBean entryBean : beanList) {
            MemoryNode memoryNode = new MemoryNode();

            // 设置记忆节点信息
            memoryNode.setAgentId(dtoBean.getAgentId());
            memoryNode.setAgentName(dtoBean.getAgentName());


            String keywords = entryBean.getKeywords() ;
            String contentStr = entryBean.getKeyInformation() ;

            memoryNode.setKeys(keywords);
            memoryNode.setContent(contentStr);

            nodes.add(memoryNode);
        }
        return nodes;
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

//    /**
//     * 重新安排解析任务，1分钟后执行。
//     * @param chatScopeId 会话范围ID
//     */
//    private void rescheduleProcessing(long chatScopeId, String roleName , AgentMemoryDto dto) {
//        // 取消现有任务
//        cancelScheduledTask(chatScopeId);
//        // 创建新任务
//        ScheduledFuture<?> newFuture = scheduler.schedule(() -> {
//            synchronized (getLockForScope(chatScopeId)) {
//                processAndClear(chatScopeId, roleName , dto);
//                scheduledTasks.remove(chatScopeId);
//            }
//        }, 1, TimeUnit.MINUTES);
//        scheduledTasks.put(chatScopeId, newFuture);
//    }
//
//    /**
//     * 关闭线程池，释放资源。
//     */
//    public void shutdown() {
//        scheduler.shutdown();
//        try {
//            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
//                scheduler.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduler.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//    }

}