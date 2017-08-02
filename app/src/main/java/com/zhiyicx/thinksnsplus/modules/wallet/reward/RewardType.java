package com.zhiyicx.thinksnsplus.modules.wallet.reward;

/**
 * @Describe 打赏类型
 * @Author Jungle68
 * @Date 2017/5/23
 * @Contact master.jungle68@gmail.com
 */

public enum RewardType {
    INFO(10001), // 咨询
    DYNAMIC(10002);// 动态

    public final int id;

    RewardType(int id) {
        this.id = id;
    }

}
