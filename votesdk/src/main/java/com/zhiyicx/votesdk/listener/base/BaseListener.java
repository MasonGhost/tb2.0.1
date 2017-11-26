package com.zhiyicx.votesdk.listener.base;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;

/**
 * Created by lei on 2016/8/18.
 */
public interface BaseListener {

    /**
     * 查询最近一次投票信息成功
     *
     * @param voteInfo
     */
    void onQueryLastSuccess(VoteInfo voteInfo);

    /**
     * 查询最近一次投票信息失败
     *
     * @param code
     * @param msg
     */
    void onQueryLastFailure(String code, String msg);

    /**
     * 查询某次投票活动最新的信息成功
     *
     * @param voteInfo
     */
    void onQueryNewestSuccess(VoteInfo voteInfo);

    /**
     * * 查询某次投票活动最新的信息失败
     *
     * @param code
     * @param msg
     */
    void onQueryNewestFailure(String code, String msg);

    /**
     * 收到观众端投票信息
     *
     * @param msg
     * @param voteInfo
     */
    void receiveAudienceVote(Message msg, VoteInfo voteInfo,OptionDetail detail);



}
