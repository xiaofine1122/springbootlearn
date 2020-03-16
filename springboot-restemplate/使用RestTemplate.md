### 简介
用 Spring  RestTemplate 消费一个 Restful的web服务。
我将用restTemplate去消费一个服务： http://gturnquist-quoters.cfapps.io/api/random.

### 构建
IDEA 新建 选择web

通过RestTemplate消费服务，需要先context中注册一个RestTemplate bean。代码如下：
```
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {
	return builder.build();
}

@Bean
public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
	return args -> {
		String quote = restTemplate.getForObject(
				"http://gturnquist-quoters.cfapps.io/api/random", String.class);
		log.info(quote.toString());
	};
}
```

#### 测试
运行程序
控制台输出如下，成功
```
{"type":"success","value":{"id":10,"quote":"Really loving Spring Boot, makes stand alone Spring apps easy."}}
```

#### 官方文档
https://spring.io/guides/gs/consuming-rest/





