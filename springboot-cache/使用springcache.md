### 简介
##### 声明式缓存
Spring 定义 CacheManager 和 Cache 接口用来统一不同的缓存技术。例如 JCache、 EhCache、 Hazelcast、 Guava、 Redis 等。在使用 Spring 集成 Cache 的时候，我们需要注册实现的 CacheManager 的 Bean。

Spring Boot 为我们自动配置了 JcacheCacheConfiguration、 EhCacheCacheConfiguration、HazelcastCacheConfiguration、GuavaCacheConfiguration、RedisCacheConfiguration、SimpleCacheConfiguration 等。

在我们不使用其他第三方缓存依赖的时候，springboot自动采用ConcurrenMapCacheManager作为缓存管理器。

在我们不使用其他第三方缓存依赖的时候，springboot自动采用ConcurrenMapCacheManager作为缓存管理器。

### 构建

IDEA新建 勾选 I/O下 Spring cache abstraction

或者直接引入依赖
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

#### 编码
先创建一个实体类
 
```
 public class Book {

    private String isbn;
    private String title;

    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }
  ....getter
  ....setter  
 
 }
```
创建一个数据访问接口
```
public interface BookRepository {

    Book getByIsbn(String isbn);

}
```

这个你可以写一个很复杂的数据查询操作，比如操作mysql、nosql等等。为了演示这个栗子，我只做了一下线程的延迟操作，当作是查询数据库的时间。


实现接口类：
```
@Component
public class SimpleBookRepository implements BookRepository {

    @Override
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        return new Book(isbn, "Some book");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
```
测试类
```
@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final BookRepository bookRepository;

    public AppRunner(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info(".... Fetching books");
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
    }
}
```
#### 测试

之后启动主程序 SpringbootCacheApplication

控制台输出
```
2020-03-15 10:00:34.524  INFO ...: .... Fetching books
2020-03-15 10:00:37.524  INFO ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:00:40.525  INFO ...: isbn-4567 -->Book{isbn='isbn-4567', title='Some book'}
2020-03-15 10:00:43.526  INFO ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:00:46.527  INFO ...: isbn-4567 -->Book{isbn='isbn-4567', title='Some book'}
2020-03-15 10:00:49.528  INFO ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:00:52.529  INFO ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
```


在程序的入口中加入@ EnableCaching开启缓存技术：
```
@SpringBootApplication
@EnableCaching
public class SpringbootCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCacheApplication.class, args);
    }
}
```

在需要缓存的地方加入@Cacheable注解，比如在SimpleBookRepository

.getByIsbn（）方法上加入@Cacheable(“books”)，这个方法就开启了缓存策略，当缓存有这个数据的时候，会直接返回数据，不会等待去查询数据库。
```
 @Override
    @Cacheable("books")
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        return new Book(isbn, "Some book");
    }
```

这时再启动程序，你会发现程序打印：
```
2020-03-15 10:03:09.559  INFO 1704 --- [main] ...: .... Fetching books
2020-03-15 10:03:12.564  INFO 1704 --- [main] ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:03:15.565  INFO 1704 --- [main] ...: isbn-4567 -->Book{isbn='isbn-4567', title='Some book'}
2020-03-15 10:03:15.566  INFO 1704 --- [main] ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:03:15.566  INFO 1704 --- [main] ...: isbn-4567 -->Book{isbn='isbn-4567', title='Some book'}
2020-03-15 10:03:15.566  INFO 1704 --- [main] ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
2020-03-15 10:03:15.566  INFO 1704 --- [main] ...: isbn-1234 -->Book{isbn='isbn-1234', title='Some book'}
```

只有打印前面2个数据，程序等了3s，之后的数据瞬间打印在控制台上了，这说明缓存起了作用。


#### 官方文档：
https://spring.io/guides/gs/caching/