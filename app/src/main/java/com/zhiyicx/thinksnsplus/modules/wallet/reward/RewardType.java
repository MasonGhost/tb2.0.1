package com.zhiyicx.thinksnsplus.modules.wallet.reward;

/**
 * @Describe 打赏类型
 * @Author Jungle68
 * @Date 2017/5/23
 * @Contact master.jungle68@gmail.com
 */

public enum RewardType {
    INFO(10001), // 咨询 // requst code must lower 16 bit ,so id must be < 65000
    DYNAMIC(10002),// 动态
    USER(10003); // 用户

    public final int id;

    RewardType(int id) {
        this.id = id;
    }

}
