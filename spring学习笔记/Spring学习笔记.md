 #   从servlet到ApplicationContext经历了什么？



## IOC 中3个重要的类

 	![](/Users/machen/Documents/data_struct/spring学习笔记/images/WX20200914-232210.png)

 IOC和DI组件主要是在 `beans`模块中。springIOC和beans的基本执行顺序：

- 调用servlet 的 init() 方法，并且要初始化ApplicationContext对象
- 读取配置文件（properties / xml / yml文件 )，配置文件保存到内存中 `BeanDefinition`
- 扫描相关的类
- 初始化IOC容器，并且实例化对象（包装器模式）`BeanWrapper`
- 完成DI注入



`ApplicationContext` - 简单的理解他就是工厂类

- getBean()：从IOC容器中获取一个bean实例，并且完成这个bean的DI注入
- spirng默认是单例，而且是   延时加载（Lazy）的【用的时候在加载】