### 简介
主要介绍如何在springboot中如何创建含有多个module的工程，栗子中含有两个 module，一个作为libarary. 工程，另外一个是主工程，调用libary .其中libary jar有一个服务，main工程调用这个服务。

### 构建
##### 创建根工程
创建一个maven 工程,其pom文件为：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.xiaofine</groupId>
    <artifactId>springboot-multi-module</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>library</module>
        <module>application</module>
    </modules>

</project>
```

创建后项目上右键 new -》 module
建立 library 和  application 工程

library工程按照默认springboot 的新建
建立之后去掉 POM中的下面部分 告诉Maven 不为library项目构建可执行的jar
```
<build>
	<plugins>
		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
</build>
```
修改POM中
```
<artifactId>multi-module-library</artifactId>
```


删掉
LibraryApplication 启动类

library下新建类
```
package com.xiaofine.multimodele.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@ConfigurationProperties("service")
public class ServiceProperties {

    /**
     * A message for the service.
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

```

```
@Service
@EnableConfigurationProperties(ServiceProperties.class)
public class MyService {

    private final ServiceProperties serviceProperties;

    public MyService(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

    public String message() {
        return this.serviceProperties.getMessage();
    }
}

```


设置 Application  项目

Application项目需要依赖Library项目。需要相应地修改Application 的POM文件
增加下面依赖
```
<dependency>
	<groupId>com.xiaofine</groupId>
	<artifactId>multi-module-library</artifactId>
	<version>${project.version}</version>
</dependency>
```

修改启动类 Application
引入依赖，创建web服务

```
@SpringBootApplication(scanBasePackages = "com.xiaofine.multimodele")
@RestController
public class Application {

    private final MyService myService;

    public Application(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/")
    public String home() {
        return myService.message();
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```

配置 Application 项目下的 application.properties 加入
```
service.message=Hello, World
```
#### 测试
启动 Application 下的 Application 类 
访问  http://localhost:8080/
![187252da36a9899e92a967bb6a55b6c3.png](en-resource://database/1247:1)

说明确实引用了libary中的方法。


