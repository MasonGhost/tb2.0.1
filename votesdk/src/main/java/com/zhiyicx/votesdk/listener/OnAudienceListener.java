package com.zhiyicx.votesdk.listener;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;

/**
 * Created by lei on 2016/8/26.
 */
public abstract class OnAudienceListener implements AudienceListener {


    @Override
    public void onVoteSuccess(VoteInfo voteInfo,OptionDetail detail) {

    }

    @Override
    public void onVoteFailure(String code, String message) {

    }

    @Override
    public void receivePresenterMessage(Message msg, VoteInfo info) {

    }

    @Override
    public void receivePresenterVoteCreate(Message msg, VoteInfo info) {

    }

    @Override
    public void receivePresenterVotePause(Message msg, VoteInfo info) {

    }

    @Override
    public void receivePresenterVoteStop(Message msg, VoteInfo info) {

    }

    @Override
    public void receivePresenterVoteRestart(Message msg, VoteInfo info) {

    }

    @Override
    public void receivePresenterVoteResetTime(Message msg, VoteInfo info) {

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
    public void receiveAudienceVote(Message msg, VoteInfo infom,OptionDetail detail) {

    }



}
