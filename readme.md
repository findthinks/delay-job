<h1 align="center">
<img src="docs/other/logo.png"  width="400" />
</h1>
<h1 align="center">
    Delay-Job,high-performance distributed lightweight delay scheduling middleware
</h1>
<p align="center">
    <img src="https://img.shields.io/badge/build-passing-Green?style=float" />
    <img src="https://img.shields.io/badge/JDK-1.8+-Green?style=float&logo=openjdk" />
    <img src="https://img.shields.io/badge/Maven-3.0+-Green?style=float&logo=apachemaven" />
    <img src="https://img.shields.io/badge/Mysql-5.7+-Green?style=float&logo=mysql" />
    <img src="https://img.shields.io/badge/Docker-Support-yellow?style=float&logo=docker" />
    <img src="https://img.shields.io/badge/License-Apache%202.0-blueviolet" />
</p>

English | [简体中文](./readme.zh-CN.md)

### Use Cases
* 🐯How to close the order in time if the order has not been paid after a period of time?
* 🦁️How to remind the user that the information has not been completed after a period of time after registration?

The scene where the user triggers a specific event and expects to deal with it after a period of delay is the place to delay the task scheduling application

### Feature
* 🚀 Custom delay scheduling time
* 🔔 Task pause
* 🔥 Task Sharding
* ⚡ Visual management task sharding
* ✨ Visual management of delayed tasks
* 🔧 Rapid deployment

### Architecture
![img.png](docs/arch/arch_v1.0.png)
### Performance Index
* Second-level trigger: Second-level precise scheduling 50000 times/s
* Test resources: CentOS7.9, 2CPU, 4GB Ram

### Quick start
Required
* Java 1.8+
* Mysql 5.7+ 
#### 1. Download the binary package
  
Use the following command to download the binary package 
```
wget https://github.com/findthinks/delay-job/releases/download/0.6.1/delay-job-bin-0.6.1.zip
unzip delay-job-bin-v0.6.1.zip
```
#### 2. Create database
exec docs/db/schema_init.sql for creatting database, table and init data.
#### 3. Start the scheduling service
Enter the "config" directory, and modify the "application.yml" database related configuration according to the actual situation. Enter the "bin" directory, use the "startup.sh" script to start the service. Check whether the "log/delay-job.log" service starts successfully.
```
cd delay-job/bin
./startup.sh
```
#### 4. Add delay job
```
curl -X 'POST' 'http://localhost:1989/api/v1/submit/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"first_delay_job",
      "triggerTime":1676608523,
      "callbackProtocol":"LOG",
      "callbackEndpoint":"LOG",
      "jobInfo":"I am a delay job."
    }'
```
> Tips：callbackProtocol、callbackEndpoint as "LOG" at the same time, which can be used for debugging. After the task is triggered, trigger information will be output in the dispatching console.
#### 5. Trigger notification
```
2023-02-17 12:35:23||Job-Executor-0||INFO||com.findthinks.delay.job.scheduler.EchoJobTrigger:15||Job[outJobNo:first_delay_job] trigger success, CurrentTime:1676608523, TriggerTime:1676608523, CreateTime:1676608438.
```

### Management UI
After the service starts successfully, visit："http://localhost:1989"，The default username and password are "delay/delay".

#### 1. Scheduler management
Delay-job adopts the task sharding scheduling architecture. Through the scheduler view of the management ui, you can query the currently deployed scheduler information.
![img.png](docs/other/console_scheduler.png)
#### 2. Shard management
Sharding information can be configured on the management interface to add, enable, and disable shards. It is recommended that the number of available shards be 2 to 4 times the total number of schedulers. By default, 3 shards are preset for the scheduling cluster, and each shard will be automatically balanced among multiple schedulers.
![img.png](docs/other/console_shard.png)
#### 3. Task management
The registered task information can be queried through the management interface, and the precise query of the task number is currently supported.
![img.png](docs/other/console_job.png)

### 技术对接
#### 1. 任务接入
任务管理包括任务注册，任务批量注册，任务取消，暂停任务计时，恢复任务计时接口。
* [HTTP客户端接入](docs/http_job_register.md)
* [GRPC客户端接入](src/main/resources/pb/Job.proto)，客户端提取Job.proto文件生成任务注册代码，注册延迟任务。

> 注：<br/>
> 1、回调通知可以自由选择，如使用HTTP或者GRPC接口注册任务时，可以选择HTTP、GRPC、KAFKA中的任一种作为回调通知的方式。<br/>
> 2、默认本地GRPC任务管理接口发布地址为：localhost:1990

#### 2. 触发通知
到达任务触发时间，调度服务端会触发一个通知，支持HTTP接口回调，GRPC接口回调，KAFKA消息通知三种方式。
* [HTTP通知](docs/http_job_callback.md)，客户端提供POST回调接口，接收服务端回调请求。
* [GRPC通知](src/main/resources/pb/JobCallback.proto)，客户端提取JobCallback.proto文件，发布回调GRPC接口，接收服务端回调请求。
* [KAFKA通知](docs/kafka_job_callback.md)，服务端将任务触发事件消息投递到Kafka，业务端消费消息，处理延迟任务。
### 版本
#### 20223/2/14 - version 0.6.1
第一个可用版本。

### 微信交流
![alt 属性文本](docs/other/wechat_grp.png)
