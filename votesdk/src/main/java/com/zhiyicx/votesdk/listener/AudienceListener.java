package com.zhiyicx.votesdk.listener;


import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.base.BaseListener;

/**
 * Created by lei on 2016/8/11.
 */
public interface AudienceListener extends BaseListener {


    /**
     * 投票成功
     */
    public void onVoteSuccess(VoteInfo voteInfo,OptionDetail detail);

    /**
     * 投票失败
     */
    public void onVoteFailure(String code, String message);

    /**
     * 收到主播对投票的一些改变的信息
     *
     * @param msg
     */
    void receivePresenterMessage(Message msg, VoteInfo info);

    /**
     * 收到主播创建投票的消息
     *
     * @param msg
     * @param info
     */
    void receivePresenterVoteCreate(Message msg, VoteInfo info);

    /**
     * 收到主播暂停投票的消息
     *
     * @param msg
     * @param info
     */
    void receivePresenterVotePause(Message msg, VoteInfo info);

    /**
     * 收到主播结束投票的消息
     *
     * @param msg
     * @param info
     */
    void receivePresenterVoteStop(Message msg, VoteInfo info);

    /**
     * 收到主播恢复投票的消息
     *
     * @param msg
     * @param info
     */
    void receivePresenterVoteRestart(Message msg, VoteInfo info);

    /**
     * 收到主播重设投票时段的消息
     *
     * @param msg
     * @param info
     */
    void receivePresenterVoteResetTime(Message msg, VoteInfo info);



}
