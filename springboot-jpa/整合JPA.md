#### 简介
JPA全称Java Persistence API.JPA通过JDK 5.0注解或XML描述对象－关系表的映射关系，并将运行期的实体对象持久化到数据库中。

JPA 的目标之一是制定一个可以由很多供应商实现的API，并且开发人员可以编码来实现该API，而不是使用私有供应商特有的API。

JPA是需要Provider来实现其功能的，Hibernate就是JPA Provider中很强的一个，应该说无人能出其右。从功能上来说，JPA就是Hibernate功能的一个子集。

### 构建

#### 初始化
选择 web SpringDataJPA  Mysql

或者maven引入
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```

最后添加druid连接池
```
<dependency>
	<groupId>com.alibaba</groupId>
	<artifactId>druid</artifactId>
	<version>1.1.21</version>
</dependency>
```

#### 配置文件 
application.yml
```
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://192.168.1.107:3306/entity?serverTimezone=GMT%2B8&useSSL=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

**注意**，如果通过jpa在数据库中建表，将jpa.hibernate,ddl-auto改为create，建完表之后，要改为update,要不然每次重启工程会删除表并新建。

#### 编码
```
@Entity
public class User {

    //@Id表明id， @GeneratedValue 字段自动生成
    @Id
    @GeneratedValue
    private int id;

    private String name ;

    private String email;
    
    //省略了setter getter tostring
}
```
dao
```
public interface UserDao extends JpaRepository<User,Integer> {}
```
省略了server层 实际开发时不能省略

controller
```
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String addUser(@RequestParam(value = "name")String name,
                   @RequestParam(value = "email")String email){
        User user =new User();
        user.setName(name);
        user.setEmail(email);

        User user1 = userDao.saveAndFlush(user);
        return user1.toString();
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") int id ,
                             @RequestParam(value = "name",required = true)String name,
                             @RequestParam(value = "email",required = true) String email){
        User user =new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        User user1 = userDao.saveAndFlush(user);
        return user1.toString();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") int id){
        //        return .orElse(null).toString();

        return userDao.findById(id).orElse(null);
//        Optional<User> user = userDao.findById(id);
//        if (user.isPresent()){
//            return user.get();
//        }else {
//            return null;
//        }
    }

    @RequestMapping(value="list",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.findAll();
    }
}
```
#### 测试
使用postman 或者IDEA自带的 IDEA REST Client



##### 问题1：
在2.0以上版本中，CrudRepository接口的findOne(T id)方法已经被移除。
所以使用 Optional<User> user = userDao.findById(id);
Optional 需要判断是否为空使用 .isPresent()
.get() 提取其中的对象
```
Optional<User> user = userDao.findById(id);
if (user.isPresent()){   
return user.get();
}else {    
return null;
}
```
或者
```
userDao.findById(id).orElse(null);
```




