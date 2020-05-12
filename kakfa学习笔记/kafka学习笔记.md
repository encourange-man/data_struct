# 前沿

Kafka官方文档：[kafka官方文档](http://kafka.apache.org/documentation.html#producerapi)

Kafka的定位是：**分布式流处理平台**，一个流处理平台有以下三个特性：

- 作为消息队列，发布和订阅流中的记录。
- 





**反思与思考：**

- kafka是什么？与其他消息队列有什么特别？
- 生产者如何将生产的消息写入Kafka？
- 消费者如何从Kafka集群消费消息？
- Kafka如何存储消息？
- Kafka集群如何管理调度，如何进行消息负载均衡？
- 各组件间如何进行通信等诸多问题
- zookeeper在架构图中扮演了什么角色，为什么需要它？
- kafka为啥选用文件系统作为存储 29页
- Kafka在数据写入及数据同步采用了零拷贝（zero-copy）技术（什么是零拷贝？）

# 一.Kafka基础

## **1.kafka基本概念**

- **Broker(代理)**

一个Kafka实例成为Broker，多个实例组成了Kafka集群。每个Broker都有一个对应的id(非负)。

- **Topic(主题)**

**一组消息归纳为一个主题（Topic**），一个主题就是对消息的一个分类。

- **Record(消息)**

record是kafka通信的基本单位。由一个固定长度的消息头和一个可变长度的消息体构成

- **Partition(分区)**

**每个主题又被分成一个或多个分区**。**每个分区由一系列有序、不可变的消息组成**，是一个**有序队列**。理论上分区数越多吞吐量约高，**kafka只能保证一个分区之内消息的有序性，并不能保证跨分区消息的有序性**。**每条消息被追加到相应的分区中，是顺序写磁盘**，因此效率非常高，这是kafka高吞吐量的一个重要保证（每次读写都会寻址->写入，硬盘最喜欢顺序IO）

- **Replica(副本)**

每个分区在物理上对应为一个文件夹，**每个分区又有一个至多个副本**，分区的每个副本在逻辑上抽象为一个日志(log)对象。每个主题对应的分区数可以在Kafka启动时配置文件中配置，也可以在创建主题时指定。

- **leader副本**

一个分区有多个副本，就**需要保证多个副本数据的一致性**。Kafka会选择该分区的一个副本作为leader副本，只有**leader副本才负责**	

**客户端读写请求**。

- **Follower副本**

Follower副本从Leader副本同步消息，

- **偏移量**

任何发布到分区的消息都会被直接追加到日志文件的尾部，每条消息在日志文件中都会对应一个按序递增的偏移量。

- **logSegment日志段（逻辑概念）**

一个日志又被划分为多个日志段，日志段时Kafka日志对象分片的最小单位。一个日志段对应磁盘上一个具体日志文件和两个索引文件（**.index**和**.timeinde**x）。日志文件用于保存实际数据。**.index文件用于消息偏移量索引**，**.timeindex文件是消息时间戳索引文件**。

- **消费者(Consumer)和消费组(ConsumerGroup)**

Kafka中每个消费者属于一个特定消费组，每个消费者有全局危矣的id，同一个主题的一条消息只能被同一个消费组下某一个消费者消费，不同消费组的消费者可同时消费该消息（同一个消费组下的消费者只能消费同一个主题下的唯一一个消息）。

- **zookeepr**

**kafka利用zookeeper保存相应元数据信息**，Kafka在启动或运行时会在Zookeeper上创建相应节点来保存元数据信息，Kafka通过监听机制来监听节点元数据的变化。

<img src="images/WX20200509-102247@2x.png" style="zoom:50%;" />

## **2.kafka特性**

**2.1 消息持久化**

**2.2高吞吐量**

# **二.kafka核心组件**

主要介绍kafka核心功能模块，主要包括：

- 延迟操作组件
- 控制器
- 协调器
- 网络通信
- 日志管理器
- 副本管理器
- 动态配置管理器
- 心跳检测

## **2.1延迟操作组件**

**延迟操作**指的是：不需要立即执行，需要满足一定条件才能触发执行的操作。

# **三.kafka相关配置**

## **3.1 服务端参数配置**

```properties
## 指定broker要连接的zookeeper集群地址，多个节点使用,隔开 
zookeeper.connect=localhost:2181 
## 客户端要连接broker的入口地址列表 
listeners=PLAINTEXT://:9092
## 集群中broker的唯一标示 
broker.id=0 
## 记录日志存放的根目录（kafka的所有消息都会保存在磁盘上）
log.dirs=/usr/local/var/lib/kafka-logs
```





## **3.2 生产者**

代码实例**【todo】**

```java
//消息对象ProducerRecord
public class ProducerRecord<K, V> {    
  private final String topic;           //主题   
  private final Integer partition;      //分区号   
  private final Headers headers;        //消息头部    
  private final K key;                //键，同一个key会被划分到同一个分区中（消息可以在此归纳）   
  private final V value;                //消息体   
  private final Long timestamp;         //时间戳 
}
```

必要的参数配置：

```properties
## 生产者连接kafka集群所需要的broker地址，多个地址使用,隔开 
spring.kafka.consumer.bootstrap-servers: 
## key的序列化操作器（broker接受的消息必须以byte[]形式） 
spring.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.StringDeserializer
## value的序列化操作器（broker接受的消息必须以byte[]形式）
spring.kafka.consumer.value-deserializer:io.confluent.kafka.serializers.KafkaAvroDeserializer
```

## **3.3 消费者**



