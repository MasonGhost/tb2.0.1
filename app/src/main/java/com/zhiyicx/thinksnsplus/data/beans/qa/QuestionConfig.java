package com.zhiyicx.thinksnsplus.data.beans.qa;

/**
 * @author Jungle68
 * @describe  doc  @see https://slimkit.github.io/docs/api-v2-question-answer.html
 * @date 2018/2/9
 * @contact master.jungle68@gmail.com
 */
public class QuestionConfig {

    /**
     * apply_amount : 200 对应配置信息的    "question:apply_amount": 200,  //  申请精选所需支付金额
     * onlookers_amount : 100 对应配置信息的    "question:onlookers_amount": 100  //  围观答案所需支付金额
     * anonymity_rule : 匿名规则12321
     */

    private int apply_amount;
    private int onlookers_amount;
    private String anonymity_rule;

    public int getApply_amount() {
        return apply_amount;
    }

    public void setApply_amount(int apply_amount) {
        this.apply_amount = apply_amount;
    }

    public int getOnlookers_amount() {
        return onlookers_amount;
    }

    public void setOnlookers_amount(int onlookers_amount) {
        this.onlookers_amount = onlookers_amount;
    }

    public String getAnonymity_rule() {
        return anonymity_rule;
    }

    public void setAnonymity_rule(String anonymity_rule) {
        this.anonymity_rule = anonymity_rule;
    }
}
