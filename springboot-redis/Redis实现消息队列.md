#### 前提
项目在之前的集成redis基础上

### 构建

#### 引入的依赖
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
#### 编码
创建一个消息接收者

```
public class Receiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    private AtomicInteger counter = new AtomicInteger();

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
        counter.incrementAndGet();
    }

    public int getCount() {
        return counter.get();
    }
}
```

注入消息接收者
```
@Bean
Receiver receiver() {
	return new Receiver();
}

@Bean
StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
	return new StringRedisTemplate(connectionFactory);
}
```
在spring data redis中，利用redis发送一条消息和接受一条消息，需要三样东西：
* 一个连接工厂
* 一个消息监听容器
* Redis template

注入消息监听容器
```
@Bean
RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
										MessageListenerAdapter listenerAdapter) {

	RedisMessageListenerContainer container = new RedisMessageListenerContainer();
	container.setConnectionFactory(connectionFactory);
	container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

	return container;
}
```
#### 测试代码
在springboot入口的main方法：
```
public static void main(String[] args) throws Exception {
	ApplicationContext ctx = SpringApplication.run(SpringbootRedisApplication.class, args);

	StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
	Receiver receiver = ctx.getBean(Receiver.class);

	while (receiver.getCount() == 0) {

		LOGGER.info("Sending message...");
		template.convertAndSend("chat", "Hello from Redis!");
		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	System.exit(0);
}
```

先用redisTemplate发送一条消息，接收者接收到后，打印出来。启动springboot程序，控制台打印：
```
2020-03-15 10:34:24.154  INFO 1028 --- [           main] c.x.s.SpringbootRedisApplication         : Sending message...
2020-03-15 10:34:24.165  INFO 1028 --- [    container-2] c.x.springbootredis.message.Receiver     : Received <Hello from Redis!>
2020-03-15 10:34:24.672  INFO 1028 --- [extShutdownHook] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'

```

测试通过，接收者确实接收到了发送者的消息。



#### 官方文档
https://spring.io/guides/gs/messaging-redis/