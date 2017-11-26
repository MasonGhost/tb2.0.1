package com.zhiyicx.zhibolibrary.ui.view;

import android.app.Activity;
import android.content.Context;

import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;

/**
 * Created by jess on 16/5/11.
 *
 * @param <T>
 */
public interface PublishCoreView<T> extends BaseView {
    /**
     * 获得发布页面核心布局
     *
     * @return
     */
    ZBLBaseFragment getCoreFragment();

    /**
     * 隐藏软键盘
     */
    void hidekeyboard();

    /**
     * 获取用户信息
     *
     * @return
     */
    ZBApiImInfo getImInfo();

    /**
     * 获得用户数据
     *
     * @return
     */
    SearchResult getData();

    UserInfo getUser();

    /**
     * 接收到文本消息
     *
     * @param message
     */
    void recievedTextMessage(Message message);

    /**
     * 收到投票的消息
     * @param message
     */
    void receivedVoteMessage(Message message);

    /**
     * 发送消息超时
     * @param message
     */
    void receivedTimeOutMessage(Message message);

    /**
     * 接收到礼物消息
     *
     * @param message
     */
    void recievedGiftMessage(Message message);

    /**
     * 接收到赞消息
     *
     * @param message
     */
    void recievedZanMessage(Message message);

    /**
     * 接收关注消息
     *
     * @param message
     */
    void recievedFllowMessage(Message message);


    /**
     * 处理分发消息
     *
     * @param message
     */
    void handleMessage(Message message);

    /**
     * 把信息保存起来，并且显示到列表
     *
     * @param userMessage
     */
    void saveAndAddChat(BaseJson<T[]> userMessage, Message msg);

    /**
     * 添加自己输入的信息到聊天列表
     */
    void addSelfChat(boolean isSuccess, Message message);

    /**
     * 添加自己输入的信息到聊天列表
     */
    void addChat(UserMessage userMessage);

    /**
     * 被点击的列表头像
     *
     * @param postion
     */
    void getClickPresenterInfo(int postion);

    /**
     * 刷新排行榜
     *
     * @param apiList
     * @param isMore
     */
    void giftRankrefresh(BaseJson<SearchResult[]> apiList, boolean isMore);


    /**
     * 加入了房间
     */
    void joinedChatroom(ChatRoomContainer chatRoomContainer);

    /**
     * 用户离开了房间
     */
    void leavedChatroom(ChatRoomContainer chatRoomContainer);

    /**
     * 主播离开了房间
     */
    void convrEnd(int cid);

    /**
     * 获取到了房间的数量
     */
    void getChatroomMc(ChatRoomDataCount chatRoomDataCount);

    /**
     * 刷新金币数
     */
    void updatedGold();

    /**
     * 发送赞或者礼物成功返回数据
     */
    void sendZanOrgift(int type);

    /**
     * 更新主播信息
     *
     * @param userInfo
     */
    void updatePresenterInfo(UserInfo userInfo);

    /**
     * 是否可点击
     *
     * @param isClickable
     * @param type
     */
    void setClickable(boolean isClickable, int type);

    /**
     * 礼物配置缓存
     *
     * @param gift
     */
    void saveGiftConfigCach(ZBGift gift);

    /**
     * 永久禁言
     */
    void disableSendMsgEver();

    /**
     * 时段禁言
     */
    void disableSendMsgSomeTime(long time);

    /**
     * 进入页面时的禁言判断
     *
     * @param isBanneded
     */
    void setbanneded(boolean isBanneded, long gag);

    /**
     * 获取用于在结束页面显示金币赞个数的实体
     */
    ZBEndStreamJson getEndStream();

    /**
     * 人数统计
     *
     * @return
     */
    ChatRoomDataCount getChatRoomDataCount();

    /**
     * 隐藏禁言对话框
     */
    void dimissImDisablePop();

    boolean isPresenter();

    UserInfo getPresenterInfo();

    Activity getCurrentActivity();
}
