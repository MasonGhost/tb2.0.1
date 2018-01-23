package com.zhiyicx.thinksnsplus.modules.q_a.answer;

/**
 * @author Jliuer
 * @Date 18/01/23 15:03
 * @Email Jliuer@aliyun.com
 * @Description 问答编辑器  发布内容 类型
 *
 * ps：针对回答编辑器 界面 复用 的 三种情况
 */
public enum PublishType {
    PUBLISH_ANSWER(10001),
    UPDATE_ANSWER(10002),
    UPDATE_QUESTION(10003);

    public final int id;

    PublishType(int id) {
        this.id = id;
    }

}
