package com.zhiyicx.votesdk.listener;

import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;

/**
 * Created by lei on 2016/8/26.
 */
public abstract class OnPresenterListener implements PresenterListener {

    @Override
    public void onVoteCreateSuccess(VoteInfo voteInfo) {

    }

    @Override
    public void onVoteCreateFailure(String code, String message) {

    }

    @Override
    public void onVotePauseSuccess(VoteInfo info) {

    }

    @Override
    public void onVotePauseFailure(String code, String message) {

    }

    @Override
    public void onVoteStopSuccess(VoteInfo info) {

    }

    @Override
    public void onVoteStopFailure(String code, String message) {

    }

    @Override
    public void onVoteRestartSuccess(VoteInfo info) {

    }

    @Override
    public void onVoteRestartFailure(String code, String message) {

    }

    @Override
    public void onVoteTimeResetSuccess(VoteInfo info) {

    }

    @Override
    public void onVoteTimeResetFailure(String code, String message) {

    }

    @Override
    public void onQueryLastSuccess(VoteInfo voteInfo) {

    }

    @Override
    public void onQueryLastFailure(String code, String msg) {

    }

    @Override
    public void onQueryNewestSuccess(VoteInfo voteInfo) {

    }

    @Override
    public void onQueryNewestFailure(String code, String msg) {

    }

    @Override
    public void receiveAudienceVote(Message message, VoteInfo info,OptionDetail detail) {

    }


}
