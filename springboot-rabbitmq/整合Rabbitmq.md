#### 简介
整合RabbitMQ服务器，并且通过它怎么去发送和接收消息。我将构建一个springboot工程，通过RabbitTemplate去通过MessageListenerAdapter去订阅一个POJO类型的消息。

### 构建

IDEA 新建选择 Messaging RabbitMQ

或者引入依赖
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```
#### 配置连接RabbitMQ
```
spring.rabbitmq.host=192.168.1.107
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

##### 创建消息接收者
在任何的消息队列程序中，你需要创建一个消息接收者，用于响应发送的消息。
```
@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);
    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }
    public CountDownLatch getLatch() {
        return latch;
    }
}
```

消息接收者是一个简单的POJO类，它定义了一个方法去接收消息，当你注册它去接收消息，你可以给它取任何的名字。其中，它有CountDownLatch这样的一个类，它是用于告诉发送者消息已经收到了，你不需要在应用程序中具体实现它，只需要latch.countDown()就行了。


#####  创建消息监听，并发送一条消息

在spring程序中，RabbitTemplate提供了发送消息和接收消息的所有方法。你只需简单的配置下就行了：
* 需要一个消息监听容器
* 声明一个quene,一个exchange,并且绑定它们
* 一个组件去发送消息
```
@SpringBootApplication
public class SpringbootRabbitmqApplication {

    static final String topicExchangeName = "spring-boot-exchange";

    static final String queueName = "spring-boot";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SpringbootRabbitmqApplication.class, args);
    }

}
```
#### 创建测试方法
```
@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final ConfigurableApplicationContext context;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate,
                  ConfigurableApplicationContext context) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(SpringbootRabbitmqApplication.queueName, "Hello from RabbitMQ!");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        context.close();
    }
}
```
启动服务发现控制台输出，测试成功
```
Sending message...
Received <Hello from RabbitMQ!>
```

#### 官网地址

https://spring.io/guides/gs/messaging-rabbitmq/




