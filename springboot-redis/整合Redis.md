### 构建
IDEA初始化选择 web  Spring Data Redis

或者pom直接添加
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
#### 配置连接 
在 application.properties
```
spring.redis.host=192.168.1.107
```
有设置密码的增加 spring.redis.password=

#### 编码
数据访问层
```
@Repository
public class RedisDao {

    @Autowired
    private StringRedisTemplate template;

    public void setKey(String key ,String value){
        ValueOperations<String,String> ops = template.opsForValue();
        ops.set(key,value,1, TimeUnit.MINUTES);//1分钟过期处理
    }

    public String getValue(String key){
        ValueOperations<String,String> ops = template.opsForValue();
        return ops.get(key);
    }
}
```

#### 测试
```
@SpringBootTest
class SpringbootRedisApplicationTests {

    public static Logger logger= LoggerFactory.getLogger(SpringbootRedisApplicationTests.class);

    @Test
    void contextLoads() {
    }


    @Autowired
    RedisDao redisDao;
    @Test
    public void testRedis(){
        redisDao.setKey("name","xiao");
        redisDao.setKey("age","11");
        logger.info(redisDao.getValue("name"));
        logger.info(redisDao.getValue("age"));
    }
}
```


最后控制台打印，测试通过
```
c.x.s.SpringbootRedisApplicationTests    : xiao
c.x.s.SpringbootRedisApplicationTests    : 11
```