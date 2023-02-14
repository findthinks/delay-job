## HTTP接入
### 1. 任务注册
POST /api/v1/submit/job
#### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号（长度32位内字符串）|
|type|integer| 是 |任务类型：5(普通任务)、10（可暂停计时任务）|
|triggerTime|number| 是 |触发时间（秒）|
|callbackProtocol|string| 是 |回调协议：HTTP、GRPC、KAFKA|
|callbackEndpoint|string| 是 |回调地址|
|jobInfo|string| 是 |透传参数|
#### 响应参数
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
### 2. 批量任务注册
POST /api/v1/submit/jobs
#### 请求参数
|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|none|array| 是 |json数组|
|outJobNo|body|string| 是 |外部任务编号（长度32位内字符串）|
|type|body|integer| 是 |任务类型：5(普通任务)、10（可暂停计时任务）|
|triggerTime|body|number| 是 |触发时间（秒）|
|callbackProtocol|body|string| 是 |回调协议：HTTP、GRPC、KAFKA|
|callbackEndpoint|body|string| 是 |回调地址|
|jobInfo|body|string| 是 |透传参数|

#### 响应参数
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

### 3. 暂停任务计时
POST /api/v1/pause/job
#### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
#### 响应参数
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
### 4. 恢复任务计时
POST /api/v1/resume/job
#### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
#### 响应参数
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
### 5. 取消任务
POST /api/v1/cancel/job
#### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号|
#### 响应参数
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