package com.zhiyicx.votesdk.entity;

import java.util.List;

/**
 * 类说明：
 * 投票全部信息
 * Created by lei on 2016/8/15.
 */
@org.msgpack.annotation.Message
public class VoteInfo extends BaseModel {
    private String vote_id;//投票id
    private String create_time;//创建时间
    private String title;//主题
    private int time;//本次投票持续时间
    private int ballot;//总票数
    private int status;//状态： 1：正在投票；2：投票临时关闭；0：投票结束
    private List<VoteOption> options;//下面所有选项list
    private OptionDetail voteDetail;//选择选项的具体信息

    public OptionDetail getVoteDetail() {
        return voteDetail;
    }

    public void setVoteDetail(OptionDetail voteDetail) {
        this.voteDetail = voteDetail;
    }

    public String getVote_id() {
        return vote_id;
    }

    public void setVote_id(String vote_id) {
        this.vote_id = vote_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getBallot() {
        return ballot;
    }

    public void setBallot(int ballot) {
        this.ballot = ballot;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<VoteOption> getOptions() {
        return options;
    }

    public void setOptions(List<VoteOption> options) {
        this.options = options;
    }


    @Override
    public String toString() {
        return "VoteInfo{" +
                "vote_id='" + vote_id + '\'' +
                ", create_time='" + create_time + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", ballot=" + ballot +
                ", status=" + status +
                ", options=" + options +
                ", optionDetail="  +
                '}';
    }

}
