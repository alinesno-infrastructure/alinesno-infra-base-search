package com.alinesno.infra.base.search;

import okhttp3.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * 集成一个Java开发示例工具
 * @author LuoAnDong
 * @version 2023年8月3日 上午6:23:43
 */
@SpringBootApplication
public class BaseSearchApplication {

	public static void main(String[] args) throws IOException {

		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, "{\n    \"collectionName\":\"nklpbnfw\" ,\n    \"searchText\":\"罗安东.\",\n    \"topK\":9\n}");
		Request request = new Request.Builder()
				.url("http://alinesno-infra-base-search-boot.beta.base.infra.linesno.com/api/base/search/vectorSearch/search")
				.method("POST", body)
				.addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
				.addHeader("Content-Type", "application/json")
				.addHeader("Accept", "*/*")
				.addHeader("Host", "alinesno-infra-base-search-boot.beta.base.infra.linesno.com")
				.addHeader("Connection", "keep-alive")
				.build();
		Response response = client.newCall(request).execute();
		System.out.println(response);

//		SpringApplication.run(BaseSearchApplication.class, args);
	}

}