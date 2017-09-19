package com.zhiyicx.votesdk.entity;

/**
 * 类说明：
 *投票选项
 * Created by lei on 2016/8/15.
 */
@org.msgpack.annotation.Message
public class VoteOption {

    private String option_key;//选项序列名称
    private String option_value;//选项值名称
    private int option_ballot;//获得多少投票


    public String getOption_key() {
        return option_key;
    }

    public void setOption_key(String option_key) {
        this.option_key = option_key;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }

    public int getOption_ballot() {
        return option_ballot;
    }

    public void setOption_ballot(int option_ballot) {
        this.option_ballot = option_ballot;
    }

    @Override
    public String toString() {
        return "VoteOption{" +
                "option_key='" + option_key + '\'' +
                ", option_value='" + option_value + '\'' +
                ", option_ballot=" + option_ballot +
                '}';
    }
}
