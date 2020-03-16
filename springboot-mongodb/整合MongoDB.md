### 构建
IDEA新建项目是选择 web、 Spring Data MongoDB

或者直接引入依赖
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

#### 配置文件
application.properties
如果mongodb端口是默认端口，并且没有设置密码，可不配置，sprinboot会开启默认的。
```
spring.data.mongodb.uri=mongodb://localhost:27017/springboot-db
```

mongodb设置了密码，这样配置：
```
spring.data.mongodb.uri=mongodb://name:pass@localhost:27017/dbname
```
#### 编码
实体类：
```
public class Customer {

    @Id
    public  String id;

    public  String firstName;
    public  String lastName;

    public Customer(String firstName,String lastName){
        this.firstName=firstName;
        this.lastName=lastName;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
```
dao层
```
public interface CustomerRepository extends MongoRepository<Customer,String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);

}
```
写一个接口，继承MongoRepository，这个接口有了几本的CURD的功能。如果你想自定义一些查询，比如根据firstName来查询，获取根据lastName来查询，只需要定义一个方法即可。注意firstName严格按照存入的mongodb的字段对应。在典型的java的应用程序，写这样一个接口的方法，需要自己实现，但是在springboot中，你只需要按照格式写一个接口名和对应的参数就可以了，因为springboot已经帮你实现了。



#### 测试
```
@SpringBootApplication
public class SpringbootMongodbApplication implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMongodbApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        customerRepository.deleteAll();

        customerRepository.save(new Customer("Alice","Smith"));
        customerRepository.save(new Customer("Bob","Smith"));
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : customerRepository.findAll()) {
            System.out.println(customer);
        }

        System.out.println();


        System.out.println("Customers found with findByFirstName():");
        System.out.println("-------------------------------");
        System.out.println(customerRepository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName():");
        System.out.println("-------------------------------");
        for(Customer customer : customerRepository.findByLastName("Smith")){
            System.out.println(customer);
        }
    }
}
```


启动程序  控制台打印 测试通过
```
Customers found with findAll():
-------------------------------
Customer{id='5e6c8c818ff8a52061ada1e4', firstName='Alice', lastName='Smith'}
Customer{id='5e6c8c818ff8a52061ada1e5', firstName='Bob', lastName='Smith'}

Customers found with findByFirstName():
-------------------------------
Customer{id='5e6c8c818ff8a52061ada1e4', firstName='Alice', lastName='Smith'}
Customers found with findByLastName():
-------------------------------
Customer{id='5e6c8c818ff8a52061ada1e4', firstName='Alice', lastName='Smith'}
Customer{id='5e6c8c818ff8a52061ada1e5', firstName='Bob', lastName='Smith'}
```


##### 问题1
MongoDB 我直接设置了密码访问，并且授权不正确 导致 not authorized 没有授权的错误
解决：创建用户时 指定正确的权限（role）
```
db.createUser({user:"lwb",pwd:"lwb",roles:[{role:"readWrite",db:"demo"}]})
```
