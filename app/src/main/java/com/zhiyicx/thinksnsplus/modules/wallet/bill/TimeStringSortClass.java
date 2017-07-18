package com.zhiyicx.thinksnsplus.modules.wallet.bill;

import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;

import java.util.Comparator;

/**
 * @Author Jliuer
 * @Date 2017/6/9 16:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TimeStringSortClass implements Comparator<RechargeSuccessBean> {

    private boolean reversed = true;

    public TimeStringSortClass(boolean reversed) {
        this.reversed = reversed;
    }

    public TimeStringSortClass() {
    }

    @Override
    public int compare(RechargeSuccessBean t1, RechargeSuccessBean t2) {
        if (reversed) {
            return t2.getCreated_at().compareTo(t1.getCreated_at());
        } else {
            return t1.getCreated_at().compareTo(t2.getCreated_at());
        }
    }


}