### KAFKA通知
客户端注册任务时，将通知协议设置为KAFKA，通知地址为Topic名称。服务端触发执行后，会将通知消息投递到指定Kafka集群的Topic中。默认情况下，服务端会检测是否配置了spring.kafka.bootstrap-servers参数，如未配置，则不会启用预置的kafka通知。
#### 1. 配置
当选择Kafka作为延迟任务触发通知时，服务端需增加如下参数配置。
```yaml
spring: 
  kafka:
    bootstrap-servers: "127.0.0.1:9092" # 实际kafka链接地址
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer  # 按需配置
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer # 按需配置
      acks: 1 # 按需配置
      retries: 1  # 按需配置
```
##### 注：作者使用springboot集成kafka的作为预置方案，如需自定义集成kafka，可以按如下步骤。

* 移除根pom.xml文件中对kafka库的依赖；
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```
* 删除com.findthinks.delay.job.scheduler.KafkaJobTrigger
* 自定义实现IJobTrigger，将通知消息投递到指定的kafka集群。

#### 2. 任务注册样例
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/submit/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial",
      "triggerTime":1666621588,
      "callbackProtocol":"KAFKA",
      "callbackEndpoint":"a_topic_for_notify",
      "jobInfo":"I am a delay job.",
      "type":5
    }'
```
```
{
  "code":"ok",
  "message":"success"
}
```
#### 3. 通知消息样例
```
{
    "outJobNo":"job_no_tutorial",
    "triggerTime":1666621589,
    "jobInfo":"I am a delay job.",
}
```