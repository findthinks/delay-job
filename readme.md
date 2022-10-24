## Delay-job 延迟任务调度系统
Delay-job是一个开源分布式、轻量级的延迟任务调度实现。

### 特性
* 支持特定事件发生后，延迟一定时间后触发调度
* 支持分布式分片任务调度
* 支持可视化管理任务分片
* 支持可视化管理延迟任务 
* 快速部署(只依赖Mysql)

### 架构
![alt 属性文本](docs/arch/arch_v1.0.png)
### 性能
* 秒级触发：秒级精确调度50000次/s
* 测试资源：CentOS7.9，2CPU，4GB Ram运行于腾讯云虚拟机

### 服务端部署
#### 1. 下载部署包
使用如下命令下载最新发布的系统的部署包。
```
wget https://github.com/**********/download/delay-job-bin-v0.6.1.zip
unzip delay-job-bin-v0.6.1.zip
```
#### 2. 创建数据库
提取docs/db/schema_init.sql执行建库，建表，数据初始化。
#### 3. 启动调度服务端
进入解压目录config目录，按实修改application.yml数据库相关配置。进入解压目录bin目录，使用startup.sh脚本启动服务。观察log/delay-job.log服务是否启动成功。
```
cd delay-job
./startup.sh
```

### 客户端接入
#### HTTP接入
##### 1. 任务注册
POST /api/v1/submit/job
###### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号（长度32位内字符串）|
|type|integer| 是 |任务类型：5(普通任务)、10（可暂停计时任务）|
|triggerTime|number| 是 |触发时间（秒）|
|callbackProtocol|string| 是 |回调协议：HTTP、GRPC|
|callbackEndpoint|string| 是 |回调地址|
|jobInfo|string| 是 |透传参数|
###### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|message|string| 是 |响应描述|
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/submit/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial",
      "triggerTime":1666621588,
      "callbackProtocol":"HTTP",
      "callbackEndpoint":"http://localhost:9092/callback",
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
##### 2. 批量任务注册
POST /api/v1/submit/jobs
###### 请求参数
|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|none|array| 是 |json数组|
|outJobNo|body|string| 是 |外部任务编号（长度32位内字符串）|
|type|body|integer| 是 |任务类型：5(普通任务)、10（可暂停计时任务）|
|triggerTime|body|number| 是 |触发时间（秒）|
|callbackProtocol|body|string| 是 |回调协议：HTTP、GRPC|
|callbackEndpoint|body|string| 是 |回调地址|
|jobInfo|body|string| 是 |透传参数|

###### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|message|string| 是 |响应描述|
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/submit/jobs' \
  -H 'Content-Type:application/json' \
  -d'[
      {
        "outJobNo":"job_no_tutorial1",
        "triggerTime":1666621588,
        "callbackProtocol":"HTTP",
        "callbackEndpoint":"http://localhost:9092/callback",
        "jobInfo":"I am a delay job.",
        "type":5
      },
      {
        "outJobNo":"job_no_tutorial2",
        "triggerTime":1666621589,
        "callbackProtocol":"HTTP",
        "callbackEndpoint":"http://localhost:9092/callback",
        "jobInfo":"I am a delay job too.",
        "type":5
      }
    ]'
```
```
{
  "code":"ok",
  "message":"success"
}
```

##### 3. 暂停任务计时
POST /api/v1/pause/job
###### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
###### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|message|string| 是 |响应描述|
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/pause/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial"
    }'
```
```
{
  "code":"ok",
  "message":"success"
}
```
##### 4. 恢复任务计时
POST /api/v1/resume/job
###### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
###### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|message|string| 是 |响应描述|
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/resume/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial"
    }'
```
```
{
  "code":"ok",
  "message":"success"
}
```
##### 5. 取消任务
POST /api/v1/cancel/job
###### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
###### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|message|string| 是 |响应描述|
> 请求响应示例(curl)
```
curl -X 'POST' 'http://localhost:1989/api/v1/cancel/job' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial"
    }'
```
```
{
  "code":"ok",
  "message":"success"
}
```

### 版本
#### 2022/10/22 - version 0.6.1
第一个试用版本。