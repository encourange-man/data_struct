# 一、Java基础

## 1. Java8

s

# 二、SpringBoot

此学习笔记是参考与springboot2.1.2官方文档的，然后他的系统需要：

|          | 版本              |
| -------- | ----------------- |
| Maven    | 3.3以上           |
| Java SDK | Java8，兼容Java11 |
| Tomcat 9 | 4.0               |
| Jetty9.4 | 3.1               |

我们都使用Maven来管理包结构，新创建的springboot的pom.xml都有什么内容

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0                   	           							http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    
    <groupId>com.machen</groupId>
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>hellowrold</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```

​	springboot提供了一系列的```starters```来添加你的```jars```到```classpath```，所以只需要导入相应的starter，比如导入开发web项目，导入web环境，只需要导入```spring-boot-starter-web```，其中就会包含web环境所需要的依赖，就像下面这样。

​	每个项目在创建好之后都已经导入```spring-boot-starter-parent```在你的pom文件中，这是一个特殊的starter，它提供了一些很有用的maven的默认配置，

```xml
<!--spring-boot-starter-web中的依赖 -->
<dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <version>2.1.1.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-json</artifactId>
      <version>2.1.1.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
      <version>2.1.1.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.hibernate.validator</groupId>
      <artifactId>hibernate-validator</artifactId>
      <version>6.0.13.Final</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>5.1.3.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.1.3.RELEASE</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```

# 1.  构建第一个项目

```java
@RestController
@EnableAutoConfiguration
public class HellowroldApplication {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(HellowroldApplication.class, args);
    }
}
```

## **1.1  @RestController**

**就是@Controller和@ResponseBody的结合**，作用就是直接将```String```类型的返回值，直接返回给请求者。就像上面的代码一样，页面就会显示“Hello World”的字段。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller          //这是一个Controller-bean
@ResponseBody        //将返回结果直接返回给请求者
public @interface RestController {
```



## 1.2  @SpringBootApplication

```java
/**
 *  @SpringBootApplication 来标注一个主程序类，说明这是一个Spring Boot应用
 */
@SpringBootApplication
public class HelloWorldMainApplication {

    public static void main(String[] args) {
        // Spring应用启动起来
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}
```

@**SpringBootApplication**: 

​	  Spring Boot应用标注在某个类上说明这个类是SpringBoot的主配置类，SpringBoot就应该运行这个类的main方法来启动SpringBoot应用.



```@SpringbootApplication```相当于```@Configuration，``````@EnableAutoConfiguration```和``` @ComponentScan``` 并具有他们的默认属性值。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
		@Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

```



## 1.3  @EnableAutoConfiguration

作用：**开启自动配置功能**。这是class和interface级别的注解.

比如在开发web项目时，如果使用spring框架，需要一些配置文件，而这个注解时告诉springboot你想如何配置spring，它是基于你添加的jar依赖项。如果```spring-boot-starter-web```已经添加Tomcat和Spring MVC,这个注释自动将假设您正在开发一个web应用程序并添加相应的spring设置 。

```java
@AutoConfigurationPackage
@Import(EnableAutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
    .......
}
```

## **1.4  @AutoConfigurationPackage**

作用：自动配置包

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {

}
```

**@Import(AutoConfigurationPackages.Registrar.class)**

​	@Import是给容器中导入一个组件，将会调用AutoConfigurationPackages中的**Registrar方法**，目的是：**将主配置类（@SpringBootApplication标注的类）的所在包及下面所有子包里面的所有组件扫描到Spring容器**。



### @Import(EnableAutoConfigurationImportSelector.class)

​	开启自动配置导入选择器，将所有需要导入的组件以全类名的方式返回，这些组件就会被添加到容器中。

​	会给容器中导入非常多的自动配置类（xxxAutoConfiguration），就是给容器中导入这个场景需要的所有组件，并配置好这些组件。有了自动配置类，免去了我们手动编写配置注入功能组件等的工作![](/images/20180129224104.png)

​	Spring Boot在启动的时候从类路径下的META-INF/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置类导入到容器中，自动配置类就生效，帮我们进行自动配置工作；以前我们需要自己配置的东西，自动配置类都帮我们完成了。



## 主程序类（入口类）的位置

​	spring通常建议我们将```main```方法**所在的类放到一个root包**下,```@EnableAutoConfiguration```（开启自动配置）注解通常都放到```main```所在类的上面，下面是一个典型的结构布局 :

![](/images/2018-12-24_170718.png)



# 2、使用Springboot

## 2.1、spring-boot-starter-parent

创建springboot项目一般都会继承```spring-boot-starter-parent```pom文件，这个文件内定义了一下Maven的默认属性，资源过滤器和插件等。比如：**java默认使用1.8版本**，使用**UTF-8**作为编码集。

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>1.5.9.RELEASE</version>
</parent>

他的父项目是
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-dependencies</artifactId>
  <version>1.5.9.RELEASE</version>
  <relativePath>../../spring-boot-dependencies</relativePath>
</parent>
他来真正管理Spring Boot应用里面的所有依赖版本；
```



## 2.2、资源过滤配置

​	使用springboot 不再像使用spring框架那样，需要定义一些列的xml文件，springboot的配置都是写下**配置文件中的**。springboot支持两种配置文件，一种是```properties```，另一种是```yml```文件。下面是spring-boot-starter-parent.xml中的资源过滤的配置，其中定义了配置文件的location和配置文件的名称。

```xml
<resources>
    <resource>
        <filtering>true</filtering>
        <directory>${basedir}/src/main/resources</directory>
        <includes>
            <include>**/application*.yml</include>
            <include>**/application*.yaml</include>
            <include>**/application*.properties</include>
        </includes>
    </resource>
    <resource>
        <directory>${basedir}/src/main/resources</directory>
        <excludes>
            <exclude>**/application*.yml</exclude>
            <exclude>**/application*.yaml</exclude>
            <exclude>**/application*.properties</exclude>
        </excludes>
    </resource>
 </resources>
```

​	在真正开发中，可能需要不知一个配置文件。比如：开发、测试、上线都可能有个配置文件。但是springboot默认的配置文件是```application.properties```，可以在其中定义一些共有的配置属性，还可以在其中配置我们不同环境所需要的配置文件，比如下面这样：

```properties
spring.profiles.active=dev
```

这就会激活```application-dev.properties```配置文件，注意这个配置文件需要和```application.properties```在同一个目录级别下。

## 2.3、如何不继承parent.Pom

 并不是所有人都需要继承```spring-boot-starter-parent```，可能会用到公司自己定义的或者自己定义maven配置。

??????

## 2.4、@Configuration配置类

SpringBoot使用 **配置类《====》xml配置文件**。如果想要配置文件生效，需要使用```@Configuration```注解。

​	 不用将你的所有的```@Configuration```导入到一个类中，可以使用```@Import```注解来**导入配置类**。或者可以使用```@ComponentScan```来**自动扫描所有的spring组件**，当然包括```@Configuration```配置类。

​	如果必须要**导入基于xml的配置文件**，可以使用```@ImportResource```注解。



## 2.5、@ComponentScanner

​	Spring是一个依赖注入框架。所有的内容都是关于bean的定义及其依赖关系。定义Spring Beans的第一步是使用正确的注解```@Component```或```@Service```或```@Repository```。但是，Spring不知道你定义了某个bean除非它知道从哪里可以找到这个bean。

- **```@ComponentScan```做的事情就是告诉Spring从哪里找到bean。**
- 如果你的其他包都在使用了@SpringBootApplication注解的main app所在的包及其下级包，则**你什么都不用做**，SpringBoot会自动帮你把其他包都扫描了
- 如果你有一些bean所在的包，不在main app的包及其下级包，那么你需要手动加上```@ComponentScan```注解并指定那个bean所在的包

```java

/*
	@ComponentScan:扫描“com.machen.xxx”下的所有组件
*/
@ComponentScan("com.machen")
@SpringBootApplication
public class HellowroldApplication {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HellowroldApplication.class, args);

        System.out.println(run.getBean("helloController"));
        System.out.println(run.getBean("helloService" ));
    }
}
```

![](/images/2018-12-25_164800.png)

​	

**所有组件默认的别名都是以其名字的开头小写字母。**



## 2.6、Springboot Devtools介绍

结合博客和案例代码试试

？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？



## 2.7、resources文件夹中目录结构

- **static**：保存所有的静态资源；例如js css  images。

- **templates**：保存所有的模板页面。Spring Boot默认jar包使用嵌入式的Tomcat，默认不支持JSP页面，但是可以使用模板引擎（freemarker、thymeleaf）

- **application.properties**：Spring Boot应用的配置文件，可以修改一些默认设置。

  

  **默认服务器访问的地址是8080，如何修改这个端口号？**

  ```properties
  # 项目的根路径
  server.servlet.context-path=/machen
  # 服务器访问端口号
  server.port=8080
  ```

  

# 3、配置文件

SpringBoot使用一个全局的配置文件，配置**文件名是固定的**，默认使用两种配置文件；

​	•   **application.properties**

​	•   **application.yml**

**配置文件的作用**：修改SpringBoot自动配置的默认值；SpringBoot在底层都给我们自动配置好。



## 3.1、yaml

 yml是YAML（YAML Ain't Markup Language）语言的文件，以数据为中心，比json、xml更适合做配置文件

​	YAML ：是一个标记语言

​	YAML  不是一个标记语言；

标记语言：

​	以前的配置文件；大多都使用的是  **xxxx.xml**文件；

​	YAML：**以数据为中心**，比json、xml等更适合做配置文件；

**YAML配置例子：**

```yaml
server:
  port: 8081
```

相比如xml的配置文件：

```xml
<server>
	<port>8081</port>
</server>
```

### 3.1.1、yaml基本语法

`k:(空格)v`：表示一对键值对（空格必须有）；

以**空格**的缩进来控制层级关系；只要是左对齐的一列数据，都是同一个层级的。

```yaml
server:
    port: 8081
    path: /hello
```

注意：**属性和值也是大小写敏感**。

### 3.1.2、值的写法

#### 3.1.2.1、字面量：普通的值

k: v：字面直接来写，字符串默认不用加上单引号或者双引号；

​	" "：双引号：**不会转义字符串里面的特殊字符**；特殊字符会作为本身想表示的意思

​				name:   "zhangsan \n lisi"：输出；zhangsan 换行  lisi

​	'  '：单引号：**会转义特殊字符**，特殊字符最终只是一个普通的字符串数据

​				name:   ‘zhangsan \n lisi’：输出；zhangsan \n  lisi



#### 3.1.2.2、对象、Map(属性和值)(键值对)

k: v：在下一行来写对象的属性和值的关系；注意缩进

​		对象还是k: v的方式

```yaml
friends:
		lastName: zhangsan
		age: 20
```

**行内写法**：

```yaml
friends: {lastName: zhangsan,age: 18}
```

#### 3.1.2.3、数组(List、Set)

用   **-**  值表示数组中的一个元素

```yaml
pets:
 - cat
 - dog
 - pig
```

**行内写法**：

```yaml
pets: [cat,dog,pig]
```

### 3.1.3、yml配置文件注入

yaml配置文件：

```yaml
person:
    lastName: hello
    age: 18
    boss: false
    birth: 2017/12/12
    maps: {k1: v1,k2: 12}
    lists:
      - lisi
      - zhaoliu
    dog:
      name: 小狗
      age: 12
```

JavaBean：

```java
/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *      prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 *
 * 只有这个组件是容器中的组件Component，才能容器提供的@ConfigurationProperties功能；
 */
@Component
@ConfigurationProperties(prefix = "person")
public class Person {

    private String lastName;
    private Integer age;
    private Boolean boss;
    private Date birth;

    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
```

导入**配置文件处理器**，编写配置的时候会有提示信息

```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

### 3.1.4、yml多文档块方式

在yml中使用**多文档块**的方式，使用```---``` 来区分不同的文档块，可以使用如下的方式激活不同的文档块：

```yaml
spring:
	profiles:
	  active: dev
```

```yaml
# 文档块1
server:
  port: 8081
# 指定激活的文档块
spring:
  profiles:
    active: prod

---
# 文档块2
server:
  port: 8083
spring:
  profiles: dev


---
# 文档块3
server:
  port: 8084
spring:
  profiles: prod  #指定属于哪个环境
```



### 3.1.5、properties配置文件乱码问题

![idea配置乱码](images/搜狗截图20180130161620.png)

### 3.1.6、@Value和@ConfigurationProperties比较

​	在xml配置中，经常使用**value**来给属性赋值，例如这样，当然在SpringBoot中也可以使用注解**@Value**的方式来给属性赋值。

``` xml
<bean id="persion" class="">
    <property name="age" value="18"></property>
</bean>
```

|                      | @ConfigurationProperties | @Value     |
| -------------------- | ------------------------ | ---------- |
| 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法） | 支持                     | 不支持     |
| SpEL                 | 不支持                   | 支持       |
| **JSR303数据校验**   | 支持                     | 不支持     |
| 复杂类型封装         | 支持                     | 不支持     |

​	bu不管配置文件是yml还是properties，他们都能获取值。但是，**如果我们只是在某个业务逻辑中需要获取一下配置文件中的某个值**，还是建议使用 ```@Value

··  @ConfigurationProperties  @Value  功能  批量注入配置文件中的属性  一个个指定  松散绑定（松散语法）  支持  不支持  SpEL  不支持  支持  **JSR303数据校验**?????????  支持  不支持  复杂类型封装  支持  不支持

```java
/**
 * RestController : 相当于就是Controller和ResponseBody
 */
@RestController
public class HelloController {

    @Value("${person.name}")
    private String name;

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){
        return "Hello "+name;
    }

}
```

### 3.1.7、配置文件参数校验@Validated

```java
/**
     * <bean class="Person">
     *      <property name="lastName" value="字面量/${key}从环境变量、配置文件中获取值/#{SpEL}"/>
     * <bean/>
 */
@Component
@ConfigurationProperties(prefix = "person")
@Validated
public class Person {

   //lastName必须是邮箱格式
    @Email
    //@Value("${person.last-name}")
    private String lastName;
    //@Value("#{11*2}")
    private Integer age;
    //@Value("true")
    private Boolean boss;

    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;
```

​	如果说，我们专门编写了一个javaBean来和配置文件进行映射，我们就直接使用```@ConfigurationProperties```，另外```@ConfigurationProperties```默认从全局的配置文件中获取值，如果要从局部的配置文件获取值，就要是使用**@PropertySource**来加载局部的配置文件。

## 3.2、@PropertySource

作用：**加载指定的配置文件**。

```java
/**
 *@PropertySource：加载指定的配置文件
 */
@PropertySource(value = {"classpath:person.properties"})
@Component
@ConfigurationProperties(prefix = "person")
//@Validated
public class Person {
    //lastName必须是邮箱格式
   // @Email
    //@Value("${person.last-name}")
    private String lastName;
    //@Value("#{11*2}")
    private Integer age;
    //@Value("true")
    private Boolean boss;
```

## 3.3、@ImportResource

作用：导入Spring的配置文件，让配置文件里面的内容生效；

Spring Boot里面没有Spring的配置文件，我们自己编写的配置文件，也不能自动识别，如果想让Spring的配置文件生效，加载进来；@**ImportResource**标注在一个配置类上，例如这样：

```java
@ImportResource(locations = {"classpath:beans.xml"})
导入Spring的配置文件让其生效
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">


    <bean id="helloService" class="com.atguigu.springboot.service.HelloService"></bean>
</beans>
```

是在SpringBoot中推荐使用全注解的方式，所以使用 **注解类《===》配置文件**

1、配置类**@Configuration**------>Spring配置文件

2、使用**@Bean**给容器中添加组件

```java
/**
 * @Configuration：指明当前类是一个配置类；就是来替代之前的Spring配置文件
 * 使用@Bean来代替<bean>标签，其返回值会添加到容器中去，默认使用方法名作为组件的id
 */
@Configuration
public class MyAppConfig {

    //将方法的返回值添加到容器中；容器中这个组件默认的id就是方法名
    @Bean
    public HelloService helloService(){
        System.out.println("配置类@Bean给容器中添加组件了...");
        return new HelloService();
    }
}
```

## 3.4、Profile

​	profile是Spring对不同环境提供不同配置功能的支持，比如开发使用开发环境的配置，发布使用发布环境，测试使用测试环境。

​	我们在主配置文件编写的时候，文件名可以是   **application-{profile}.properties/yml**

**默认使用application.properties的配置；**

### 3.4.1、如何激活指定的Profile

- 在配置文件中指定  **spring.profiles.active=dev**

```properties
server.port=8081
# 激活application-dev.properties的配置文件
spring.profiles.active=dev
```

命令行

```java
java -jar spring-boot-02-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

虚拟机参数

```properties
-Dspring.profiles.active=dev
```

![](/images/1544169257(1).png)









## 3.4、配置文件占位符

 ### 3.4.1、随机数

```java
${random.value}、${random.int}、${random.long}
${random.int(10)}、${random.int[1024,65536]}
```

### 3.4.2、占位符获取之前配置的值

占位符可以获取之前配置的值，如果没有可以是用 **:** 指定默认值

```properties
# 使用一个随机的UUID作为name
person.last-name=张三${random.uuid}
person.age=${random.int}
person.birth=2017/12/15
person.boss=false
person.maps.k1=v1
person.maps.k2=14
person.lists=a,b,c
# 获取之前配置中的值
perso.cat.name=${person.last-name}
# 如果hello没有值，就是使用默认值
person.dog.name=${person.hello:Tom}_dog
person.dog.age=15
```



## 3.5、配置文件加载位置

​	springboot 启动会扫描以下位置的 ```application.properties```或者```application.yml```文件作为Spring boot的默认配置文件。

```properties
# 根目录下的config文件夹下的application文件
–file:./config/
# 根目录下的application文件
–file:./
# 类路径下的config文件夹下的application文件
–classpath:/config/
# 类路径下的application文件
–classpath:/
```

**优先级由高到底**，**高优先级的配置会覆盖低优先级的配置**；

![](/images\1544170746(1).png)

 ### 3.5.1、改变默认配置文件的位置

我们还可以通过 ```spring.config.location``` 来改变默认的配置文件位置

```properties
server.servlet.context-path=/machen
server.port=8080
# 指定配置文件的位置
spring.config.location=/
```

**项目打包好以后，我们可以使用命令行参数的形式，启动项目的时候来指定配置文件的新位置；指定配置文件和默认加载的这些配置文件共同起作用形成互补配置；**

```properties
java -jar spring-boot-02-config-02-0.0.1-SNAPSHOT.jar --spring.config.location=G:/application.properties
```

## 3.6、外部配置文件加载顺序

**SpringBoot也可以从以下位置加载配置； 优先级从高到低；高优先级的配置覆盖低优先级的配置，所有的配置会形成互补配置**。

- **命令行参数**

  所有的配置都可以在命令行上进行指定

java -jar spring-boot-02-config-02-0.0.1-SNAPSHOT.jar --server.port=8087  --server.context-path=/abc

​	**多个配置用空格分开； --配置项=值**

- 来自java:comp/env的JNDI属性
- Java系统属性（System.getProperties()）
- 操作系统环境变量
- RandomValuePropertySource配置的random.*属性值



==**优先加载带profile**==

- **jar包外部的application-{profile}.properties或application.yml(带spring.profile)配置文件**
- **jar包内部的application-{profile}.properties或application.yml(带spring.profile)配置文件**



==**再来加载不带profile**==

- **jar包外部的application.properties或application.yml(不带spring.profile)配置文件**
- **jar包内部的application.properties或application.yml(不带spring.profile)配置文件**



- @Configuration注解类上的@PropertySource
- 通过SpringApplication.setDefaultProperties指定的默认属性



# 4、Springboot的特性

## 4.1、自定义的彩蛋

## 4.2、ApplicationRunner和ComandLineRunner 

如果想要在SpringApplication启动后执行一次特殊的代码，只需要继承ApplicationRunner或者ComandLineRunner接口，实现其run方法。‘

？？？？？？？？？？？？？？？？？？？？？？？？？？



# 5. SpringMVC MOCK测试

在web开发中，我们通常进行Service层服务的单元测试。有时候，我们也需要在Controller层进行接口测试，它有如下好处：

- 规范化接口测试。如果不在Controller层测试，开发人员往往需要使用浏览器或者Postman等工具访问接口，流程繁琐。
- 提高开发效率。如果不在Controller层测试，开发人员需要启动整个工程判断代码正确性。配置Controller层测试后，无需启动所有接口；使用StandAlone方式测试时，甚至无需启动web工程。
- 配置监控服务。可以配置监控进程，对所有接口进行周期性检测，发现接口异常时进行通知。



SpringMVC Test框架构建在spring-test模块的Servlet API模拟对象之上，因此不使用正在运行的Servlet容器。它使用DispatcherServlet提供完整的Spring MVC运行时行为。

Spring MVC Test的目标就是提供一个有效的方式去测试Controller，通过实际的DispatcherServlet去执行一个请求，并切生成相应的相应。



Spring提供两种形式的集成测试：

1. **独立测试方式**（**StandaloneMockMvcBuilder**）。

   ```java
   // 通过调用MockMvcBuilders.standaloneSetup()
   StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup();
   ```

   优点：运行快，无需启动web，不加载Spring的配置。

   缺点：对于依赖其他Component的接口，需要手动mock所依赖的Component，手动设置其返回收据，一次只能测试一个controller。

   ```java
   public class MyWebTests {
   
       private MockMvc mockMvc;
   
       @Before
       public void setup() {
           this.mockMvc = MockMvcBuilders.standaloneSetup(new AccountController()).build();
       }
   
       // ...
   }
   ```

   `standalongSetUp`更接近于单元测试，它一次测试一个`controller`，但是需要mock出Controller的依赖并且手动注入到Controller中，并且它不涉及加载Spring配置

   

2. **集成Web测试方式**（**DefaultMockMvcBuilder**）。

   ```java
   // 通过调用MockMvcBuilders.webAppContextSetup()
   DefaultMockMvcBuilder defaultMockMvcBuilder = MockMvcBuilders.webAppContextSetup();
   ```

   优点：加载了ApplicationContext，所依赖的各种Component已经注入，完全模拟运行时场景，无需手动造数据。

   缺点：相比于独立测试方式，运行较慢。

   ```java
   @RunWith(SpringRunner.class)
   @WebAppConfiguration
   @ContextConfiguration("my-servlet-context.xml")
   public class MyWebTests {
   
       @Autowired
       private WebApplicationContext wac;
   
       private MockMvc mockMvc;
   
       @Before
       public void setup() {
           this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
       }
   
       // ...
   }
   ```

需要做如下几步操作：

- 使用@ContextConfiguration注解，加载SpringMVC 的配置文件
- 注入WebApplication对象，用于构建一个MockMvc的实例。



## 5.1 常见注解

```properties
@RunWith(SpringJUnit4ClassRunner.class): 表示使用Spring Test组件进行单元测试

@WebAppConfiguration: 使用这个annotation会在跑单元测试的时候真实的启一个web服务，然后开始调用Controller的Rest API，待单元测试跑完之后再将web服务停掉

@ContextConfiguration: 指定Bean的配置文件信息，可以有多种方式，这个例子使用的是文件路径形式，如果有多个配置文件，可以将括号中的信息配置为一个字符串数组来表示
```



```java
@SpringJUnitWebConfig(locations = "test-servlet-context.xml")
class ExampleTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getAccount() throws Exception {
        this.mockMvc.perform(get("/accounts/1")
                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.name").value("Lee"));
    }
}
```

上述测试依赖于TestContext框架的WebApplicationContext支持，从位于与测试类相同的包中的XML配置文件加载Spring配置，但也支持基于Java和Groovy的配置。

MocMvc实例被用于运行一个`GET`请求，并且验证结果相应码为`200`，请求中的`ContentType`是`application/json`，他的返回相应也是`Json `格式的对象，`name`对象有个`Lee`属性，对于其中的`JsonPath`可以参考github上的资料，[JsonPath](https://github.com/json-path/JsonPath)



## 5.2 执行请求 perform()

您可以执行使用任何HTTP方法的请求，比如在url模版样式中指定参数：

```java
 MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/web/{id}/list",12)
                .param("thing", "somewhere")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
```

在上面的例子中，，为每个执行的请求设置contextPath和servletPath是很麻烦的。您可以在构建mocMVC时设置默认请求属性，比如这样：

```java
@Before
public void setUp(){
  mockMvc = MockMvcBuilders.webAppContextSetup(wac)
    .defaultRequest(MockMvcRequestBuilders.get("/"))
    .build();
}
```

## 5.3 定义期望 andExcept()

下面是在Springboot中Controller测试类

```java
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest
public class AuditorInfoWebControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Logger logger = LoggerFactory.getLogger(WebApplicationTests.class);

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getAuditorsListTest() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/web/auditor/list")
                .param("auditorType", "m2c")
                .param("userId", "1101")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(result.getResponse());
        logger.info("################## "+result.getResponse());
    }
}
```

## 5.4 测试API说明

Spring mvc测试框架提供了测试MVC需要的API，主要包括`Mock`、`MockMvcBuilder`、`MockMvc`、`RequestBuilder`、`ResultMatcher`、`ResultHandler`、`MvcResult`等。另外提供了几个静态工厂方法便于测试：`MockMvcBuilders`、`MockMvcRequestBuilders`、

`MockMvcResultMatchers`、`MockMvcResultHandlers`。

### 5.4.1 MockMvcBuilders

MockMvcBuilder是用来构造MockMvc的构造器。主要创建两种测试环境：独立测试和集成的web测试。

其中的`build()`方法，用于返回一个MockMVC对象。

```java
//build()方法返回一个MocMVC对象 
MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AllocConfigInfoController()).build();
```



### 5.4.2 MockMvc

使用之前的MockMvcBuilder.build()得到构建好的MockMvc；这个是mvc测试的核心API。

```java
MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))  
       .andExpect(MockMvcResultMatchers.view().name("user/view"))  
       .andExpect(MockMvcResultMatchers.model().attributeExists("user"))  
       .andDo(MockMvcResultHandlers.print())  
       .andReturn(); 
```

`perform()`：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；

`andExpect()`：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；

`andDo()`：添加ResultHandler结果处理器，比如调试时打印结果到控制台；

`andReturn()`：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；



另外还提供了以下API：

`setDefaultRequest()`：设置默认的RequestBuilder，用于在每次perform执行相应的RequestBuilder时自动把该默认的RequestBuilder合并到perform的RequestBuilder中；

`setGlobalResultMatchers()`：设置全局的预期结果验证规则，如我们通过MockMvc测试多个控制器时，假设它们都想验证某个规则时，就可以使用这个；

`setGlobalResultHandlers()`：设置全局的ResultHandler结果处理器；



### 5.4.3 MockMvcRequestBuilders

RequestBuilder是用来构建请求的。其提供了一个方法`buildRequest(ServletContext servletContext)`用于构建MockHttpServletRequest。

主要有两个子类`MockHttpServletRequestBuilder`和`MockMultipartHttpServletRequestBuilder`（如文件上传使用），即用来Mock客户端请求需要的所有数据。

### 5.4.4 ResultActions

调用`MockMvc.perform(RequestBuilder requestBuilder)`后将得到`ResultActions`，通过`ResultActions`完成如下三件事：

- `andExpect(ResultMatcher matcher)`：添加验证断言来判断执行请求后的结果是否是预期的；
- `andDo(ResultHandler handler)`：添加结果处理器，用于对验证成功后执行的动作，如输出下请求/结果信息用于调试
- `andReturn()`：返回验证成功后的MvcResult；用于自定义验证/下一步的异步处理

### 5.4.5 MockMvcResultMatchers

`ResultMatcher`用来匹配执行完请求后的结果验证，其就一个`match(MvcResult result)`断言方法，如果匹配失败将抛出相应的异常。

### 5.4.6 MockMvcResultHandlers

`ResultHandler`用于对处理的结果进行相应处理的，比如输出整个请求/响应等信息方便调试，Spring mvc测试框架提供了`MockMvcResultHandlers`静态工厂方法，该工厂提供了`ResultHandler print()`返回一个输出`MvcResult`详细信息到控制台的ResultHandler实现

### 5.4.7 MvcResult

即执行完控制器后得到的整个结果，并不仅仅是返回值，其包含了测试时需要的所有信息。

`MockHttpServletRequest getRequest()`：得到执行的请求；s

`MockHttpServletResponse getResponse()`：得到执行后的响应；



参考文献：

[Spring MVC框架详解](https://jinnianshilongnian.iteye.com/blog/2004660)

[Spring MVC Test Framework](https://docs.spring.io/spring/docs/5.2.0.BUILD-SNAPSHOT/spring-framework-reference/testing.html#spring-mvc-test-framework)

# 6. 日志

## 5.1、常用日志框架介绍

现在市面上有很多的日志框架，比如：JUL（java.util.logging），JCL（Apache Commons Logging），Log4j，Log4j2，Logback、SLF4j、jboss-logging....

- **log4j**：是Apache的一种基于Java日志工具（出生于1996年）

- **log4j2**：是log4j的升级版（出生于2012年）

- **JUL**： Java Util Logging，自Java1.4以来的官方日志实现。 （出生于2002年）

- **Logback**：是sl4j阵营的一套日志组件的实现。

- **Commons Logging**：同样也是Apache的一套基于java日志接口，之前叫Jakarta Commons Logging，后更名为Commons Logging 。

- **sl4J**：Simple Logging Facade for Java ，类似于Commons Logging，是一套简易Java**日志门面**，本身并无日志的实现 。

  

		现今，Java日志领域被划分为两大阵营：**Commons Logging**阵营和**Slf4j**阵营。 Commons Logging在Apache大树的笼罩下，有很大的用户基数 。但是sl4j目前发展的比较好。所以推荐使用sl4j。

		Commons Logging和Sl4j是门面接口，而Log4j和Logback是具体的实现。比较常见的组合是 logback + sl4j 和Commons Logging + log4j。Logback必须配合Slf4j使用。由于Logback和Slf4j是同一个作者，其兼容性不言而喻** 。

[日志框架的前世今生](https://www.cnblogs.com/chenhongliang/p/5312517.html)

## 5.2、springboot日志框架

Spring Boot在框架内容部使用**```Java Commons Loggin```**，**```spring-boot-starter-logging```**采用了**```slf4j+logback```**的形式，Spring Boot也能自动适配（jul、log4j2、logback） 并简化配置

| 日志门面      | 日志实现       |
| ------------- | -------------- |
| JCL           | Log4j JUL      |
| slf4j         | Logback log4j2 |
| jboss-logging |                |

| 日志系统 | 日志文件名                                                  |
| -------- | ----------------------------------------------------------- |
| Logback  | ```logback.xml```、```logback-spring.xml```、logback.groovy |
| Log4j    | ```log4j2.xml```、```log4j-spring.xml```/                   |
| JUL      | logging.properties                                          |

## 5.3、控制台输入日志

springboot默认选用的日志门面是```Commons Logging```，默认的日志级别是```INFO```。来看一下springboot默认的日志输出格式：

```java
2014-03-05 10:57:51.112 INFO 45469 --- [ main] org.apache.catalina.core.StandardEngine :
Starting Servlet Engine: Apache Tomcat/7.0.52
2014-03-05 10:57:51.253 INFO 45469 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/] :
Initializing Spring embedded WebApplicationContext
2014-03-05 10:57:51.253 INFO 45469 --- [ost-startStop-1] o.s.web.context.ContextLoader :
Root WebApplicationContext: initialization completed in 1358 ms
2014-03-05 10:57:51.698 INFO 45469 --- [ost-startStop-1] o.s.b.c.e.ServletRegistrationBean :
Mapping servlet: 'dispatcherServlet' to [/]
2014-03-05 10:57:51.702 INFO 45469 --- [ost-startStop-1] o.s.b.c.embedded.FilterRegistrationBean :
Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
```

默认的输出格式是由以下组成：

- 日期和时间
- 日志级别：error、warn、info、debug、trace
- 进程id
- 进程名称
- 日志名：通常是源类名
- 日志信息

**如何自定义自己的日志格式呢？**

这个需要配置字节的日志文件，设置自己想要的日志格式

？？？？

**日志颜色渲染？**

**26.2章节**，要使用```%clr```，

```java
%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){yellow}
```

| Level | Color |
| ----- | ----- |
| error | red   |
| warn  | yello |
| info  | green |
| debug | green |
| trace | green |
|       |       |

springboot默认使用logback，其默认使用base.xml配置文件

```xml
<included>
	<include resource="org/springframework/boot/logging/logback/defaults.xml" />
	<property name="LOG_FILE"        value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
	<include resource="org/springframework/boot/logging/logback/console-appender.xml" />
	<include resource="org/springframework/boot/logging/logback/file-appender.xml" />
	<root level="INFO">
		<appender-ref ref="CONSOLE" />
		<appender-ref ref="FILE" />
	</root>
</included>
```

## 5.3、日志级别控制

Spring Boot使用**Commons Logging**进行所有内部日志记录 ，默认情况下，如果您使用“Starters”，则使用Logback进行日志记录 。

```xml
<!--
	springboot-dependencies中定义了两个starters,默认底层支持两种日志实现，一种是logback，另一种是		log2j2。
默认使用sl4j日志框架
-->
 <slf4j.version>1.7.25</slf4j.version>

  <log4j2.version>2.11.1</log4j2.version>
  <logback.version>1.2.3</logback.version>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
    <version>2.1.1.RELEASE</version>
<dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-logging</artifactId>
    <version>2.1.1.RELEASE</version>
</dependency>
```



所有的日志系统都可以设置包结构的日志级别，比如在springboot应用中可以通过```logging.level.<logger-name>=<level>```，就如下面这样：

```properties
# springboot的根日志级别
logging.level.root=WARN
# 设置org.springframework.web包下的所有文件的日志级别为debug
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate=ERROR
```

## 5.4、sl4j占位符的使用

使用`{}`来进行占位操作。具体代码如下：

```java
log.info("获取审核人员信息的入参businessCode=[{}], auditStep=[{}]", businessCode, auditStep);

[]在这里有什么作用？测试一下？

```



## 5.4、自定义日志配置文件

一个日志系统可以被激活通过**相关的```lib```**和一个classpath下的配置文件或者指定路径下的**配置文件**，通过```logging.config```属性，例如这样：

```properties
logging.config="the location of the logging configuration"
```

根据不同的日志系统，会加载以下相应的配置文件：

| 日志系统 | 日志文件名                                      |
| -------- | ----------------------------------------------- |
| Logback  | logback.xml、logback-spring.xml、logback.groovy |
| Log4j    | log4j2.xml、log4j-spring.xml                    |
| JUL      | logging.properties                              |

**springboot官方推荐使用```-spring```的配置文件名，例如：```logback-spring.xml```。**

除了这些，还需要介绍一下spring环境下有关log的其他属性，如下面的表格所示：

| 日志属性                              | 属性说明                 |
| ------------------------------------- | ------------------------ |
| **logging.exception-conversion-word** |                          |
| **logging.file**                      |                          |
| **logging.file.max-size**             | 日志输出文件大小的最大值 |
| **logging.file.max-history**          | 日志文件保留的最大时间   |
| **logging.path**                      |                          |
| **logging.pattern.console**           | 控制台的日志格式         |
| **logging.pattern.dateformat**        |                          |
| **logging.pattern.file**              |                          |
| **loggin.pattern.level**              |                          |

举例说明：logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!--定义日志文件的存储地址 勿在 LogBack 的配置中使用相对路径-->
    <property name="LOG_PATH" value="/med/log/apps/"/>
    <property name="PROJECT_GROUP" value="finance"/>
    <property name="PROJECT_NAME" value="shield"/>

    <!-- 彩色日志 -->
    <!-- 彩色日志依赖的渲染类 -->
    <conversionRule conversionWord="clr" 		    	        	     converterClass="org.springframework.boot.logging.logback.ColorConverter"/>           
    <conversionRule conversionWord="wex"
                    converterClass="org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter"/>
    <conversionRule conversionWord="wEx"
                    converterClass="org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter"/>
    <!-- 彩色日志格式 -->
    <property name="LOG_PATTERN"
              value="%d{yyyy-MM-dd'T'HH:mm:ss.SSSZ} [%level] ${PROJECT_GROUP}/${PROJECT_NAME} %logger{36} %X{filter:--} %X{request_id:--} %msg%n}"/>
    <!-- Console 输出设置 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
            <charset>utf8</charset>
        </encoder>
    </appender>

    <!-- 按照每天生成日志文件 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>${LOG_PATH}/${PROJECT_GROUP}-${PROJECT_NAME}.%d.log</FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>7</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>${LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!-- sentry的拦截级别配置 -->
    <appender name="SENTRY" class="io.sentry.logback.SentryAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
    </appender>

    <!--mybatis log configure 特殊处理-->
    <logger name="com.apache.ibatis" level="DEBUG"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>

    <root level="debug">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="SENTRY"/>
    </root>

</configuration>
```



springboot默认的配置文件：

[Logback](https://github.com/spring-projects/spring-boot/tree/master/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/logback)

[log4j2](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/log4j2/log4j2.xml)

[Java Util Logging](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-project/spring-boot/src/main/resources/org/springframework/boot/logging/java/logging-file.properties)



## 5.4、日志输入文件

默认情况下，springboot只是将日志打印到控制台，并不输出到文件。如果你想将日志输入到文件中保存，需要在配置文件中设置```logging.file```和```logging.path```。

```properties

```

 日志文件每10M将会循环记录一次，默认会记录```error```，```warn```，```info```级别的日志。如果想要**改变日志文件的大小限制**可以使用```logging.file.max-size```属性。默认情况下，**日志记录操作将会无限期的进行下去，除非**设置了```logging.file.max-history```属性（默认值为0）。



**注意：日志系统初始化操作早与应用的生命周期**，所以可以通过```@PropertySource```注解来获取属性文件没有加载进来的日志属性。

# 6、MVC

springboot可以通过内嵌的tomcat、jetty、Netty来创建一个http服务，大多数的web应用都可以使用```spring-boot-starter-web```模块建立起来。当然你也选择建立相关的应用通过使用```spring-boot-starter-webflux```模块**（这是spring5.x的新特性）**。

## springMVC 自动配置

spring MVC自动配置添加了很多的特性，下面列举几个：

- 包含了```ContentNegotiatingViewResolver```和```BeanNameViewResolver```beans。
- 支持静态资源，也支持```webjar```
- 自动注册了```Converter```，```GenericConverter```和```Formatter```这些bean
- 支持```HttpMessageConverters```
- 自动注册了```MessageCodesResolver```

**如果你想保留spring MVC的这些默认的特性，而且还想添加自己MVC配置**（比如拦截器、格式转换器、视图映射器等），**可以自己添加配置类使用```@Configuration```注解，并实现````WebMvcConfigurer`  ```接口**。

**如果你想完全掌控MVC，你可以添加```@Configuration```和```@EnableWebMvc```注解**，但是不建议这样做。因为你可能没有人家springboot做的那么好。



## HttpMessageConverters

spring MVC 使用```HttpMessageConverter```接口去转换http的请求和响应。其中的默认值是开箱就可以用的，比如对象Objects可以自动转换成Json或者xml的格式，默认使用```UTF-8```的编码格式。



## 序列换和反序列化

springboot提供了```@JsonComponent```注解，

```java

```

## 加载静态资源

springboot的```ResourceProperties```中定义了**静态资源的映射规则**。下面是默认的加载静态的Location

```properties
classpath:/META-INF/resources/
classpath:/resources/
classpath:/static/
classpath:/public/

"/"：当前项目的根路径
```

当然这个类中也定义一些静态资源先关的参数，比如缓存时间等，可以通过配置类来设置这些值:

```java
/*
	可以看到配置文件的前缀是spring.resources	
*/
@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
public class ResourceProperties {
    ......
}
```

默认资源被映射到```/**```，当然可以通过```spring.mvc.static-path-pattern```来进行设置，springboot会跟根据这种默认来寻找资源，比如这样：

```properties
spring.mvc.static-path-pattern=/resources/**
```

当然也可以指定静态的location，通过```spring.resources.static-locations```注解，默认值为上面所提到的location值。 

## **自定义图标？**

springboot使用```favicon.ico```来作为网站logo，在开发公司的项目的时候,需要换成公司的logo





## 错误处理



## 内嵌的servlet容器



# 7、springboot开发者工具

Spring Boot包含了一些额外的工具集，用于提升Spring Boot应用的开发体验 。```spring-boot-devtools```模块可以被包含到任何的项目中，来提供一些节约开发时间的特性。只需将该模块的依赖添加进项目中即可。

```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
        <!-- 表示不依赖传递 -->
		<optional>true</optional>
	</dependency>
</dependencies>
```

```xml
备注：<optional>true</optional>标签的意思是，A依赖B，B依赖C。
为true意思是A如果没有显示的引入C，则A就不会依赖C。
默认为false，即子类必须依赖父类。
```

​	在运行一个完整的打包过的应用时，开发者工具（devtools）会被自动禁用。如果应以`java -jar`或特殊的类加载器启动，都会被认为是一个产品级的应用（production application），从而禁用开发者工具。为了防止devtools传递到项目中的其他模块，设置该依赖级别为```optional```是个不错的实践。如果想确保devtools绝对不会包含在一个产品级构建中，你可以使用`excludeDevtools`构建属性彻底移除该JAR，Maven和Gradle都支持该属性 。

# springboot热部署

**热部署的优点：**

- 线上线下都可以使用
- 无需重启服务器

**热部署和热加载的区别和联系？**

- 热部署和热加载都不需要重启服务器可是实现编辑/部署项目
- 都是基于Java的类加载器实现的
- 热部署侧重的是**重新的部署项目**，在**服务器运行时**都可以实现部署工作
- 热加载侧重于**运行时重新加载class**，将修改并编译后的字节码文件提交到服务器，程序自动加载修改后的字节码文件（**侧重于加载重新修改后的类或者class文件**）。 

**热部署和热加载在实现原理的区别？**

​	热部署**直接加载整个应用**，会释放内存，比热加载更加彻底，当同时也消耗时间。

​	热加载**直接加载改变的class文件**，主要依赖Java的类加载器。在后台启动一个后台线程，主要**检测类文件的时间戳变化**，如果时间戳发生变化， 就将类重新加载。



# # 7、错误处理机制

## SpringBoot默认的错误处理机制

-  浏览器，默认返回一个错误页面

![](images/搜狗截图20180226173408.png)

-  如果是其他客户端，默认响应一个json数据



![](images/搜狗截图20180226173527.png)

****

**原理：可以参照ErrorMvcAutoConfiguration；错误处理的自动配置；**

步骤：

​	》一但系统出现4xx或者5xx之类的错误；

​	》ErrorPageCustomizer就会生效（定制错误的响应规则），并触发/error 请求

​	》**BasicErrorController**来处理/error请求；



在ErrorMvcAutoConfiguration中给容器添加了这几个组件：

**1、DefaultErrorAttributes**：帮我们在页面共享信息

**2、BasicErrorController**：处理默认/error请求

```java
//默认处理/error请求，否则处理server.error.path中定义的请求
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {
  	//产生HTML错误数据（浏览器发送的请求，通过请求头识别是否是浏览器请求）
    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request,
			HttpServletResponse response) {
        //获取状态信息
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
				request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
        //去哪个页面作为错误页面
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
	}
   
    //产生json数据（其他客户端发送的请求）
    @RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		HttpStatus status = getStatus(request);
		return new ResponseEntity<>(body, status);
	}
}
```



**3、ErrorPageCustomizer**：

系统出现错误以后定制error的响应规则，哪些错误跳转到哪些页面之类的

```java
private static class ErrorPageCustomizer implements ErrorPageRegistrar, Ordered {
		private final ServerProperties properties;
		private final DispatcherServletPath dispatcherServletPath;
       	//构造函数
		protected ErrorPageCustomizer(ServerProperties properties,
				DispatcherServletPath dispatcherServletPath) {
			this.properties = properties;
			this.dispatcherServletPath = dispatcherServletPath;
		}
		//注册错误页面
		@Override
		public void registerErrorPages(ErrorPageRegistry errorPageRegistry) {
			//从ErrorProperties中获取error路径
            ErrorPage errorPage = new ErrorPage(this.dispatcherServletPath
					.getRelativePath(this.properties.getError().getPath()));
			errorPageRegistry.addErrorPages(errorPage);
		}
}

/**
 * Path of the error controller.
 */
@Value("${error.path:/error}")
private String path = "/error";

```

**4、DefaultErrorViewResolver**：

错误后跳转到哪个页面是由他来决定的。

```java
@Override
public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status,
                                     Map<String, Object> model) {
    //根据status决定返回哪个错误页面
    ModelAndView modelAndView = resolve(String.valueOf(status.value()), model);
    if (modelAndView == null && SERIES_VIEWS.containsKey(status.series())) {
        modelAndView = resolve(SERIES_VIEWS.get(status.series()), model);
    }
    return modelAndView;
}

private ModelAndView resolve(String viewName, Map<String, Object> model) {
   //获取/error路径下响应错误码的页面，比如/error/404
    String errorViewName = "error/" + viewName;
    TemplateAvailabilityProvider provider = this.templateAvailabilityProviders
        .getProvider(errorViewName, this.applicationContext);
   //如果有可用的模板引擎， 返回模板引擎下的视图
    if (provider != null) {
        return new ModelAndView(errorViewName, model);
    }
    //否则返回静态路径下的视图，/META-INF/resources/,/resources/, /static/, /public/
    return resolveResource(errorViewName, model);
}
```

## 如何定制错误响应

### 如何定制错误的页面

**》如果有模板引擎的情况下**，直接创建**/error/状态码**，如/error/404

我们可以使用**4xx**和**5xx**作为错误页面的文件名来匹配这种类型的所有错误，**优先寻找精确的状态码.html**；		



页面能获取的信息；

​	timestamp：时间戳·	

​	status：状态码

​	error：错误提示

​	exception：异常对象

​	message：异常消息

​	errors：JSR303数据校验的错误都在这里

**》没有模板引擎**（模板引擎找不到这个错误页面），静态资源文件夹下找；

**》以上都没有错误页面**，就是默认来到SpringBoot默认的错误提示页面；

 ```html
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 pt-3 px-4">
    <h1>500</h1>
    <h2>status：[[${status}]]</h2><br/>
    <h2>timestamp：[[${timestamp}]]</h2><br/>
    <h2>error：[[${error}]]</h2><br/>
    <h2>exception：[[${exception}]]</h2><br/>
    <h2>message：[[${message}]]</h2><br/>
 </main>
 ```



### 如何定制错误的json数据

**1)可以定制自己的异常处理器。**

 ```java
/**
 * ControllerAdvice：控制器增强器
   但是这种并不是自适应的，浏览器和客户端返回的都是json数据
 */
@ControllerAdvice
public class MyExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    /**
     * UserException：抛出来的异常会被此拦截器拦截
     * @return
     */
    @ResponseBody
    @ExceptionHandler(UserException.class)
    public Map<String,Object> handlerException(UserException e){
        HashMap<String, Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        map.put("code","500");
        logger.info(map.toString());
        return map;
    }
}

备注：ControllerAdvicez注解
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ControllerAdvice {......}
 ```

可以通过**```@ExceptionHandler```**来拦截自定义的异常**UserException**，然后通过@ResponseBody将异常数据返回去。

缺点：

​	这种方式浏览器请求和客户端请求返回都是json数据，并没有做到自适应的效果。



**2)转发到/error进行错误处理【推荐】**

```java
@ResponseBody
@ExceptionHandler(UserException.class)
public String  handlerException(UserException e, HttpServletRequest request){
    HashMap<String, Object> map = new HashMap<>();
    map.put("message",e.getMessage());
    map.put("code","500");
    //需要设置错误status，这样Handler就会跳转到响应的错误页面
    request.setAttribute("javax.servlet.error.status_code",400);
    //转发到/error请求，BasicErrorController就会处理错误请求
    return "redirect:/error";
 }
```

可以参考BasicErrorController中的这段代码：

```java
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request,
                              HttpServletResponse response) {
    //获取错误状态码，决定将要跳转到那个错误页面
    HttpStatus status = getStatus(request);
    Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(
        request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
    response.setStatus(status.value());
    ModelAndView modelAndView = resolveErrorView(request, response, status, model);
    return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
}

protected HttpStatus getStatus(HttpServletRequest request) {
    //从这个属性中获取错误代码
    Integer statusCode = (Integer) request
        .getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    try {
        return HttpStatus.valueOf(statusCode);
    }
    catch (Exception ex) {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
```

上面这用方式返回的数据，并没有携带我们自己定义的错误属性数据，如果想要携带自己定义的错误属性，需要借助于**```DefaultErrorAttributes```**，分析一下这个过程：

》错误拦截器Handler拦截错误后，携带上错误状态码属性```javax.servlet.error.status_code```，将请求转发```/error```

》BasicErrorController来处理```/error```请求，其中的```errorHtml()```和```error()```分别处理浏览器请求和客户端请求，一个返回到错误页面，一个返回json数据。

》这两个方法中的错误属性都是通过```ErrorAttribute```接口中的```getErrorAttributes```获取的，所以可自定义ErrorAttribute组件来自定义自己的错误属性

```java
//自定义错误拦截器
@ControllerAdvice
public class MyExceptionHandler {
  
    @ResponseBody
    @ExceptionHandler(UserException.class)
    public String  handlerException(UserException e, HttpServletRequest request){
        HashMap<String, Object> map = new HashMap<>();
        map.put("message",e.getMessage());
        map.put("code","500");

        request.setAttribute("error",map);
        //需要设置错误status，这样Handler就会跳转到响应的错误页面
        request.setAttribute("javax.servlet.error.status_code",400);

        //转发到/error请求，BasicErrorController就会处理错误请求
        return "redirect:/error";
    }

}

//自定义ErrorAttributes组件
@Component
public class MyErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> map = super.getErrorAttributes(webRequest, includeStackTrace);
        map.put("company","guazi");
        //获取异常处理器携带的数据,0代表的是request作用域
        webRequest.getAttribute("error",0);
        return map;
    }
}
```







# 8、配置嵌入式的Servlet容器

SpringBoot默认使用Tomcat作为嵌入式的Servlet容器；

![](D:/%E5%AD%A6%E4%B9%A0%E8%A7%86%E9%A2%91/%E5%B0%9A%E7%A1%85%E8%B0%B7Spring%20Boot/%E6%BA%90%E7%A0%81%E3%80%81%E8%B5%84%E6%96%99%E3%80%81%E8%AF%BE%E4%BB%B6/%E6%BA%90%E7%A0%81%E3%80%81%E8%B5%84%E6%96%99%E3%80%81%E8%AF%BE%E4%BB%B6/%E6%96%87%E6%A1%A3/Spring%20Boot%20%E7%AC%94%E8%AE%B0/images/%E6%90%9C%E7%8B%97%E6%88%AA%E5%9B%BE20180301142915.png)

### 8.1、如何定制和修改Servlet容器的相关配置；

1、修改和server有关的配置（可以参考【**ServerProperties**】类）；

```properties
server.port=8081
server.context-path=/crud

server.tomcat.uri-encoding=UTF-8

//通用的Servlet容器设置
server.xxx
//Tomcat的设置
server.tomcat.xxx
```





### 8.2、注册Servlet三大组件【Servlet、Filter、Listener】

以前可以开webapp下的web.xml进行注册，springboot为了满足这个要求提供了三大组件：

 - **ServletRegistrationBean**

```java
//自定义Servlet
public class MyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("hello Servlet");
    }
}

//注册三大组件
@Bean
public ServletRegistrationBean myServlet(){
    ServletRegistrationBean registrationBean = new ServletRegistrationBean(new MyServlet(),"/myServlet");
    return registrationBean;
}
```

 - **FilterRegistrationBean**

```java
//自定义filter
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("MyFilter process...");
        chain.doFilter(request,response);
    }
    @Override
    public void destroy() {}
}
//filter注册组件
@Bean
public FilterRegistrationBean myFilter(){
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilters(new MyFilter());
    registrationBean.setUrlPatterns(Arrays.asList("/hello","/myServlet"));
    return registrationBean;
}
```



 - **ServletListenerRegistrationBean**

```java
//自定义监听器
public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("contextInitialized...web应用启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("contextDestroyed...当前web项目销毁");
    }
}
//监听器注册组件
@Bean
public ServletListenerRegistrationBean myListener(){
    ServletListenerRegistrationBean<MyListener> registrationBean = new ServletListenerRegistrationBean<>(new MyListener());
    return registrationBean;
}
```

springboot 在启动的时候会自动注册DispatcherServlet，也是同样的道理，同样使用了监听器。可以在**```DispatcherServletAutoConfiguration```**得到参考。

```java
@Bean(name = DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
@ConditionalOnBean(value = DispatcherServlet.class, name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
public ServletRegistrationBean dispatcherServletRegistration(
      DispatcherServlet dispatcherServlet) {
   
    //默认拦截： /  所有请求；包静态资源，但是不拦截jsp请求；   /*会拦截jsp
    //可以通过server.servletPath来修改SpringMVC前端控制器默认拦截的请求路径
    ServletRegistrationBean registration = new ServletRegistrationBean(
         dispatcherServlet, this.serverProperties.getServletMapping());
    
   registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
   registration.setLoadOnStartup(
         this.webMvcProperties.getServlet().getLoadOnStartup());
   if (this.multipartConfig != null) {
      registration.setMultipartConfig(this.multipartConfig);
   }
   return registration;
}
```

## 8.3、替换嵌入式Servlet容器

![](images/搜狗截图20180302114401.png)

从嵌入式容器中可以看到springboot支持三种Servlet容器：

- **Tomcat(默认)**

```xml
<dependency>
   <!-- 引入web模块默认就是使用嵌入式的Tomcat作为Servlet容器； -->
    <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

- **Jetty**

```xml
<!--首先需要排除掉Tomcat容器-->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <exclusions>
      <exclusion>
         <artifactId>spring-boot-starter-tomcat</artifactId>
         <groupId>org.springframework.boot</groupId>
      </exclusion>
   </exclusions>
</dependency>

<!--然后在引入其他Servlet容器-->
<dependency>
   <artifactId>spring-boot-starter-jetty</artifactId>
   <groupId>org.springframework.boot</groupId>
</dependency>
```



- **Undertow**

```xml
<!-- 引入web模块 -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <exclusions>
      <exclusion>
         <artifactId>spring-boot-starter-tomcat</artifactId>
         <groupId>org.springframework.boot</groupId>
      </exclusion>
   </exclusions>
</dependency>

<!--引入其他的Servlet容器-->
<dependency>
   <artifactId>spring-boot-starter-undertow</artifactId>
   <groupId>org.springframework.boot</groupId>
</dependency>
```

## 8.4 、嵌入式Servlet容器自动配置原理

可以参考```EmbeddedServletContainerAutoConfiguration```，但是自从Springboot2.0开始这个类已经被**```ServletWebServerFactoryAutoConfiguration```** 代替了。

```java
/*
	@link EnableAutoConfiguration Auto-configuration} for servlet web servers.
*/
@Configuration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = Type.SERVLET)
//ServerPropoerties中可以获取关于Server的一些配置，其中也包括tomcat,Jetty,netty等属性
@EnableConfigurationProperties(ServerProperties.class) 
//导入
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,
         //EmbeddedTomcat等都是ServletWebServerFactoryConfiguration的静态类
		ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,
		ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
		ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class ServletWebServerFactoryAutoConfiguration {

	@Bean
	public ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer(
			ServerProperties serverProperties) {
		return new ServletWebServerFactoryCustomizer(serverProperties);
	}
	
    //ConditionalOnClass:如果类路径中有tomcat,将会通过TomcatServlet工厂创建Tomcat容器
	@Bean
	@ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
	public TomcatServletWebServerFactoryCustomizer tomcatServletWebServerFactoryCustomizer(
			ServerProperties serverProperties) {
		return new TomcatServletWebServerFactoryCustomizer(serverProperties);
	}

	public static class BeanPostProcessorsRegistrar
			implements ImportBeanDefinitionRegistrar, BeanFactoryAware {
		private ConfigurableListableBeanFactory beanFactory;

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			if (beanFactory instanceof ConfigurableListableBeanFactory) {
				this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
			}
		}
		。。。。。
	}
}
```

**```ServletWebServerFactoryConfiguration```**中定义了很多静态类，都是tomcat、Jett、Undertow有关的静态内部类。大体的思路都是相同的：**判断类路径中有没有tomcat class文件或者Jetty的Class文件，如果有，创建相应的web容器工厂，在由工厂创建出具体的web容器**。其中就 拿tomcat为例：

```java
@Configuration
class ServletWebServerFactoryConfiguration {

	@Configuration
	@ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
	@ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    //内嵌的Tomcat，如果类路径中有Tomcat等，但是没有用户自定义的ServletWebServerFactory就会创建Tomcat容器工厂，由工厂在创建Tomcat web容器
	public static class EmbeddedTomcat {
		@Bean
		public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
			return new TomcatServletWebServerFactory();
		}
	。。。。。。

}
    
 /*
 下面是TomcatServletWebServerFactory中的部分代码片段
 */
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory
    implements ConfigurableTomcatWebServerFactory, ResourceLoaderAware {
    。。。。。
   
    @Override
	public WebServer getWebServer(ServletContextInitializer... initializers) {
		//创建Tomcat
        Tomcat tomcat = new Tomcat();
        //获取Tomcat的一些属性
		File baseDir = (this.baseDirectory != null) ? this.baseDirectory
				: createTempDir("tomcat");
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		Connector connector = new Connector(this.protocol);
		tomcat.getService().addConnector(connector);
		customizeConnector(connector);
		tomcat.setConnector(connector);
		tomcat.getHost().setAutoDeploy(false);
		configureEngine(tomcat.getEngine());
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		prepareContext(tomcat.getHost(), initializers);
          //将配置好的Tomcat传入进去，返回一个TomcatWebServer；并且启动Tomcat服务器
		return getTomcatWebServer(tomcat);
	}
    。。。。。
}  
```

**AbstractServletWebServerFactory**：web容器抽象工厂【工厂模式】

![](/images/2019-01-19_221434.png)



**我们对嵌入式容器的配置修改是怎么生效？**

ServletWebServerFactoryAutoConfiguration的中的静态类**BeanPostProcessorsRegistrar**，工厂创建后，会执行后处理功能，其主要功能是给容器导入了BeanPostProcessorsRegistrar。

```java
//导入BeanPostProcessorsRegistrar：bean后处理，给容器导入了一些组件
public static class BeanPostProcessorsRegistrar
			implements ImportBeanDefinitionRegistrar, BeanFactoryAware {

		private ConfigurableListableBeanFactory beanFactory;
		。。。。。。
            
         //主体给容器导入了WebServerFactoryCustomizerBeanPostProcessor组件
		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
				BeanDefinitionRegistry registry) {
			if (this.beanFactory == null) {
				return;
			}
            //WebServerFactoryCustomizerBeanPostProcessor:
			registerSyntheticBeanIfMissing(registry,
					"webServerFactoryCustomizerBeanPostProcessor",
					BeanPostProcessorsRegistrar.class);
			registerSyntheticBeanIfMissing(registry,
					"errorPageRegistrarBeanPostProcessor",
					ErrorPageRegistrarBeanPostProcessor.class);
		}
		。。。。。

	}
```

**BeanPostProcessorsRegistrar它主要是干什么？**

将其主要代码粘贴在下面，

```java
@Override
public Object postProcessBeforeInitialization(Object bean, String beanName)
    throws BeansException {
    //如果当前初始化的是一个WebServerFactory类型的组件
    if (bean instanceof WebServerFactory) {
        postProcessBeforeInitialization((WebServerFactory) bean);
    }
    return bean;
}

private void postProcessBeforeInitialization(WebServerFactory webServerFactory) {
    LambdaSafe
        .callbacks(WebServerFactoryCustomizer.class, getCustomizers(),
                   webServerFactory)
        .withLogger(WebServerFactoryCustomizerBeanPostProcessor.class)
        .invoke((customizer) -> customizer.customize(webServerFactory));
}

```

# 9. Web项目开发

## 9.1  RestFul风格

1）Restful - CRUD：CRUD满足Rest风格；

URI：  /资源名称/资源标识       HTTP请求方式区分对资源CRUD操作

|      | 普通CRUD（uri来区分操作） | RestfulCRUD       |
| ---- | ------------------------- | ----------------- |
| 查询 | getEmp                    | emp---GET         |
| 添加 | addEmp?xxx                | emp---POST        |
| 修改 | updateEmp?id=xxx&xxx=xx   | emp/{id}---PUT    |
| 删除 | deleteEmp?id=1            | emp/{id}---DELETE |

2）实验的请求架构;

| 实验功能                             | 请求URI | 请求方式 |
| ------------------------------------ | ------- | -------- |
| 查询所有员工                         | emps    | GET      |
| 查询某个员工(来到修改页面)           | emp/1   | GET      |
| 来到添加页面                         | emp     | GET      |
| 添加员工                             | emp     | POST     |
| 来到修改页面（查出员工进行信息回显） | emp/1   | GET      |
| 修改员工                             | emp     | PUT      |
| 删除员工                             | emp/1   | DELETE   |

**思考： 为什么开发中要使用restful风格呢？？？？？？？？？？？？？**

## 9.2  Swagger自动生成文档

swagger 用于自动生成html文档的，在前后端分离开发中，有利于将自己开发的Rest服务界面化的提供给前端工程师。下面介绍如何使用

首先，需要引入swagger相关的依赖，需要两个依赖(swagger2和swagger-ui)。其中后者是提供可视化的页面交互的

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>

```

​	然后在**SpringBoot的启动类上添加`@EnableSwagger2`注解** ，这样swagger就和我们的项目绑定了。系统启动之后，访问`localhost:8080/swagger-ui.html`网站，上面显示了系统中所有的`controller`

​	swagger-ui会自动生成一个无依赖的html，但是默认的html可能字段等显示的不太友好，swagger提供了丰富的注解来扩充说明我们的API，这里介绍常见的注解。

```properties
@Api: 作用在类上，说明该类的作用。
@ApiOperation: 作用在方法上，添加方法说明。
@ApiParam: 作用方法，进行方法参数说明
	name: 参数名
	value: 参数说明
	required: 参数是否必须

@ApiModel: 作用一个Model类（一般方法的请求参数和返回值封装在一个Model对象中）
@ApiModelProperty: 描述一个model的属性
   value: 字段说明 
   name:  重写属性名字 
   dataType: 重写属性类型 
   required: 是否必填 
   example: 举例说明 
   hidden: 隐藏 
   
@ApiImplicitParams: 作用在方法上，用于方法参数说明 
@ApiImplicitPara: 用来注解来给方法入参增加说明。

@ApiResponses: 用于表示一组响应
@ApiResponse: 用在@ApiResponses中，一般用于表达一个错误的响应信息
     code: 数字，例如400
     message: 信息，例如"请求参数没填好"
     response: 抛出异常的类   

```

下面给个例子具体说明一下：

```java
/**
* Model对象实体
*/
@ApiModel(value="user对象",description="用户对象user")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
  
     @ApiModelProperty(value="用户名",name="username",example="xingguo")
     private String username;
     @ApiModelProperty(value="状态",name="state",required=true)
      private Integer state;
      private String password;
      private String nickName;
      private Integer isDeleted;

      @ApiModelProperty(value="id数组",hidden=true)
      private String[] ids;
      private List<String> idList;
     //省略get/set
}


/**
* Controller对象
*/
@RestController
@RequestMapping("/say")
@Api(value = "SayController|一个用来测试swagger注解的控制器")
public class SayController {
    
  @ApiOperation(value="获取用户信息",tags={"获取用户信息copy"},notes="注意问题点")
  @GetMapping("/getUserInfo")
  public User getUserInfo(@ApiParam(name="id",value="用户id",required=true)Long id,
                             @ApiParam(name="username",value="用户名") String username) {
       
      User user = userService.getUserInfo();
      return user;
  }
	
  @ApiOperation("更改用户信息")
  @PostMapping("/updateUserInfo")
  public int updateUserInfo(@RequestBody 
    									@ApiParam(name="用户对象",value="传入json格式",required=true) User user){

     int num = userService.updateUserInfo(user);
     return num;
  }

    
    @PostMapping("/updatePassword")
    @ApiOperation(value="修改用户密码", notes="根据用户id修改密码")
    @ApiImplicitParams({
        @ApiImplicitParam(paramType="query", name = "userId", value = "用户ID", 
                          required = true, dataType = "Integer"),
        @ApiImplicitParam(paramType="query", name = "password", value = "旧密码",
                          required = true, dataType = "String"),
        @ApiImplicitParam(paramType="query", name = "newPassword", value = "新密码", 
                          required = true, dataType = "String")
    })
    public String updatePassword(@RequestParam(value="userId") Integer userId, 
                                 @RequestParam(value="password") String password,
            										@RequestParam(value="newPassword") String newPassword){
      if(userId <= 0 || userId > 2){
          return "未知的用户";
      }
      if(StringUtils.isEmpty(password) || StringUtils.isEmpty(newPassword)){
          return "密码不能为空";
      }
      if(password.equals(newPassword)){
          return "新旧密码不能相同";
      }
      return "密码修改成功!";
    }
}

```

注意：

​	如果方法的输入参数封装在对象中，在Model对象的具体字段使用`@ApiModelParam`注解

​	如果方法输入参数是基本类型，使用`@ApiParam`注解



## 9.3  WireMock 伪造Restful服务  

WireMock可以快速的伪造RestFuel服务。



## 9.4 Mockito测试





# 





# CRUD 后台管理系统

## 1、引入sl4j+logback日志系统



## 2、登录功能如何防止表单重复提交

页面表单以post的方式将请求提交到后台，后台对传递的参数进行校验，进行相应的处理。如果刷新登录页面，会造成表**单重复提交**，所以为了防止表单重复提交，可以登录成功后直接**重定向**到主页中。

```java
/*@PostMapping: 说明这是一个post请求*/
@PostMapping("/user/login")
public String login(@RequestParam("username") String username,
                    @RequestParam("password") String password,
                    HttpSession session,
                    Map<String,Object> map){
    if(!StringUtils.isEmpty(username) && "123".equals(password)){
        session.setAttribute("userId",username);
        //登录成功,防止表单重复提交，可以重定向到主页
        return "redirect:/main.html";
    }else{
        //登录失败
        map.put("msg","用户名或密码错误！");
        return "login";
    }
 }
```

为了防止一些非法的请求，需要过滤掉一些url，所以需要定制拦截器，拦截处理请求。

```java
public class LoginHandlerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(LoginHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String userId = (String)request.getSession().getAttribute("userId");
        if(!StringUtils.isEmpty(userId)){
            //已登录，放行
           return true;
        }else{
            //未登录，返回登录页面
            request.setAttribute("msg","没有权限请先登录！");
            request.getRequestDispatcher("/index.html").forward(request,response);
            logger.info("请求url："+request.getRequestURI()+" ["+request.getAttribute("msg")+"]");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
```



定义了拦截操作，还需要将拦截器注册到我们容器中：

```java
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {
    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有请求，需要过滤掉访问登录页面的请求
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/","/index.html","/user/login");
    }

}
```



## 3、国际化处理





## 





## thymeleaf公共页面公共元素抽取

```html
1、抽取公共片段
<div th:fragment="copy">
&copy; 2011 The Good Thymes Virtual Grocery
</div>

2、引入公共片段
<div th:insert="~{footer :: copy}"></div>
~{templatename::selector}：模板名::选择器
~{templatename::fragmentname}:模板名::片段名
```

## 员工添加功能

日期的格式化

springboot默认的日期格式为```dd/MM/yyyy```，具体的可以参考 ```WebMvcAutoConfiguration```

```java
@Bean
@Override
public FormattingConversionService mvcConversionService() {
    WebConversionService conversionService = new WebConversionService(
        this.mvcProperties.getDateFormat()); //从配置文件或获取日期格式否则使用默认值
    addFormatters(conversionService);
    return conversionService;
}
```

当然我们也可以自定义日志的格式，这需要修改配置文件的属性



## 7. 封装统一的返回结果



# 三、云开发技术

## 1. Docker

### 1.1 简介

**Docker**是一个开源的**应用容器引擎**；是一个轻量级容器技术；Docker支持将软件编译成一个镜像；然后在镜像中各种软件做好配置，将镜像发布出去，其他使用者可以直接使用这个镜像；运行中的这个镜像称为容器，容器启动是非常快速的。

Docker要求Centos的内核版本高于3.10

查看Centos的内核版本：

```properties
uname -r
# 如果内核版本过低，可以使用生升级
yum update 

# 安装docker
yum install docker
# 启动docker
systemctl start docker
```



### 1.2 Docker 核心概念 

**docker主机(Host)**：安装了Docker程序的机器（Docker直接安装在操作系统之上）；

**docker客户端(Client)**：连接docker主机进行操作；

**docker仓库(Registry)**：用来保存各种打包好的软件镜像；

**docker镜像(Images)**：软件打包好的镜像；放在docker仓库中；

**docker容器(Container)**：镜像启动后的实例称为一个容器；容器是独立运行的一个或一组应用

![](images/搜狗截图20180303165113.png)

**使用Docker的步骤**：

1）安装Docker

2）去Docker仓库寻找相应软件的镜像

3）使用Docker运行这个镜像，这个镜像就会生成一个Docker容器；

4）对容器的启动停止就是对软件的启动停止；







## 2. ElasticSearch



# 四、开源组件包

## 1. Apache Commons 

### 1.1 Commons-lang

```java
<!-- apache-->
<dependency>
	<groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.8.1</version>
</dependency>
  
```

**输出任意长度的字符串**：

```java
/**
* <p>Creates a random string whose length is the number of characters specified.</p>
*   @param count  the length of random string to create
*   @return the random string
*/
String filename= RandomStringUtils.randomAlphanumeric(10);
 System.out.println(filename);
```

https://www.jianshu.com/p/820e7b004b48



# Spring Testing

`ReflectionTestUtils`：

可以在测试时更改常量值，设置非公共字段，调用非公共setter方法或在测试应用序代码时调用非公共配置或生命周期回调方法的场景中使用这些方法。她可以在以下的场合使用：

- 例如@Autowired，@Inject和@Resource，一般作用的字段为私有或受保护的，可以获取其非公有字段的值。



## 集成测试的目标

Spring集成测试主要支持以下目的：

- 在测试之间管理Spring IoC容器缓存。
- 提供测试实例的依赖注入。
- 提供适合集成测试的事务管理。
- 提供特定于Spring的基类，帮助开发人员编写集成测试

### 1. 上下文管理和缓存

Spring TestContext Framework提供Spring`ApplicationContext`实例和`WebApplicationContext`实例的一致加载以及这些上下文的缓存。支持缓存加载的上下文非常重要，因为启动时间可能会成为一个问题 - 不是因为Spring本身的开销，而是因为Spring容器实例化的对象需要时间来实例化。例如，具有50到100个Hibernate映射文件的项目可能需要10到20秒来加载映射文件，并且在每个测试夹具中运行每个测试之前产生该成本会导致整体测试运行速度变慢，从而降低开发人员的工作效率。

**测试类通常需要声明 XML或Groovy配置元数据的资源位置（通常在类路径中，或者在的配置类中）**。

默认情况下，**一旦加载，配置的`ApplicationContext`将重复用于每个测试**。因此，每个测试套件仅产生一次设置成本，并且后续测试执行要快得多。在此上下文中，术语“测试套件”表示所有测试都在同一JVM中运行 - 例如，所有测试都是针对给定项目或模块从Ant，Maven或Gradle构建的。在不太可能的情况下，测试会破坏应用程序上下文并需要重新加载（例如，通过修改bean定义或应用程序对象的状态），可以将TestContext框架配置为在执行下一个之前重新加载配置并重建应用程序上下文测试。

## 注解

```properties
@ContextConfiguration: 声明应用程序上下文资源位置或用于加载上下文的带注释的类

@WebAppConfiguration: 作用在测试类上确保加载的是WebApplicationContext，默认加载src/main/webapp下的资源，在后台使用资源基本路径来创建MockServletContext，它充当测试的WebApplicationContext的ServletContext。

@ContextHierarchy

@ActiveProfiles

@TestPropertySource

@DirtiesContext

@TestExecutionListeners

@Commit

@Rollback

@BeforeTransaction

@AfterTransaction

@Sql

@SqlConfig

@SqlGroup
```

```java
@ContextConfiguration("/test-config.xml") 
或者
@ContextConfiguration(classes = TestConfig.class)
public class XmlApplicationContextTests {
    // class body...
}
```



# 五、延伸知识

## 5.1 正向代理和反向代理

### 5.1.1 正向代理

正向代理，**代理的是客户端**。为了从原始服务器取得内容，客户端向代理服务器发送一个请求，并且指定目标服务器，代理服务器转发请求到目标服务器，并且将获得的内容返回给客户端。从用户角度出发，正向代理**需要客户端进行一些特别的设置才能使用**。最常见的使用场景的就是**翻墙**。

### 5.1.2 反向代理

反向代理，**代理的是服务端**，为服务器收发请求，并进行路由分发，使真实服务器对客户端不可见。反向代理**不需要你做任何设置**，直接访问服务器真实ip或者域名，但是服务器内部会自动根据访问内容进行跳转及内容返回，你不知道它最终访问的是哪些机器。

Nginx作为时下最流行的HTTP服务器之一，同时它是一个反向代理服务器。

### 5.1.3 区别

从上面的描述也能看得出来正向代理和反向代理有以下几点不同：

- **代理对象不同**。正向代理，代理的客户端；反向代理，代理的是服务端。
- **用户操作不同**。正向代理，需要用户进行设置；反向代理不需要。
- **是否指定目标服务器**。正向代理，有明确的访问目标；反向代理，打到代理服务器，由代理服务器进行路由分发，这个阶段对客户端是透明的。



![](images/屏幕快照 2019-07-12 11.09.25.png)

正向代理中，proxy和client同属一个LAN，对server透明； 

反向代理中，proxy和server同属一个LAN，对client透明。

实际上proxy在两种代理中做的事都是代为收发请求和响应，不过从结构上来看正好左右互换了下，所以把前者那种代理方式叫做正向代理，后者叫做反向代理。



用途上的区分：

- 正向代理：正向代理用途是为了在防火墙内的局域网提供访问internet的途径。另外还可以使用缓冲特性减少网络使用率。
- 反向代理：反向代理的用途是将防火墙后面的服务器提供给internet用户访问。同时还可以完成诸如负载均衡等功能。



从安全性来讲：

- 正向代理：正向代理允许客户端通过它访问任意网站并且隐蔽客户端自身，因此你必须采取安全措施来确保仅为经过授权的客户端提供服务。
- 反向代理：对外是透明的，访问者并不知道自己访问的是代理。对访问者而言，他以为访问的就是原始服务器。

参考文献：

[正向代理和反向代理](https://www.jianshu.com/p/208c02c9dd1d)

[Nginx正向代理和反向代理](https://www.jianshu.com/p/ae76c223c6ef)























































