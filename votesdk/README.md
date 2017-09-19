# 在线投票场景

功能：

主播端： 发起投票、结束投票、临时关闭投票、恢复投票主播端发起投票、结束投票、临时关闭投票、恢复投票
观众端:  投票
投票未结束：显示投票的进度及倒计时的时间
投票结束：显示投票结果
###业务流程：
主播端发起操作成功，会向聊天室其他观众发送IM信息（操作的一个结果），通知他们做相应处理；
观众端投票成功，发送投票的结果，和投票详细给主播和聊天室其他观众，作相应处理
## 快速集成
在智播SDK的基础上
### 1.将jar包放到你的lib下面
主要是通过`VoteManger`做一些方法处理的网络请求（发起投票、投票，具体看接口文档）
### 2.将资源文件（res下）放到你的主目录下面
视图层相关的资源文件，如果你要自定义界面，那么就不需要用提供的视图层（Voteview、popupwindow），就不需要做这一步了


## sdk介绍：
主要使用`VoteManger`作为统一接口，供外面调用去发起网络请求，用了三方Gson
管理类`VoteManger`
```java
mVoteManager = VoteManager.newBuilder().cid(mCid).userType(VoteManager.TYPE_AUDIENCE).presenterUsid(usid).setListener(audienceListener).build();
```
### VoteBuilder来构建参数
参数说明：
| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| userType(type) | true | int | 用户类型 |主播：`VoteManger.TYPE_PRESENTER` ；  观众端：`VoteManager.TYPE_AUDIENCE`|
| cid(cid) | true | String | 聊天室id|  |
| presenterUsid(usid) | false | String |主播usid 观众端查询最近一次投票必传 |观众端查询最近一次投票必传（默认当前登录用户usid）  |
| setListener  | true | interface | 回调| 观众端：AudienceListener 主播端：PresenterListener |



##接口文档
### 通过VoteManager调用方法，主播端与观众端公用的方法
* **queryLatestVote** 查询某主播最近一次发起的投票
* **queryNewstVote** 查询某个投票最新的结果
* **handleMessage**	处理收到别人发送的消息，在外部接收消息中调用
* **handeTimeOutMessage** 处理自己发送消息失败，在外部发送失败中调用

### 1. 主播端

#### 1.1 发起投票
方法:`voteCreate`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| title | false | String | 投票标题|
| time | false | int | 投票时长(单位：min) default：0 永久有效|  |
| options | true | Map,List | 投票选项 | Map包含选项名称和选项内容，list只有选项内容，名称由后台返回 |

#### 1.2 临时关闭投票
方法:`votePause`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| vote_id | true | String | 某一轮投票唯一标识 |

#### 1.3 主播端永久结束投票

方法:`voteStop`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| vote_id | true | String | 某一轮投票唯一标识 |

#### 1.4 主播端恢复关闭的投票

方法:`voteRestart`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| vote_id | true | String | 某一轮投票唯一标识 |

#### 1.7 主播端重设置投票时长

方法:`resetVoteTime`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| time | false | String | 投票时长，默认：0 永久有效|

### 2. 观众端

#### 1.1 投票

方法:`sendPoll`

##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| vote_id | true | String | 某轮投票唯一标识|发起投票成功后，服务器返回，或通过查询最近一次投票获得
| optionKey | true | String | 所投选项 optionkey|  |
| count | true | long| 投票数量（金币数量） |成功后，服务器会扣除 |
| gift_code | true | String|投票时所选礼物对应code  | |

### 回调：OnPresenterListener/OnAudienceListener
是其PresenterListener/AudienceListener实现抽象类
相同的回调方法
`onSuccess(VoteInfo voteInfo)` 访问成功，status=“00000”，返回某一个投票的所有信息
`onFailure(String code,String message)` 访问成功，返回错误代码和提示信息，当返回("-1","error")代表访问出错没有访问到服务器
#### 主播端回调 `OnPresenterListener`
`onVoteCreateSuccess`   发起投票成功
`onVoteCreateFailure`   发起投票失败
`onVotePauseSuccess`    临时关闭投票成功
`onVotePauseFailure`    临时关闭投票失败
`onVoteStopSuccess`     永久结束投票成功
`onVoteStopFailure`     永久结束投票失败
`onVoteRestartSuccess`  恢复投票成功
`onVoteRestartFailure`  恢复投票失败
`onVoteTimeResetSuccess`重新设置投票时长成功
`onVoteTimeResetFailure`重新设置投票时长失败
`onQueryLastSuccess`    查询最近一次投票成功
`onQueryLastFailure`    查询最近一次投票失败
`onQueryNewestSuccess`  查询某个投票最新情况成功
`onQueryNewestFailure`  查询某个投票最新情况失败
`receiveAudienceVote`   收到观众端投票信息
#### 观众端回调
`onQueryLastSuccess`            查询某主播最近一次投票信息成功
`onQueryLastFailure`            查询某主播最近一次投票信息失败
`onQueryNewestSuccess`          查询某主播某次投票最新信息成功
`onQueryNewestFailure`          查询某主播某次投票最新信息失败
`onVoteSuccess`                 投票成功
`onVoteFailure`                 投票失败
`receivePresenterMessage`       收到主播端发送消息
`receivePresenterVoteCreate`    收到主播端发起投票消息
`receivePresenterVotePause`     收到主播端临时关闭投票消息
`receivePresenterVoteStop`      收到主播端永久结束投票消息
`receivePresenterVoteRestart`   收到主播端恢复投票消息
`receivePresenterVoteResetTime` 收到主播端重新设置投票时长消息
`receiveAudienceVote`           收到其他观众的投票信息



### 示例代码：
主播端查询最近一次投票信息：
```java
VoteManager manager = VoteManager.newBuilder().with(context).userType(VoteManager.TYPE_PRESENTER).
		setListener(new OnPresenterListener{
				@Override
            public void onSuccess(VoteInfo voteInfo) {
         }

         @Override
          public void onFailure(String code, String message) {
         }).build();

manager.queryLatestVote();
```
观众端投票
```java
VoteManager manager = VoteManager.newBuilder().with(context).userType(VoteManager.TYPE_AUDIENCE).
setAudienceListener(OnAudienceListener).cid("").build();
manager.sendPoll(vote_id,option_key,gift_code,goldCount);
```
具体实现是在AudiencePolicyImpl/PresenterPolicyImpl 里面



## 视图层
### VoteCreatePopupWindow
发起投票弹窗，实现了发起投票功能
##### 所需参数说明:

| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| optionKeys | true | List | 设置选项optionKey |每一个选项对应一个选项key eg：A 张三
| pollOptMaxNum | true | int |设置最多有多少项答案（不能大于OptionKeys.size()） 某轮投票唯一标识|发起投票成功后，服务器返回，或通过查询最近一次投票获得
| times | true | List | 设置投票时间（size = 5）固定为5个|  |

回调：
`setListener(PresenterListener listener)` //创建投票的回调
		示例：
```java
	VoteCreatePopupWindow voteCreatePop = new VoteCreatePopupWindow(getActivity());
	voteCreatePop.setPresenterListener(new OnPresenterListener() {
		   @Override
		public void onSuccess(VoteInfo s) {//发起投票成功后，后台返回的投票信息VoteInfo
			initVoteView(s);
		}

		@Override
		public void onFailure(String code, String message) {

		}
     }
```

### VotePollPopupWindow
投票弹窗，需要自己处理选择后的操作
##### 所需参数说明:
| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| maxNum | true | List | 设置选项optionKey为默认ABC..时，总共多少项 |optionkey为默认ABC..时，当前选项的个数
| optionKeys | true | int |设置最多有多少项答案（不能大于OptionKeys.size()）|
| vote_id | true | String | 投票唯一标识|
| voteInfo | true | VoteInfo |某一轮投票信息，包含了vote_id/optionKeys |


回调
	`OnVoteOptionListener`
					`vote(String optionKey)`;//点击选项opionKey


#### 示例：
```java
		VotePollPopupWindow mVoteOptionPop = new VotePollPopupWindow(getActivity());
		mVoteOptionPop.setVoteInfo(voteInfo);
		mVoteOptionPop.setOnVoteListener(new VotePollPopupWindow.OnVoteOptionListener() {
			@Override
			public void vote(String optionKey) {

			}
		});

```
### VoteOnView
投票进行中的一个视图，里面通过轮询的方式去刷新投票的进度，当投票结束会有一个回调，扩展：投票状态发生改变的回调：如：暂停、时间重置等
##### 所需参数说明:
| 参数| 必选 | 类型 | 说明 | 备注 |
|:-------------:|:-------------:|:-------------:|:-------------:|:-------------:|
| VoteInfo | true | VoteInfo | 	投票信息


`OnVoteStatusChangedListener`	投票状态发生改变的回调（结束/暂停..）
`OnVoteOptionClickListener`		点击某一个选项item的回调
对外方法
`refreshOptionItemValue(VoteInfo info)` 可以根据传入的model来刷新控件值

#### 示例代码：
```java
		VoteOnView onView = new VoteOnView(getActivity());
		onView.setVoteInfo(info);
		nView.setUserType(type);
		onView.setOnVoteStatusChangedListener(new VoteOnView.OnVoteStatusChangedListener() {
			@Override
			public void end(VoteInfo info) {//投票结束
				initVoteView(info);
			}
		});
		onView.setOnVoteOptionClickListener(new VoteOnView.OnVoteOptionClickListener() {
			@Override
			public void click(VoteOption option, VoteInfo voteInfo) {//点击某一个选项item
				if (null != mVoteOptionPop)
					mVoteOptionPop.show(mRootView);
			}
		});

		AutoLinearLayout.LayoutParams params = new AutoLinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.leftMargin = UiUtils.dip2px(10);
		onView.setLayoutParams(params);
		mVoteShowContainerRl.addView(onView);
```

### VoteEndView
投票结束结果的视图，主要就是显示，
##### 所需参数说明:

VoteInfo voteinfo  必传

### VoteOptEdt
发起投票时编辑选项的view

`setOptionText(String optStr)` 设置选项内容
`setOptionHint(String optStr)` 设置选项hint
`getOptionText()`					得到选项内容
`setOptSortText(String optSort)`  //设置选项序列号
`getOptSortText()`		//得到选项序列号

### VoteProgress
进度条上能显示文字的view
使用方法：
`setProgressStr()`  --需在setProgress前调用,才会显示文字
`setProgress()`






































