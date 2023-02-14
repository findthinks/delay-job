## HTTP通知
到达任务触发时间点，服务端会主动发起对客户端回调，客户端收到回调后，应异步处理业务并立即响应回调接收情况。
#### 回调请求
POST /api/v1/client/callback (实际为客户端注册任务时填写的回调地址)
##### 请求参数
|名称|类型|必选|说明|
|---|---|---|---|
|outJobNo|string| 是 |外部任务编号（长度32位内字符串）|
|triggerTime|number| 是 |触发时间（秒）|
|jobInfo|string| 是 |透传参数|
##### 响应参数
|名称|类型|必选|说明|
|---|---|---|---|
|code|string| 是 |响应业务编码：ok(成功)，其它为失败|
|msg|string| 是 |响应描述|
> 回调请求响应示例(curl)
```
curl -X 'POST' 'http://callbackIp:port/api/v1/client/callback' \
  -H 'Content-Type:application/json' \
  -d'{
      "outJobNo":"job_no_tutorial",
      "triggerTime":1666621589,
      "jobInfo":"I am a delay job.",
    }'
```
```
{
  "code":"ok",
  "message":"success"
}
```