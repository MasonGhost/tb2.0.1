package com.zhiyicx.thinksnsplus.modules.wallet.integration.detail;

/**
 * @Describe 积分操作类型 doc {@see https://slimkit.github.io/plus-docs/v2/core/currency}
 * @Author Jungle68
 * @Date 2018/1/24
 * @Contact master.jungle68@gmail.com
 */
public class IntegrationActionTypeEmun {
    /**
     * target_type	string	操作类型 目前有：
     * default - 默认操作、
     * commodity - 购买积分商品、
     * user - 用户到用户流程（如采纳、付费置顶等）、
     * task - 积分任务、
     * recharge - 充值、
     * cash - 积分提取
     */
    public static final String DEFAULT = "default";
    public static final String COMMODITY = "commodity";
    public static final String TASK = "task";
    public static final String RECHARGE = "recharge";
    public static final String CASH = "cash";

}
