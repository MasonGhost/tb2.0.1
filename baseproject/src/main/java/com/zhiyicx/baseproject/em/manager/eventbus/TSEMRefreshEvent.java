package com.zhiyicx.baseproject.em.manager.eventbus;

/**
 * @author Jliuer
 * @Date 18/02/01 13:55
 * @Email Jliuer@aliyun.com
 * @Description 刷新事件
 */
public class TSEMRefreshEvent {

    // 刷新的数量
    private int count;
    // 刷新的位置
    private int position;
    // 刷新方式类型
    private int type;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
