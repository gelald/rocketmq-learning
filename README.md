# RocketMQ-Learning

> 这个项目使用两种依赖方式整合了 RocketMQ 常见的使用方式
>
> 适合入门学习的小伙伴，也可以作为平时开发要使用 RocketMQ 时参考的原型

## 模块说明

### rocketmq-learning

这个是所有模块的父工程，主要是统一子工程使用的依赖版本，统一打包配置

本项目使用的主要依赖的版本:

| 🔧依赖                         | 📖版本  |
|------------------------------|-------|
| spring-boot                  | 2.6.1 |
| rocketmq-client              | 4.9.3 |
| rocketmq-spring-boot-starter | 2.2.2 |

### rocketmq-common

这个模块负责了一些基础相关的工作，能让其他模块更专注 RocketMQ 的使用：

- 统筹共用的常量
- 统一异常处理
- 统一包装响应
- knife4j 统一配置

### rocketmq-client

这个模块展示了 SpringBoot 集成 rocketmq-client 的使用方式

其中 `rocketmq-consumer-client` 是消费者模块，`rocketmq-producer-client` 是生产者模块

### rocketmq-starter

这个模块展示了 SpringBoot 集成 rocketmq-spring-boot-starter 的使用方式

其中 `rocketmq-consumer-starter` 是消费者模块，`rocketmq-producer-starter` 是生产者模块

## 部署说明

### RocketMQ 部署方式

RocketMQ 部署方式官网中提供了源码部署方式，我们这里使用 docker-compose 的部署方式

在 rocketmq-learning 下有一个目录 docker，这个目录下包含了：

- docker-compose.yaml：docker compose 配置文件，里面已经编辑好 name-server、broker、dashboard 三个容器启动的相关配置
- rocketmq-broker：broker 容器中挂载的目录，其中 conf 目录下是 broker 的启动配置，启动时需要修改其中的 brokerIP1 为本机 IP
  地址
- rocketmq-dashboard：dashboard 容器中挂载的目录
- rocketmq-name-server：name-server 容器中挂载的目录

启动方式：在 docker-compose.yaml 文件同目录下打开终端，输入命令 `docker-compose up -d` 启动

验证是否启动成功：输入命令 `docker-compose ps` 可以看到 name-server、broker、dashboard 三个容器

其中 broker 容器有可能会 `exited with code 253`
，解决方法可以看这篇文章：[Docker 搭建部署 RocketMQ 遇到broker 253问题](https://blog.csdn.net/weixin_43955543/article/details/124047740)

部署正常打开 dashboard 映射宿主机的端口能看到 rocketmq 控制台👇：

![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20220905215420.png)

### 项目部署方式

无论是使用哪一种集成方式，启动前都要在生产者消费者模块中 resource 目录下的 application.yml 文件中修改 name-server 地址

如需启动其他生产者/消费者，需要在配置文件中手动设置为 true，具体在下面详细说明。

因为生产者项目中都集成了knife4j文档组件，所以正常启动的标志是能打开文档页面👇。

| rocketmq-client | rocketmq-starter |
|-----------------|------------------|
| ![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20220904181131.png)                | ![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20220904181402.png)                 |


## 模块详细说明

这里介绍两个模块的使用方式以及一些特别的设计

### rocketmq-client

可以配合集成 `rocketmq-client` 的落地实现讲解来一起学习：[RocketMQ 操作落地 (rocketmq-client 方式)](https://gelald.github.io/javrin/writings/message-queue/RocketMQ-operation-client.html)

#### rocketmq-consumer-client

定义消费者的配置类都需要继承基类 `RocketMQBaseConsumerConfiguration`，并且在定义时都需要把定义出来的生产者加入到基类中管理消费者的 `mqConsumers` 集合中。这样做的目的是基类实现了 `DisposableBean` 这个后置处理器，在销毁 Bean 的时候先把集合中所有的生产者都逐一销毁，以便释放资源。

消费者与消息监听器的定义分离，方便独立维护，每一个消费者定义时都要传入特定的一个消息监听器，传入的监听器变量名为监听器类名**首字母小写**，这样 Spring 可以根据名字寻找具体的 Bean 注入。

例如：
```
@Bean
public DefaultMQPushConsumer defaultMQPushConsumer(MessageListenerConcurrently defaultListener) throws MQClientException {
        ...
}
```

除了默认的消费者，其他记录在 `RocketMQConsumerProperties.ConsumerSwitch` 类型的消费者支持开关配置，开关配置是为了不启动所有的消费者以便节省资源，当然全部设置为 `true` 也是能正常启动的。

#### rocketmq-producer-client

与消费者一致，定义生产者的配置类也需要继承基类 `RocketMQBaseProducerConfiguration`，并且在定义时也需要把生产者加入 `mqProducers` 集合中。

除了默认的生产者，顺序消息生产者和事务消息生产者支持开关配置，配置项可以参考 `RocketMQProducerProperties.ProducerSwitch`，全部设置为 `true` 也是可以正常启动的。

为了方便大家学习调试，本项目集成了 knife4j 文档组件，直接打开 localhost:9091/doc.html 就能进行调试了，正常启动后的界面：

![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20220905161550.png)

### rocketmq-starter

可以配合集成 `rocketmq-spring-boot-starter` 的落地实现讲解来一起学习：[RocketMQ 操作落地 (rocketmq-starter 方式)](https://gelald.github.io/javrin/writings/message-queue/RocketMQ-operation-starter.html)

#### rocketmq-consumer-starter

使用这种集成方式定义消费者变得非常简单，直接使用注解 `@RocketMQMessageListener`，另外如果同一个组要定义多个消费者实例，那么需要实现接口 `RocketMQPushConsumerLifecycleListener` 来修改实例名。

除了默认的消费者，其他记录在 `RocketMQConsumerProperties.ConsumerSwitch` 类型的消费者支持开关配置，开关配置是为了不启动所有的消费者以便节省资源，当然全部设置为 `true` 也是能正常启动的。

#### rocketmq-producer-starter

使用这种集成方式后，直接使用框架中定义好的生产者就可以了，非常方便。

其中 `LocalTransactionListener` 是生产者事务消息监听器，负责执行本地事务以及提供事务回查。

为了方便大家学习调试，本项目集成了 knife4j 文档组件，直接打开 localhost:9093/doc.html 就能进行调试了，正常启动后的界面：

![](https://wingbun-notes-image.oss-cn-guangzhou.aliyuncs.com/images/20220905165851.png)
