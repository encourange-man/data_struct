# SpringBoot 核心运行原理

Spring Boot最核心的功能就是自动配置，功能的实现都是基于**“约定优于配置”**的原则。**那么Spring Boot是如何约定，又是如何实现自动配置功能的呢？**

![](images/QQ20210410-111819.png)

自动状态的基本流程：

- Spring Boot通过**`@EnableAutoConfiguration`**注解开启自动配置，
- 扫描加载**`spring.factories`**中注册的各种**`AutoConfiguration`**类，
- 当某个**`AutoConfiguration`**类满足其注解**`@Conditional`**指定的生效条件（Starters提供的依赖、配置或Spring容器中是否存在某个Bean等）时，实例化该**`AutoConfiguration`**类中定义的Bean（组件等），并注入Spring容器，就可以完成依赖框架的自动配置



下面简单介绍一下上面各个组件或者注解的作用：

- **`@EnableAutoConfiguration`**：扫描各个jar包下的s`pring.factories`文件，并加载文件中注册的AutoConfiguration类等。
- **`spring.factories`**：配置文件，位于META-INF目录下，按照指定格式注册了自动配置的`AutoConfiguration`类。
-  **`AutoConfiguration`**：自动配置类，代表了`XXAutoConfiguration`命名的自动配置类
- **`@Conditional`**：条件注解及其衍生注解，当满足该条件注解时才会实例化AutoConfiguration类。
- **`Starters`**：三方组件的依赖及配置，如果是自定义的starter，该项目还需包含spring.factories文件、AutoConfiguration类和其他配置类



## 自动装配的过程

`@SpringBootApplication`注解是Spring Boot项目核心的注解，它里面组合了`@EnableAutoConfiguration`，这用于开启自动装配。下面看一下`@SpringBootApplication`注解组合的结构图：

**注意一点：`@EnableAutoConfiguration`注解所在类具有特定的意义，通常会被作为扫描注解@Entity的根路径。**

![](images/QQ20210410-113639.png)

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {

	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
	
  Class<?>[] exclude() default {};
	
  String[] excludeName() default {};
}
```

**`@EnableAutoConfiguration`的关键功能是通过`@Import`注解导入的`ImportSelector`来完成的。**

- **`@Improt`**

  主要提供导入配置类的功能，也可以导入实现了`ImportSelector`和`ImportBeanDefinitionRegistrar`的类，还可以通过@Import导入普通的POJO。

- **`ImportSelector`**

  `@Import`的许多功能都需要借助接口`ImportSelector`来实现，`ImportSelector`决定可引入哪些`@Configuration`。



下面通过流程图来整体描述一下自动装配的过程：

![](images/QQ20210410-145002.png)



```java
public class AutoConfigurationImportSelector implements DeferredImportSelector, 		 
			BeanClassLoaderAware,ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered{
     ....
       
   @Override
	 public String[] selectImports(AnnotationMetadata annotationMetadata) {
      //是否开启自动装配，默认开启
       if (!isEnabled(annotationMetadata)) {
        return NO_IMPORTS;
      }
       
      //加载元数据配置，默认加载类路径 META-INF/spring-autoconfigure-metadata.properties配置
      //元数据中配置了自动加载的条件，spring boot会在第5步 将收集好的配置类进行一次过滤
      AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
          .loadMetadata(this.beanClassLoader);
      AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(autoConfigurationMetadata, annotationMetadata);
      
       return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
	}
        
}
```



```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata,
			AnnotationMetadata annotationMetadata) {
   	//检查自动装配是否开启
		if (!isEnabled(annotationMetadata)) {
			return EMPTY_ENTRY;
		}
  	//获取@EnableAutoConfiguration注解的属性 exclude 和 excludeName
		AnnotationAttributes attributes = getAttributes(annotationMetadata);
    //1.获取所有META-INF目录下的 spring.factories文件中 EnableAutoConfiguration 的所有配置类列表
		List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
    //2.对上面获取到的注册配置类去重
		configurations = removeDuplicates(configurations);
		//3.获取注解属性中 exclude 和 excluedName 指定的要排除的配置列列表
    Set<String> exclusions = getExclusions(annotationMetadata, attributes);
		//检查被排除的类是否是 auto-configuration 类，不是抛出异常
    checkExcludedClasses(configurations, exclusions);
		//4.从加载的自动配类列表中移除要排除的配置类
    configurations.removeAll(exclusions);
    //5.根据autoConfigurationMetadata定义的条件，在对配置类进行一次过滤
		configurations = filter(configurations, autoConfigurationMetadata);
    //6.将配置类和要排除的类传入监听器中
		fireAutoConfigurationImportEvents(configurations, exclusions);
		return new AutoConfigurationEntry(configurations, exclusions);
	}

```



>  **在排除了`exclude`和`excludeName`指定的配置类后，还要在进行一个过滤，这次过滤是为了什么？**

首先明确参加过滤的数据有：

- configurations：List，经过初次过滤去重后的自动配置组件列表

- autoConfigurationMetadata：元数据文件`META-INF/spring-autoconfigure-metadata.properties`中配置对应实体类

- Auto-ConfigurationImportFilter：`META-INF/spring.factories`中配置key为`Auto-ConfigurationImportFilter`的Filters列表

对自动配置组件列表进行再次过滤，过滤条件为该列表中自动配置类的注解得包含在`OnBeanCondition`、`OnClassCondition`和`OnWebApplicationCondition`中指定的注解，依次包含`@ConditionalOnBean`、`@ConditionalOnClass`和`@ConditionalOnWebApplication`。



在`spring-boot-autoconfigure`中默认配置了3个筛选条件，它们都实现了`AutoConfigurationImportFilter`接口：

- `OnBeanCondition`
- `OnClassCondition`
- `OnWebApplicationCondition`



Spring Boot了**`@Conditional`**注解，可根据**是否满足指定条件来决定是否进行Bean的实力化配置**。`@Conditional`注解来提供了很多衍生的注解：

- **`@ConditionalOnBean`**：容器中有指定Bean的条件下。
- **`@ConditionalOnClass`**：在classpath类路径下有指定类的条件下。
- **`@ConditionalOnMissingBean`**：当容器里没有指定Bean的条件时
- **`@ConditionalOnProperty`**：在指定的属性有指定值的条件下。
- `@ConditionalOnResource`：在指定的属性有指定值的条件下。
- `@ConditionalOnCloudPlatform`：当指定的云平台处于active状态时。
- `@ConditionalOnExpression`：基于SpEL表达式的条件判断。
- `@ConditionalOnJava`：基于JVM版本作为判断条件
- `@ConditionalOnJndi`：在JNDI存在的条件下查找指定的位置。
- `@ConditionalOnWebApplication`：在项目是一个Web项目的条件下。
- `@ConditionalOnNotWebApplication`：在项目不是一个Web项目的条件下。



`spring-autoconfigure-metadata.properties`文件内的配置格式如下:

> 自动配置类的权限定名.注解名称 = 值



以上完成了自动配置类的读取和筛选



## Spring Boot 构造流程分析

有个遗留问题：扫描到了自动配置了，IOC容器到底是个什么？Map？

```java
@SpringBootApplication
public class GiantServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiantServerApplication.class, args);
    }
}
```





##  ## Spring Boot运行流程分析



