package com.zhiyicx.votesdk.policy.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.AudienceListener;
import com.zhiyicx.votesdk.manage.VoteManager;
import com.zhiyicx.votesdk.policy.AudiencePolicy;
import com.zhiyicx.votesdk.policy.ZBCallBack;
import com.zhiyicx.votesdk.utils.NetUtils;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;

import java.util.Map;

/**
 * 类说明：
 * 观众端实现类
 * Created by lei on 2016/8/11.
 */
public class AudiencePolicyImpl implements AudiencePolicy {
    public static final String TAG = "AudiencePolicyImpl";
    private int mCid;
    //private String mUsid;
    private AudienceListener listener;
    private Gson gson = new Gson();
    private String createTime;


    @Override
    public void setListener(AudienceListener audienceListener) {
        listener = audienceListener;
    }

    /**
     * 投票
     *
     * @param vote_id    投票活动id
     * @param option_key //选项key'
     * @param gift_code
     * @param gold
     */
    @Override
    public void votePoll(final String vote_id, final String option_key, final String gift_code, final int gold) {
        NetUtils.sendVote(vote_id, option_key, gold, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                OptionDetail detail = new OptionDetail(option_key, gift_code, gold);
                info.setVoteDetail(detail);
                if (listener != null) {
                    listener.onVoteSuccess(info, detail);
                }
                sendVoteMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null)
                    listener.onVoteFailure(code, msg);
            }
        });
    }


    /**
     * 查询某主播最近一次的投票信息
     *
     * @param usid
     */
    @Override
    public void voteQueryLatest(String usid) {
        NetUtils.queryLastVoteInfo(usid, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                createTime = info.getCreate_time();
                if (listener != null)
                    listener.onQueryLastSuccess(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null)
                    listener.onQueryLastFailure(code, msg);
            }
        });
    }

    /**
     * 查询某投票最新的情况信息
     *
     * @param voteId
     */
    @Override
    public void voteQueryNewst(String voteId) {
        NetUtils.queryNewestVoteInfo(voteId, new ZBCallBack(new VoteInfo()) {


            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null)
                    listener.onQueryNewestSuccess(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null)
                    listener.onQueryNewestFailure(code, msg);
            }
        });
    }


    /**
     * 发送投票的信息
     */
    @Override
    public void sendVoteMessage(VoteInfo info) {
        Map<String, Object> map = gson.fromJson(gson.toJson(info), new TypeToken<Map<String, Object>>() {
        }.getType());
        ZBPlayClient.getInstance().sendMessage(true, map, VoteManager.MESSAGE_VOTE_RECEIVED);
        Log.v(TAG, "A--- sendVoteMessage " + String.valueOf(info));
    }


    private VoteInfo info = new VoteInfo();

    /**
     * 收到观众端投票的信息
     *
     * 判断是否同一轮投票且是最新的投票信息
     * @param msg
     */
    @Override
    public void receiveAudienceMessage(Message msg) {
        if (info == null) info = new VoteInfo();
        if (listener != null) {
            VoteInfo receive = gson.fromJson(gson.toJson(msg.ext.custom), VoteInfo.class);
            if (TextUtils.isEmpty(createTime)) {
                listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
                info = receive;
            } else {
                if (createTime.equals(receive.getCreate_time())) {
                    if (receive.getBallot() > info.getBallot()) {
                        listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
                        info = receive;
                    }
                } else {
                    listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
                    info = receive;
                }

            }
//            if (receive.getBallot() > info.getBallot()) {
//                listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
//                info = receive;
//            }
//            if (receive.getCreate_time().equals(info.getCreate_time())) {
//                if (receive.getBallot() > info.getBallot()) {
//                    listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
//                    info = receive;
//                }
//            } else {
//                listener.receiveAudienceVote(msg, receive, receive.getVoteDetail());
//                info = receive;
//            }
        }
    }

    /**
     * 收到主播端发送信息
     *
     * @param msg
     */
    @Override
    public void receivePresenterMessage(Message msg) {
        Log.v(TAG, "A--- receivePresenterMessage " + String.valueOf(msg));
        if (listener != null) {
            VoteInfo info = gson.fromJson(gson.toJson(msg.ext.custom), VoteInfo.class);
            listener.receivePresenterMessage(msg, info);
            //0: 结束 1：投票中 2：投票暂停
            int typeId = msg.getExt().getCustomID();
            switch (typeId) {
                case VoteManager.MESSAGE_VOTE_CREATE:
                    createTime = info.getCreate_time();
                    listener.receivePresenterVoteCreate(msg, info);
                    break;
                case VoteManager.MESSAGE_VOTE_PAUSE:
                    listener.receivePresenterVotePause(msg, info);
                    break;
                case VoteManager.MESSAGE_VOTE_RESTART:
                    createTime = info.getCreate_time();
                    listener.receivePresenterVoteRestart(msg, info);
                    break;
                case VoteManager.MESSAGE_VOTE_STOP:
                    listener.receivePresenterVoteStop(msg, info);
                    break;
                case VoteManager.MESSAGE_VOTE_RESET:
                    listener.receivePresenterVoteResetTime(msg, info);
                    break;
            }

        }
    }

    /**
     * 发送消息超时
     *
     * @param msg
     */
    @Override
    public void receiveTimeOutMessage(Message msg) {
        ZBPlayClient.getInstance().sendMessage(msg);
    }

    @Override
    public void setCid(int cid) {
        this.mCid = cid;
    }

//    @Override
//    public void setUsid(String uid) {
//        this.mUsid = uid;
//    }

}
