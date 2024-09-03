# alinesno-infra-base-search
基于索引和向量的搜索服务，提供完善的全是搜索管理服务，用于商品搜索、文档搜索、PDF、Word搜索等。

## ES版本说明

这里使用的是8.5.2版本的es，后面更新会使用和验证OpenSearch版本

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

