#### 准备
先启动mysql
mysql版本为： 8.0.16 MySQL Community Server

### 构建
IDEA新建项目是选择 web  JDBC MYSQL

或者直接maven添加依赖
```
<dependency>
<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
	<groupId>mysql</groupId>
	<artifactId>mysql-connector-java</artifactId>
	<scope>runtime</scope>
</dependency>
```
最后添加druid 连接池
```
<dependency>    
    <groupId>com.alibaba</groupId>    
    <artifactId>druid </artifactId>    
    <version>1.1.21</version>
</dependency>
```


#### 配置连接属性 
application.yml
```
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://192.168.1.107:3306/entity?serverTimezone=GMT%2B8&useSSL=false
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
```
#### 具体编码
dao server controller三层结构

模型类
```
public class User {

    private int id;

    private String name ;

    private String email;

    省略 setter getter toString
}
```

dao层
```
public interface UserDao {

    int add(User user);

    int update(User user);

    int delete(int id);

    User findUserById(int id);

    List<User> findAllUsers();
}
```
dao实现类
```
@Repository
public class UserDaoImpl implements UserDao {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        return jdbcTemplate.update("insert into user(name,email) values(?,?)",user.getName(),user.getEmail());
    }

    @Override
    public int update(User user) {
        return jdbcTemplate.update("update user SET name =?,email=? WHERE id=?",user.getName(),user.getEmail(),user.getId());
    }

    @Override
    public int delete(int id) {
        return jdbcTemplate.update("DELETE from user where id=?",id);
    }

    @Override
    public User findUserById(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM user where id =?",new Object[]{id}, new BeanPropertyRowMapper(User.class));
        if(list!=null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> list = jdbcTemplate.query("SELECT * FROM user ",new BeanPropertyRowMapper(User.class));
        if(list!=null && list.size()>0){
            return list;
        }else{
            return null;
        }
    }
}
```

server层
```
public interface UserService {

    int add(User user);

    int update(User user);

    int delete(int id);

    User findUserById(int id);

    List<User> findAllUsers();
}
```
server层实现类
```
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;


    @Override
    public int add(User user) {
        return userDao.add(user);
    }

    @Override
    public int update(User user) {
        return userDao.update(user);
    }

    @Override
    public int delete(int id) {
        return userDao.delete(id);
    }

    @Override
    public User findUserById(int id) {
        return userDao.findUserById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
}
```

controller  
restful构建api的风格

```
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public String addUser(@RequestParam(value = "name")String name,
                   @RequestParam(value = "email")String email){
        User user =new User();
        user.setName(name);
        user.setEmail(email);

        int t = userService.add(user);
        if(t==1){
            return user.toString();
        }else {
            return "fail";
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") int id ,
                             @RequestParam(value = "name",required = true)String name,
                             @RequestParam(value = "email",required = true) String email){
        User user =new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        int t = userService.update(user);
        if(t==1){
            return user.toString();
        }else {
            return "fail";
        }
    }

    //默认不写是GET请求
    @RequestMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") int id){
        int t = userService.delete(id);
        if(t==1){
            return "succese delete"+id;
        }else {
            return "fail";
        }
    }


    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public User getUserById(@PathVariable("id") int id){
        return userService.findUserById(id);
    }

    @RequestMapping(value="list",method = RequestMethod.GET)
    public List<User> getUsers(){
        return userService.findAllUsers();
    }
}
```
#### 测试
使用postman 或者IDEA自带的 IDEA REST Client


##### 问题1：
连接mysql8以上需要使用
com.mysql.cj.jdbc.Driver
url需要指定时区serverTimezone=GMT
```
jdbc:mysql://192.168.168.101:3306/mysql?serverTimezone=GMT%2B8&useSSL=false
```
如果设定serverTimezone=UTC，会比中国时间早8个小时，如果在中国，可以选择Asia/Shanghai或者Asia/Hongkong，或GMT%2B8







