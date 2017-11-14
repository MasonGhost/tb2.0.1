# 智播ChatRoom文档

## 大纲

>- [ChatRoomClient介绍](#ChatRoomClient介绍)
>- [MessageBuilder介绍](#MessageBuilder介绍)
>- [Message、IMConfig等数据介绍](#Message、IMConfig等数据介绍) 
>- [错误码](#错误码)

---




## ChatRoomClient介绍
##### 1. 通过聊天室id初始化ChatRoomClient
```java
ChatRoomClient client=new ChatRoomClient(roomId);
```
##### 2. 进入聊天室
```java
client.joinRoom();
```
##### 3. 离开聊天室
```java
client.leaveRoom();
```
##### 4. 查看房间成员
```java
client.leaveRoom();
```
##### 5. 发送文本消息
```java
client.sendTextMsg(text);
```
##### 6. 发送礼物消息
```java
client.sendGiftMessage(Object jsonstr);
```
##### 7. 点赞，type查看[type规定说明](#type规定说明)
```java
client.sendZan(int type);;
```
##### 8. 发送关注主播
```java
client.sendAttention();
```
##### 9. 发送自定义消息
```java
  /**
     * 发送自定义消息
     * @param isEnable  被禁言时，会不会屏蔽，true为可用，不屏蔽
     * @param jsonstr   可自定义JavaBean，自己对应解析
     */
client.sendMessage(boolean isEnable, Object jsonstr);
```
<font color="red">注意：</font>在不需要ChatRoomClient时，调用onDestroy()
```java
client.onDestroy();
```
##### 10. 监听器

```java

client.setImStatusListener(imStatusListener);
client.setImMsgReceveListener(imMsgReceveListener);
/**
*IM状态监听
*/
public interface ImStatusListener {
    void onConnected();
    void onDisconnect(int code, String reason);
    void onError(Exception error);

}
/**
*消息监听
*/
public interface ImMsgReceveListener {
    //收到消息
    void onMessageReceived(Message message);
    //发送消息回执
    void onMessageACKReceived(Message message);
    //加入聊天室回执
    void onChatRoomJoinACKReceived(ChatRoomContainer chatRoomContainer);
    //离开聊天室回执
    void onChatRoomLeaveACKReceived(ChatRoomContainer chatRoomContainer);
    //查询房间人数回执
    void onChatRoomMCACKReceived(ChatRoomContainer chatRoomContainer);
    //直播结束通知
    void onConverEndReceived(Conver conver);
}

```

---


## MessageBuilder介绍
```
   /**
     * 创建文本消息
     * @param cid  房间id
     * @param text 内容
     * @param rt  是否实时消息
     * @return  Message
     */
   MessageBuilder.createTextMessage(int cid, String text, boolean rt) ;
    /**
     * 创建指定目标文本消息
     * @param cid  房间id
     * @param uids  目标对象的id
     * @param text 内容
     * @param rt  是否必答
     * @return Message
     */
    MessageBuilder.createTagetTextMessage(int cid, List<Integer> uids, String text, boolean rt);
    /**
     * 赞
     * @param cid
     * @param type，对应SocketService.MESSAGE_TYPE_ZAN_YELLOW等
     * @param rt
     * @return Message
     */
    MessageBuilder.createZanMessage(int cid, int type,boolean rt)；
    /**
     * 关注
     * @param cid
     * @param rt
     * @return Message
     */
   MessageBuilder.createAttentionMessage(int cid,boolean rt)；
    /**
     * 自定义消息
     *
     * @param cid
     * @param isEnable  被禁言时，会不会屏蔽，true为可用，不屏蔽
     * @param jsonstr
     * @param rt
     * @return Message
     */
    MessageBuilder.createCustomMessage(int cid, boolean isEnable, Object jsonstr, boolean rt) ;
    /**
     * 指定目标自定义消息
     * @param cid
     * @param uids
     * @param isEnable
     * @param jsonstr
     * @param rt
     * @return Message
     */
    MessageBuilder.createCustomMessage(int cid, List<Integer> uids, boolean isEnable, Object jsonstr, boolean rt)；
    
     /**
     * 完全自定义消息
     */
   MessageBuilder.createMessage(int cid, List<Integer> uids, int type, boolean rt, Object ext);
    
```

---

## Message、IMConfig等数据介绍

### 1. Message介绍
```java
public class Message implements Serializable {
    public int from;
    public int to;
    public List<Integer> uid;
    public int type;
    public String text;
    public Object msg;
    public boolean rt;
    public int err;
    public long gag=-1;
}
```
Message属性说明
|名字|类型|必须|说明|
|:----:|:-------:|:----:|:----:|
|from  | int| 否|消息发送者的im_uid;发送者不需要填写|
|to  | int| 是|消息要发送到的会话ID|
|uid  | List<Integer>| 否|指定消息的具体接收者uid(int类型)；如果没有此项，消息将发送给会话中所有成员，如果是系统会话消息并且没有指定uid列表，将发往全部已注册用户。**注意：**因私有会话成员只有两个（自己和对方），所以不支持此参数|
|type  | int| 否|消息类型,详情查看[type规定说明](#type规定说明)|
|text  | String| 否|消息的文本内容|
|msg  | Object| 否|自定义消息，[礼物示例](#礼物示例)|
|rt  | boolean| 否|是否是实时消息，如果有此项且值为true时为实时消息，否则为普通消息|
|err  | int| 否|错误码|
|gag  | long| 否|0为永久禁言，大于0则为解禁时间戳|

type规定说明：
    
|  值  |  类型  |中文描述|
| :-- :| :--: |:--:|
| 0  | int  | 文本消息|
| 100  | int  | 用户自定义消息，被禁言时会被屏蔽|
| 128-199  | int   | 赠送礼物
| 200| int | 0xfee617色值的桃心    |
|201|int|0x54b98e色值的桃心|
|202|int|0x47b5da色值的桃心|
|203|int|0xfb667d色值的桃心|
| 210  | int  | 用户自定义消息，被禁言时不会被屏蔽|
| 255  | int   | 关注了主播    |
<font color="red">注意：</font>当用户被主播禁言后，0-127的消息是会被屏蔽掉



### 2. IMConfig介绍
```java
public class IMConfig implements Serializable {
    private int imUid;
    private String imPsw;
    private int mBin= ImService.BIN_MSGPACK;
    private boolean isZlib=true;
}
```
IMConfig属性说明

|  名字  | 类型  |	说明|
| :--: | :--: | :--: |
| imUid | int  | IM通信的id |
| imPsw | String| IM通信的密码 |
| mBin  | int  | 希望服务器返回的数据类型是json，还是msgpck,默认为msgpack  |
| isZlib | boolean| 客户端是否支持压缩，默认压缩 |


---
## 错误码
|  错误代码  | 错误代码类别  |	详细描述|
| :--: | :--: |:--:|
| 1000  | server err | 服务器发生了未知的异常，导致处理中断|
| 1010  | application err | 无效的数据包|
| 1011  | application err | 无效的数据包类型|
| 1012  | application err | 无效的消息主体|
| 1013  | application err | 	无效的序列化类型|
| 1020  | normal err | 未提供认证需要的uid和pwd|
| 1021  | normal err | 认证失败，可能原因是uid不存在或密码错误，或账号已被禁用|
| 2003  | normal err | 聊天室成员查询失败|
| 2001  | normal err | 聊天室加入失败|
| 2002  | normal err | 聊天室离开失败|


