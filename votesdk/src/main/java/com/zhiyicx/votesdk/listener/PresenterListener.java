package com.zhiyicx.votesdk.listener;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.entity.VoteOption;
import com.zhiyicx.votesdk.listener.base.BaseListener;

/**
 * Created by lei on 2016/8/11.
 */
public interface PresenterListener extends BaseListener {


    /**
     * 主播发起投票
     */
    void onVoteCreateSuccess(VoteInfo voteInfo);

    void onVoteCreateFailure(String code, String message);

    /**
     * 主播暂停投票
     */
    void onVotePauseSuccess(VoteInfo info);

    void onVotePauseFailure(String code, String message);

    /**
     * 主播结束投票
     */
    void onVoteStopSuccess(VoteInfo info);

    void onVoteStopFailure(String code, String message);

    /**
     * 主播恢复投票
     */
    void onVoteRestartSuccess(VoteInfo info);

    void onVoteRestartFailure(String code, String message);

    /**
     * 主播重置投票时间
     */
    void onVoteTimeResetSuccess(VoteInfo info);

    void onVoteTimeResetFailure(String code, String message);




}
