package com.zhiyicx.votesdk.manage;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.votesdk.listener.AudienceListener;
import com.zhiyicx.votesdk.listener.PresenterListener;
import com.zhiyicx.votesdk.policy.AudiencePolicy;
import com.zhiyicx.votesdk.policy.PresenterPolicy;
import com.zhiyicx.votesdk.policy.impl.AudiencePolicyImpl;
import com.zhiyicx.votesdk.policy.impl.PresenterPolicyImpl;

import java.util.List;
import java.util.Map;

/**
 * 类说明：
 * 管理类
 * Created by lei on 2016/8/18.
 */
public class VoteManager {
    public static final String TAG = "VoteManager";
    public static final int TYPE_PRESENTER = 10;
    public static final int TYPE_AUDIENCE = 11;
    public static final int TYPE_CUSTOME = MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE;
    public static final int MESSAGE_VOTE_CREATE = 1201;
    public static final int MESSAGE_VOTE_RECEIVED = 1202;
    public static final int MESSAGE_VOTE_PAUSE = 1203;
    public static final int MESSAGE_VOTE_STOP = 1204;
    public static final int MESSAGE_VOTE_RESTART = 1205;
    public static final int MESSAGE_VOTE_RESET = 1206;


    private Context mContext;
    private PresenterPolicy mPresenterPolicy;
    private AudiencePolicy mAudiencePolicy;


    private String mPresenterUsid;//主播usid
    private int type;

    private VoteManager(VoteBuilder builder) {
        this.mContext = builder.context;
        this.mPresenterPolicy = builder.presenterPolicy;
        this.mAudiencePolicy = builder.audiencePolicy;
        this.mPresenterUsid = builder.presenterUsid;
        this.type = builder.type;
    }


    public static VoteBuilder newBuilder() {
        return new VoteBuilder();
    }


    /**
     * 创建投票
     */
    public void createVote(String title, Map<String, String> options, int time) {
        if (null == options || options.size() < 2) {
            throw new NullPointerException("the options can't be null and option.size should be >= 2");
        }
        if (mPresenterPolicy != null && null != options) {
            mPresenterPolicy.voteCreate(title, time, options);
        }
    }

    /**
     * 创建投票
     */
    public void createVote(String title, List<String> options, int time) {
        if (null == options || options.size() < 2) {
            throw new NullPointerException("the options can't be null and option.size should be >= 2");
        }
        if (mPresenterPolicy != null && null != options) {
            mPresenterPolicy.voteCreate(title, time, options);
        }
    }

    /**
     * 查询最近一次的投票场景信息
     */
    public void queryLatestVote() {
        if (TYPE_PRESENTER == type && mPresenterPolicy != null) {
            mPresenterPolicy.voteQueryLatest();
        } else if (TYPE_AUDIENCE == type && mAudiencePolicy != null) {
            mAudiencePolicy.voteQueryLatest(mPresenterUsid);
        }
    }

    /**
     * 获得投票进行时最新数据
     */
    public void queryNewstVote(String vote_id) {
        if (TextUtils.isEmpty(vote_id)) {
            throw new NullPointerException("the vote_id can't be null");
        }
        if (TYPE_PRESENTER == type && mPresenterPolicy != null) {
            mPresenterPolicy.voteQueryNewst(vote_id);
        } else if (TYPE_AUDIENCE == type && mAudiencePolicy != null) {
            mAudiencePolicy.voteQueryNewst(vote_id);
        }
    }


    /**
     * 临时暂停投票
     */
    public void votePause(String vote_id) {
        if (TextUtils.isEmpty(vote_id)) {
            throw new NullPointerException("the vote_id can't be null");
        }
        mPresenterPolicy.votePause(vote_id);
    }

    /**
     * 结束投票
     */
    public void voteStop(String vote_id) {
        if (TextUtils.isEmpty(vote_id)) {
            throw new NullPointerException("the vote_id can't be null");
        }
        if (mPresenterPolicy != null) {
            mPresenterPolicy.voteStop(vote_id);
        }
    }

    /**
     * 恢复暂停的投票
     */
    public void voteRestart(String vote_id) {
        if (TextUtils.isEmpty(vote_id)) {
            throw new NullPointerException("the vote_id can't be null");
        }
        if (mPresenterPolicy != null) {
            mPresenterPolicy.voteRestart(vote_id);
        }
    }

    /**
     * 重新设置投票时间
     */
    public void resetVoteTime(String vote_id, int time) {
        if (TextUtils.isEmpty(vote_id)) {
            throw new NullPointerException("the vote_id can't be null");
        }
        if (time < 0) {
            throw new IllegalArgumentException("the time must be >= 0");
        }
        if (mPresenterPolicy != null) {
            mPresenterPolicy.resetVoteTime(vote_id, time);
        }
    }

    /**
     * 观众开始投票
     */
    public void sendPoll(String vote_id, String optionKey, String gift_code, int goldCount) {
        if (TextUtils.isEmpty(vote_id) || TextUtils.isEmpty(optionKey) || goldCount <= 0) {
            throw new IllegalArgumentException("the vote_id or optionKey can't be null,or the votecount can't be <=0");
        }
        if (mAudiencePolicy != null) {
            mAudiencePolicy.votePoll(vote_id, optionKey, gift_code, goldCount);
        }
    }

    //收到消息
    public void handleMessage(Message msg) {
        if (TYPE_CUSTOME == msg.getType()) {
            switch (msg.ext.customID) {
                case MESSAGE_VOTE_CREATE:
                case MESSAGE_VOTE_PAUSE:
                case MESSAGE_VOTE_RESET:
                case MESSAGE_VOTE_STOP:
                case MESSAGE_VOTE_RESTART:
                    if (mAudiencePolicy != null) {
                        mAudiencePolicy.receivePresenterMessage(msg);
                    }
                    break;
                case MESSAGE_VOTE_RECEIVED:
                    if (mAudiencePolicy != null) {
                        mAudiencePolicy.receiveAudienceMessage(msg);
                    }
                    if (mPresenterPolicy != null) {
                        mPresenterPolicy.receiveVoteMessage(msg);
                    }
                    break;
            }
        }
    }

    /**
     * 发送消息超时
     *
     * @param msg
     */
    public void handeTimeOutMessage(Message msg) {
        Log.v(TAG, "handeTimeOutMessage  Message:  " + msg);
        if (TYPE_CUSTOME == msg.getType()) {
            switch (msg.ext.customID) {
                case MESSAGE_VOTE_CREATE:
                case MESSAGE_VOTE_PAUSE:
                case MESSAGE_VOTE_RESET:
                case MESSAGE_VOTE_STOP:
                case MESSAGE_VOTE_RESTART:
                    if (mPresenterPolicy != null) {
                        mPresenterPolicy.receiveTimeOutMessage(msg);
                    }
                    break;
                case MESSAGE_VOTE_RECEIVED:
                    if (mAudiencePolicy != null) {
                        mAudiencePolicy.receiveTimeOutMessage(msg);
                    }
                    break;
            }
        }
    }


    //**************************************************

    public static class VoteBuilder {

        private Context context;
        private AudienceListener audienceListener;
        private PresenterListener presenterListener;
        private int type;
        private String presenterUsid;
        private PresenterPolicy presenterPolicy;
        private AudiencePolicy audiencePolicy;
        private int cid;//主播cid

        public VoteBuilder with(Context context) {
            this.context = context;
            return this;
        }

        public VoteBuilder userType(int type) {
            this.type = type;
            return this;
        }


        public VoteBuilder presenterUsid(String usid) {
            this.presenterUsid = usid;
            return this;
        }

        public VoteBuilder cid(int cid) {
            this.cid = cid;
            return this;
        }

        public VoteBuilder setListener(PresenterListener listener) {
            presenterListener = listener;
            return this;
        }

        public VoteBuilder setListener(AudienceListener listener) {
            audienceListener = listener;
            return this;
        }


        public VoteManager build() {
//            if (context == null)
//                throw new NullPointerException("context can't be null");
            if (type != TYPE_AUDIENCE && type != TYPE_PRESENTER) {
                throw new IllegalArgumentException("type is not correct,it should be TYPE_PRESENTER or TYPE_AUDIENCE");
            }
            if (cid <= 0) {
                throw new IllegalArgumentException("cid can't be null");
            }
            if (type == TYPE_PRESENTER) {
                presenterPolicy = new PresenterPolicyImpl();
                presenterPolicy.setCid(cid);
                presenterPolicy.setListener(presenterListener);
            } else if (type == TYPE_AUDIENCE) {
                if (TextUtils.isEmpty(presenterUsid)) {
                    throw new NullPointerException("the presenterUsid can't be null");
                }
                audiencePolicy = new AudiencePolicyImpl();
                audiencePolicy.setCid(cid);
                audiencePolicy.setListener(audienceListener);
            }
            return new VoteManager(this);
        }

    }


}
