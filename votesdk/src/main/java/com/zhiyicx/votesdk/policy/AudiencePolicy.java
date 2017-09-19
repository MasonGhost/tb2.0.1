package com.zhiyicx.votesdk.policy;


import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.listener.AudienceListener;
import com.zhiyicx.votesdk.listener.OnAudienceListener;

/**
 * 类说明：
 * 观众端接口
 * Created by lei on 2016/8/11.
 */
public interface AudiencePolicy {

    void setListener(AudienceListener audienceListener);

    /**
     * 投票
     *
     * @param vote_id    投票活动id
     * @param option_key //选项key'
     * @param       //票数
     */
    void votePoll(String vote_id, String option_key,String gift_code,int gold);

    /**
     * 查询某主播最近一次投票信息
     * @param usid
     */
    void voteQueryLatest(String usid);

    /**
     * 查询某个投票最新的信息
     * @param voteId
     */
    void voteQueryNewst(String voteId);

    /**
     * 发送投票消息
     */
    void sendVoteMessage(VoteInfo info);



    /**
     * 收到其他观众投票信息
     *
     * @param msg
     */
    void receiveAudienceMessage(Message msg);

    /**
     * 收到主播端发送的消息（发起投票、投票改变、）
     *
     * @param msg
     */
    void receivePresenterMessage(Message msg);

    void receiveTimeOutMessage(Message msg);

    void setCid(int cid);

    //void setUsid(String usid);

}
