package com.zhiyicx.votesdk.entity;

/**
 * 类说明：
 * 观众选择投票具体信息
 * Created by lei on 2016/9/2.
 */
@org.msgpack.annotation.Message
public class OptionDetail extends BaseModel {
    private String vote_key;//投票选项序列名称
    private int gold;//投的金币数量
    private String gift_code;//投的礼物code

    public OptionDetail() {
    }

    public OptionDetail(String vote_key, String gift_code, int gold) {
        this.vote_key = vote_key;
        this.gift_code = gift_code;
        this.gold = gold;
    }


    public String getVote_key() {
        return vote_key;
    }

    public void setVote_key(String vote_key) {
        this.vote_key = vote_key;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getGift_code() {
        return gift_code;
    }

    public void setGift_code(String gift_code) {
        this.gift_code = gift_code;
    }

    @Override
    public String toString() {
        return "OptionDetail{" +
                "vote_key='" + vote_key + '\'' +
                ", gold=" + gold +
                ", gift_code='" + gift_code + '\'' +
                '}';
    }
}

