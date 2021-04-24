# 服务发布与注册

服务发布的过程：

- dubbo组件扫描，加载（寻找服务接口）
- URL驱动，url组装
- 启动服务
- 注册服务到注册中心上



## DubboComponentScan

核心的实现`DubboComponentScanRegistrar`

```java
public class DubboComponentScanRegistrar implements ImportBeanDefinitionRegistrar {

    //注册@DubboComponentScan注解扫描路径下的bean到IOC容器中
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
				
       //获取注解@DubboComponentScan定义的所有包扫描路径
      Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);
      
      //
      registerServiceAnnotationBeanPostProcessor(packagesToScan, registry);
		
      // @since 2.7.6 Register the common beans
      registerCommonBeans(registry);
    }
  
  private void registerServiceAnnotationBeanPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
				//构造并注册ServiceAnnotationBeanPostProcessor后置处理对象
        BeanDefinitionBuilder builder = rootBeanDefinition(ServiceAnnotationBeanPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }
}
```



## ServiceAnnotationBeanPostProcessor

```java
public class ServiceClassPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware,
        ResourceLoaderAware, BeanClassLoaderAware {
    	....
      
    	//bean装载完成后，会执行后置处理方法
      @Override
      public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) 
        throws BeansException {

          //注册一个DubboBootstrapApplicationListener对象，
          //会注册监听ApplicatonContext(IOC容器)刷新和关闭事件，会启动和关闭dubboBootstrap
          registerBeans(registry, DubboBootstrapApplicationListener.class);
					
         //注册packageScan路径下定义的bean
          Set<String> resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);
          if (!CollectionUtils.isEmpty(resolvedPackagesToScan)) {
              registerServiceBeans(resolvedPackagesToScan, registry);
          } else {
              if (logger.isWarnEnabled()) {
                  logger.warn("packagesToScan is empty , ServiceBean registry will be ignored!");
              }
          }
      } 
}
```

注册dubboBean:

```java
//扫描packageScan路径下定义的@Dubbo注解的bean
private void registerServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
  //创建并设置DubboClassPathBeanDefinitionScanner对象
  DubboClassPathBeanDefinitionScanner scanner =
    new DubboClassPathBeanDefinitionScanner(registry, environment, resourceLoader);
	
  //设置生成一个bean的name规则
  BeanNameGenerator beanNameGenerator = resolveBeanNameGenerator(registry);
  scanner.setBeanNameGenerator(beanNameGenerator);

  //兼容老版本，扫描DubboService.class和Service.class注解定义的bean
  serviceAnnotationTypes.forEach(annotationType -> {
    scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
  });

  for (String packageToScan : packagesToScan) {
    // Registers @Service Bean first
    scanner.scan(packageToScan);
    
    //查找所有被@Service注解定义的bean，并封装成BeanDefinitionHolders 对象
    Set<BeanDefinitionHolder> beanDefinitionHolders =
      findServiceBeanDefinitionHolders(scanner, packageToScan, registry, beanNameGenerator);

    if (!CollectionUtils.isEmpty(beanDefinitionHolders)) {
      //注册ServiceBean
      for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
        registerServiceBean(beanDefinitionHolder, registry, scanner);
      }

      if (logger.isInfoEnabled()) {
        logger.info(beanDefinitionHolders.size() + " annotated Dubbo's @Service Components { " +beanDefinitionHolders + " } were scanned under package[" + packageToScan + "]");
      }

    } else {

      if (logger.isWarnEnabled()) {
        logger.warn("No Spring Bean annotating Dubbo's @Service was found under package["
                    + packageToScan + "]");
      }
    }
  }
}
```

