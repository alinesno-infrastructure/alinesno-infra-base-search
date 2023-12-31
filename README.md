# alinesno-infra-base-search
基于索引和向量的搜索服务，提供完善的全是搜索管理服务，用于商品搜索、文档搜索、PDF、Word搜索等。

## 接口

### DocumentSearch API 

|序号|接口名称|接口地址|完成状态|备注|
|---|---|---|---|---|
|1|将数据保存到Elasticsearch|POST /api/base/search/document/save|未完成|需要传入JSON对象、索引基础名称和索引类型|
|2|根据查询文本进行搜索|GET /api/base/search/document/search|未完成|需要传入索引基础名称、索引类型和查询文本|
|3|根据字段进行搜索|GET /api/base/search/document/searchByField|未完成|需要传入索引基础名称、索引类型、字段名称和查询文本|

### VectorAPI

|序号|接口名称|接口地址|完成状态|备注|
|---|---|---|---|---|
|1|文件上传|POST /api/base/search/vectorData/upload|未完成|支持PDF、Word、Xmind文件上传|
|2|创建集合的REST API接口|POST /api/base/search/vectorData/createCollection|未完成|需要传入集合名称、集合描述、分片数量和字段类型|
|3|插入数据的REST API接口|POST /api/base/search/vectorData/insertData|未完成|需要传入集合名称、分区名称和插入参数字段列表|
|4|删除数据的REST API接口|DELETE /api/base/search/vectorData/deleteData|未完成|需要传入集合名称和删除表达式|

|序号|接口名称|接口地址|完成状态|备注|
|---|---|---|---|---|
|1|处理搜索Milvus集合的HTTP POST请求|POST /api/base/search/vectorSearch/search|未完成|需要传入集合名称、要搜索的向量列表和要返回的最近邻居数量|
|2|处理带有过滤条件的搜索Milvus集合的HTTP POST请求|POST /api/base/search/vectorSearch/searchWithFilter|未完成|需要传入集合名称、要搜索的向量列表、要返回的最近邻居数量和搜索表达式|
|3|处理异步搜索Milvus集合的HTTP POST请求|POST /api/base/search/vectorSearch/searchAsync|未完成|需要传入集合名称、要搜索的向量列表、要包括在搜索中的分区列表和要返回的最近邻居数量，可能会抛出ExecutionException和InterruptedException|
|4|处理获取Milvus集合分区列表的HTTP GET请求|GET /api/base/search/vectorSearch/partitions|未完成|需要传入要获取分区列表的集合名称|

### 接口列表更新

|序号|接口名称|接口地址|完成状态|备注|
|---|---|---|---|---|
|1|获取句子向量的HTTP POST请求|POST /embeddings|已完成|接收文本数据并返回句子向量|

## ES版本说明

这里使用的是7.9.3版本的es(规避es的SSLE协议)，后面更新会使用和验证OpenSearch版本

## 示例

当然，这是一个使用curl发送POST请求到我们的Spring Boot应用程序的示例：

假设你的Spring Boot应用运行在localhost的8080端口上，你可以这样发送请求：

```bash
curl -X POST -H "Content-Type: application/json" -d '{"key1":"value1", "key2":"value2"}' 'http://localhost:8080/api/save?indexBase=myIndex&indexType=daily'
```

在这个请求中：

- `-X POST` 指定了请求类型为POST。
- `-H "Content-Type: application/json"` 设置了请求头，指定内容类型为JSON。
- `-d '{"key1":"value1", "key2":"value2"}'` 是我们要发送的JSON数据。
- `'http://localhost:8080/api/save?indexBase=myIndex&indexType=daily'` 是我们的请求URL，其中包含了我们的请求参数。

请确保将`'{"key1":"value1", "key2":"value2"}'`替换为你实际要发送的JSON数据，将`myIndex`替换为你的索引基础名称，将`daily`替换为你的索引类型（可以是`daily`或`monthly`）。

对应的curl请求示例如下：

```bash
curl -X GET 'http://localhost:8080/api/search?indexBase=myIndex&indexType=daily&queryText=mySearchText'
```

请确保将`myIndex`替换为你的索引基础名称，将`daily`替换为你的索引类型（可以是`daily`或`monthly`），将`mySearchText`替换为你的搜索文本。



对应的curl请求示例如下：

```bash
curl -X GET 'http://localhost:8080/api/searchByField?indexBase=myIndex&indexType=daily&fieldName=myFieldName&queryText=mySearchText'
```

请确保将`myIndex`替换为你的索引基础名称，将`daily`替换为你的索引类型（可以是`daily`或`monthly`），将`myFieldName`替换为你要搜索的字段名，将`mySearchText`替换为你的搜索文本。

注意：在实际生产环境中，你可能需要对错误进行更详细的处理，并提供更详细的响应信息。这只是一个基础的示例，你可能需要根据你的具体需求进行修改。

## 鸣谢

- 日志系统设计参考[把ES换成ClickHouse，B站的日志系统像开挂了一样](https://www.51cto.com/article/719248.html)