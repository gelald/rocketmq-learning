# RocketMQ 5.x 升级方案

## 1. 版本规划

| 组件 | 当前版本 | 目标版本                                           | 备注                        |
|------|---------|------------------------------------------------|---------------------------|
| JDK | 1.8 | **17**                                         | Spring Boot 3.x 强制要求      |
| Spring Boot | 2.6.1 | **3.4.x**                                      | 配合 v5 starter 主流组合        |
| RocketMQ Server | - | **5.2.0**                                      | 稳定版并且对齐 SDK 版本            |
| 原生 Client | `rocketmq-client:4.9.7` | `rocketmq-client-java:5.2.0`                   | gRPC 协议，全新 API            |
| Spring Starter | `rocketmq-spring-boot-starter:2.2.2` | `rocketmq-v5-client-spring-boot-starter:2.3.5` | 面向 RocketMQ 5.x 的 starter |

### 升级必要性说明

- `rocketmq-client-java` 5.x 本身支持 JDK 8+，但 `rocketmq-v5-client-spring-boot-starter` 2.3.5 配合 Spring Boot 3.x 是主流组合
- Spring Boot 3.x 强制要求 JDK 17+
- 作为新 demo 项目，没有历史包袱，直接上 JDK 17 + Spring Boot 3.x

---

## 2. 架构变化：4.x vs 5.x

```
4.x (当前)                              5.x (目标)
┌──────────────────────┐               ┌──────────────────────┐
│ Producer              │               │ Producer              │
│ DefaultMQProducer     │               │ Producer (Builder)    │
│                      │               │                      │
│ Consumer              │               │ Consumer              │
│ DefaultMQPushConsumer │               │ PushConsumer          │
│ DefaultMQPullConsumer │               │ SimpleConsumer (新)   │
│                      │               │                      │
│ Protocol: Remoting    │               │ Protocol: gRPC        │
│ Artifact: rocketmq-   │               │ Artifact: rocketmq-   │
│          client       │               │          client-java  │
└──────────────────────┘               └──────────────────────┘
```

### API 对照表

| 4.x | 5.x | 说明 |
|-----|-----|------|
| `new DefaultMQProducer(group)` | `Producer.newBuilder().build()` | Builder 模式创建 |
| `new DefaultMQPushConsumer(group)` | `PushConsumer.newBuilder().build()` | Builder 模式创建 |
| `new Message(topic, tag, body)` | `MessageBuilder.newBuilder().build()` | 消息构建方式 |
| `producer.send(msg)` | `producer.send(msg)` | 发送接口简化 |
| `consumer.registerMessageListener()` | `PushConsumer.subscribe()` | 消费方式重构 |
| 无 | `SimpleConsumer` | 5.x 新增，替代 PullConsumer |
| Remoting 协议直连 Broker | gRPC 协议连 Proxy | 需要 Proxy 组件 |

---

## 3. Maven 依赖变更

### 3.1 根 POM 版本管理

```xml
<properties>
    <java.version>17</java.version>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <spring.boot.version>3.4.x</spring.boot.version>
    <rocketmq-client-java.version>5.2.0</rocketmq-client-java.version>
    <rocketmq-v5-starter.version>2.3.5</rocketmq-v5-starter.version>
</properties>
```

### 3.2 原生 Client 模块依赖

```xml
<!-- 4.x -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client</artifactId>
    <version>4.9.7</version>
</dependency>

<!-- 5.x -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-client-java</artifactId>
    <version>5.2.0</version>
</dependency>
```

### 3.3 Spring Starter 模块依赖

```xml
<!-- 4.x -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.2.2</version>
</dependency>

<!-- 5.x -->
<dependency>
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-v5-client-spring-boot-starter</artifactId>
    <version>2.3.5</version>
</dependency>
```

---

## 4. 项目结构

保持现有三模块架构不变：

```
rocketmq-learning-v5/
├── rocketmq-v5-common/              # 公共依赖、常量、工具类
│   ├── pom.xml
│   └── src/main/java/
├── rocketmq-v5-client/              # 原生 API 演示
│   ├── pom.xml
│   ├── rocketmq-v5-producer-client/ # 原生 Producer
│   └── rocketmq-v5-consumer-client/ # 原生 Consumer (PushConsumer + SimpleConsumer)
└── rocketmq-v5-starter/             # Spring Boot 演示
    ├── pom.xml
    ├── rocketmq-v5-producer-starter/ # Spring Template Producer
    └── rocketmq-v5-consumer-starter/ # Spring Annotation Consumer
```

---

## 5. 前置条件

### 5.1 RocketMQ 5.x Proxy

5.x 的 gRPC 客户端**必须通过 Proxy 组件接入**，不能直连 Broker。部署时需启动：

- Nameserver
- Broker
- **Proxy**（5.x 新增组件）

启动顺序：Nameserver → Broker → Proxy

### 5.2 本地开发环境

- JDK 17+
- Maven 3.6+
- RocketMQ 5.5.0（含 Proxy）

---

## 6. 核心风险点

1. **API 完全重写** — 5.x 原生 Client 的 API 不是向下兼容的版本升级，代码需要重写
2. **Proxy 依赖** — gRPC 客户端必须经过 Proxy，需确保 Proxy 正确部署和配置
3. **Spring Starter 全新 artifact** — `rocketmq-v5-client-spring-boot-starter` 是独立的 starter，注解和配置方式与旧版不同
4. **Spring Boot 3.x 迁移** — `javax.*` → `jakarta.*` 包名变更，Swagger 等依赖需要同步升级

---

## 7. 参考资料

- [RocketMQ 5.x Java Client SDK 官方文档](https://rocketmq.apache.org/docs/sdk/02java/)
- [rocketmq-clients GitHub](https://github.com/apache/rocketmq-clients)
- [rocketmq-spring GitHub](https://github.com/apache/rocketmq-spring)
- [rocketmq-v5-client-spring-boot-starter 2.3.5 (Maven)](https://mvnrepository.com/artifact/org.apache.rocketmq/rocketmq-v5-client-spring-boot-starter/2.3.5)
- [RocketMQ Release Notes](https://rocketmq.apache.org/release-notes/)
- [RocketMQ 5.0 API 和 SDK 演进](https://www.alibabacloud.com/blog/the-evolution-of-rocketmq-5-0-api-and-sdk_599582)
