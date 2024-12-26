# alinesno-infra-base-search
基于索引和向量的搜索服务，提供完善的全是搜索管理服务，用于商品搜索、文档搜索、PDF、Word搜索等。

## GraphRAG 索引创建过程

- [深度解读GraphRAG：如何通过知识图谱提升 RAG 系统](https://blog.csdn.net/zhishi0000/article/details/143177019)

索引过程包括四个关键步骤：
- 文本单元分割（Text Unit Segmentation）：整个输入语料库被划分为多个文本单元（文本块）。这些文本块是最小的可分析单元，可以是段落、句子或其他逻辑单元。通过将长文档分割成较小的文本块，我们可以提取并保留有关输入数据的更详细信息。
- 提取 Entity、关系（Relationship）和 Claim：GraphRAG 使用 LLM 识别并提取每个文本单元中的所有Entity（人名、地点、组织等）、Entity 之间的关系以及文本中表达的关键 Claim。
- 层次聚类：GraphRAG 使用 Leiden 技术对初始知识图谱执行分层聚类。Leiden 是一种 community 检测算法，能够有效地发现图中的 community 结构。每个聚类中的 Entity 被分配到不同的 community，以便进行更深入的分析。
  注意：community 是图中一组节点，它们彼此之间紧密连接，但与网络中其他 dense group 的连接较为“稀疏”。
- 生成 Community 摘要：GraphRAG 使用自下而上的方法为每个 community 及其中的重要部分生成摘要。这些摘要包括 Community 内的主要 Entity、Entity的关系和关键 Claim。这一步为整个数据集提供了概览，并为后续查询提供了有用的上下文信息。

## 鸣谢

主要参考以下项目

- MemoryScope
- Mem0
- ai-town