package com.zhiyicx.thinksnsplus.modules.usertag;

/**
 * @Describe 标签来源
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public enum TagFrom {
    REGISTER(8001),
    USER_EDIT(8002),
    INFO_PUBLISH(8003);

    public int id;

    TagFrom(int id) {
        this.id = id;
    }
}
