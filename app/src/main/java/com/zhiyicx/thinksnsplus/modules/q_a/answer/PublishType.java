package com.zhiyicx.thinksnsplus.modules.q_a.answer;

/**
 * @Describe 打赏类型
 * @Author Jungle68
 * @Date 2017/5/23
 * @Contact master.jungle68@gmail.com
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
