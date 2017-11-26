package com.zhiyicx.votesdk.policy.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.PresenterListener;
import com.zhiyicx.votesdk.manage.VoteManager;
import com.zhiyicx.votesdk.policy.PresenterPolicy;
import com.zhiyicx.votesdk.policy.ZBCallBack;
import com.zhiyicx.votesdk.utils.NetUtils;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;

import java.util.List;
import java.util.Map;

/**
 * 类说明：
 * 主播端实现类
 * Created by lei on 2016/8/11.
 */
public class PresenterPolicyImpl implements PresenterPolicy {
    public static final String TAG = "PresenterPolicyImpl";

    private PresenterListener listener;
    private int mCid;
    //private String mUsid;
    private Gson gson = new Gson();
    private String createTime;

    @Override
    public void setListener(PresenterListener presenterListener) {
        listener = presenterListener;
    }

    /**
     * 查询最近一次的投票信息
     */
    @Override
    public void voteQueryLatest() {
        NetUtils.queryLastVoteInfo("", new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onQueryLastSuccess(info);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onQueryLastFailure(code, msg);
                }
            }
        });
    }

    /**
     * 查询某个投票最新信息
     *
     * @param vote_id
     */
    @Override
    public void voteQueryNewst(String vote_id) {
        NetUtils.queryNewestVoteInfo(vote_id, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onQueryNewestSuccess(info);
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onQueryNewestFailure(code, msg);
                }
            }
        });
    }

    /**
     * 发起一个投票
     *
     * @param title
     * @param time
     * @param options
     */
    @Override
    public void voteCreate(String title, int time, Map<String, String> options) {
        Log.v(TAG, "P---voteCreate..");
        NetUtils.createPoll(options, time, title, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                Log.v(TAG, "voteCreate onSuccess" + String.valueOf(listener));
                createTime = info.getCreate_time();
                if (listener != null) {
                    listener.onVoteCreateSuccess(info);
                }
                sendVoteCreateMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                Log.v(TAG, "P---voteCreate.onFailure." + msg);
                if (listener != null) {
                    listener.onVoteCreateFailure(code, msg);
                }
            }
        });
    }

    /**
     * 发起投票
     *
     * @param title
     * @param time
     * @param options
     */
    @Override
    public void voteCreate(String title, int time, List<String> options) {

        NetUtils.createPoll(options, time, title, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                Log.v(TAG, "voteCreate onSuccess" + String.valueOf(listener));
                createTime = info.getCreate_time();
                if (listener != null) {
                    listener.onVoteCreateSuccess(info);
                }
                sendVoteCreateMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                Log.v(TAG, "P---voteCreate onFailure");
                if (listener != null) {
                    listener.onVoteCreateFailure(code, msg);
                }
            }
        });
    }


    /**
     * 临时关闭投票
     *
     * @param vote_id
     */
    @Override
    public void votePause(String vote_id) {
        NetUtils.votePause(vote_id, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onVotePauseSuccess(info);
                }
                sendVotePauseMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onVotePauseFailure(code, msg);
                }
            }
        });
    }

    /**
     * 结束投票
     *
     * @param vote_id
     */
    @Override
    public void voteStop(String vote_id) {
        NetUtils.voteStop(vote_id, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onVoteStopSuccess(info);
                }
                sendVoteStopMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onVoteStopFailure(code, msg);
                }
            }
        });

    }

    /**
     * 恢复投票
     *
     * @param vote_id
     */
    @Override
    public void voteRestart(String vote_id) {
        NetUtils.voteRestart(vote_id, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onVoteTimeResetSuccess(info);
                }
                sendVoteRestartMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onVoteRestartFailure(code, msg);
                }
            }
        });
    }

    /**
     * 重置投票时间
     *
     * @param vote_id
     * @param time
     */
    @Override
    public void resetVoteTime(String vote_id, long time) {
        NetUtils.updateVoteTime(vote_id, time, new ZBCallBack(new VoteInfo()) {
            @Override
            public void onSuccess(VoteInfo info) {
                if (listener != null) {
                    listener.onVoteTimeResetSuccess(info);
                }
                sendVoteResetTimeMessage(info);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (listener != null) {
                    listener.onVoteTimeResetFailure(code, msg);
                }
            }
        });
    }

    @Override
    public void receiveTimeOutMessage(Message msg) {
        ZBStreamingClient.getInstance().sendMessage(msg);
    }

    private VoteInfo info = new VoteInfo();

    /**
     * 收到观众段投票的信息
     *
     * @param msg
     */
    @Override
    public void receiveVoteMessage(Message msg) {
        Log.v(TAG, "P--- receiveVoteMessage " + String.valueOf(msg));
        if (info == null) {
            info = new VoteInfo();
        }
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
        }
    }


    /**
     * 发起投票成功后，发送创建投票消息观众端
     */
    @Override
    public void sendVoteCreateMessage(VoteInfo info) {
        Log.v(TAG, "P:--- sendVoteCreateMessage" + String.valueOf(info));
        sendMessage(VoteManager.MESSAGE_VOTE_CREATE, info);
    }

    /**
     * 发送暂停投票消息给观众端
     *
     * @param info
     */
    @Override
    public void sendVotePauseMessage(VoteInfo info) {
        Log.v(TAG, "P:--- sendVotePauseMessage" + String.valueOf(info));
        sendMessage(VoteManager.MESSAGE_VOTE_PAUSE, info);
    }

    /**
     * 发送恢复投票消息给观众端
     *
     * @param info
     */
    @Override
    public void sendVoteRestartMessage(VoteInfo info) {
        Log.v(TAG, "P: sendVoteRestartMessage" + String.valueOf(info));
        sendMessage(VoteManager.MESSAGE_VOTE_RESTART, info);
    }

    /**
     * 发送结束投票消息给观众端
     *
     * @param info
     */
    @Override
    public void sendVoteStopMessage(VoteInfo info) {
        Log.v(TAG, "P:--- sendVoteStopMessage" + String.valueOf(info));
        sendMessage(VoteManager.MESSAGE_VOTE_STOP, info);
    }

    /**
     * 发送重设置投票时间给观众端
     *
     * @param info
     */
    @Override
    public void sendVoteResetTimeMessage(VoteInfo info) {
        Log.v(TAG, "P:--- sendVoteResetTimeMessage" + String.valueOf(info));
        sendMessage(VoteManager.MESSAGE_VOTE_RESET, info);
    }


    @Override
    public void setCid(int cid) {
        this.mCid = cid;
    }

    /**
     * 具体发送消息
     *
     * @param type
     * @param info
     */
    private void sendMessage(int type, VoteInfo info) {
        Map<String, Object> map = gson.fromJson(gson.toJson(info), new TypeToken<Map<String, Object>>() {
        }.getType());
        ZBStreamingClient.getInstance().sendMessage(true, map, type);
    }

}
