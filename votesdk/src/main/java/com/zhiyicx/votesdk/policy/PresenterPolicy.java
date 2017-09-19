package com.zhiyicx.votesdk.policy;

import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.OnPresenterListener;
import com.zhiyicx.votesdk.listener.PresenterListener;

import java.util.List;
import java.util.Map;

/**
 * Created by lei on 2016/8/11.
 */
public interface PresenterPolicy {

    void setListener(PresenterListener presenterListener);

    void voteQueryLatest();

    void voteQueryNewst(String vote_id);

    /**
     * 发起投票
     *
     * @param title
     * @param time
     * @param options
     */
    void voteCreate(String title, int time, Map<String, String> options);

    void voteCreate(String title, int time, List<String> options);

    /**
     * 暂停投票
     *
     * @param vote_id
     */
    void votePause(String vote_id);

    /**
     * 结束投票
     *
     * @param vote_id
     */
    void voteStop(String vote_id);

    /**
     * 恢复投票
     *
     * @param vote_id
     */
    void voteRestart(String vote_id);

    /**
     * 重新设置投票时长
     *
     * @param vote_id
     * @param time
     */
    void resetVoteTime(String vote_id, long time);


    void receiveTimeOutMessage(Message msg);

    /**
     * 收到房间其他人发送的投票信息
     *
     * @param msg
     */
    void receiveVoteMessage(Message msg);


    /**
     * 给主播间其他人发送消息(发起投票)
     */
    void sendVoteCreateMessage(VoteInfo info);

    void sendVotePauseMessage(VoteInfo info);

    void sendVoteRestartMessage(VoteInfo info);

    void sendVoteStopMessage(VoteInfo info);

    void sendVoteResetTimeMessage(VoteInfo info);

    void setCid(int cid);

  //  void setUsid(String uid);
}
