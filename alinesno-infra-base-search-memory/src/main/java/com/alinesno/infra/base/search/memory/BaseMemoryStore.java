package com.alinesno.infra.base.search.memory;

import com.alinesno.infra.base.search.memory.bean.MemoryNode;

import java.util.List;

/**
 * 定义了处理记忆节点（MemoryNode）的记忆存储接口。
 * 它概述了基本操作，如检索、更新、刷新和关闭记忆范围。
 */
public interface BaseMemoryStore {

//    /**
//     * 检索与查询最相关的一系列 MemoryNode 对象，
//     * 考虑过滤字典以应用额外约束。返回的节点数量由 top_k 限制。
//     *
//     * @param query 查询字符串，用于查找相关的记忆
//     * @param topK  返回的最大 MemoryNode 对象数量
//     * @param filterDict 键代表过滤字段，值为过滤条件列表的字典
//     * @return 按照与查询的相关性排序的 MemoryNode 对象列表，最多包含 topK 项
//     */
//    List<MemoryNode> retrieveMemories(String query, int topK, Map<String, List<String>> filterDict);
//
//    /**
//     * 异步检索与查询最匹配的一系列 MemoryNode 对象，
//     * 尊重过滤字典，结果大小限制为 topK。
//     *
//     * @param query 查询文本，在记忆节点中搜索
//     * @param topK 最大返回节点数
//     * @param filterDict 应用于记忆节点的过滤器
//     * @return 匹配标准的最多 topK 个 MemoryNode 对象列表
//     */
//    CompletableFuture<List<MemoryNode>> aRetrieveMemories(String query, int topK, Map<String, List<String>> filterDict);

    /**
     * 批量插入记忆节点。
     *
     * @param nodes 要插入的记忆节点列表
     */
    void batchInsert(List<MemoryNode> nodes);

//    /**
//     * 批量更新记忆节点。
//     *
//     * @param nodes 要更新的记忆节点列表
//     * @param updateEmbedding 是否更新嵌入向量
//     */
//    void batchUpdate(List<MemoryNode> nodes, boolean updateEmbedding);
//
//    /**
//     * 批量删除记忆节点。
//     *
//     * @param nodes 要删除的记忆节点列表
//     */
//    void batchDelete(List<MemoryNode> nodes);
//
//    /**
//     * 刷新任何待处理的记忆更新或操作，以确保数据一致性。
//     * 子类应该覆盖此方法以提供具体的刷新机制。
//     */
//    default void flush() {
//        // 默认实现为空
//    }
//
//    /**
//     * 关闭记忆存储，释放与之关联的所有资源。
//     * 子类必须实现此方法以定义如何正确关闭记忆存储。
//     */
//    void close();

}