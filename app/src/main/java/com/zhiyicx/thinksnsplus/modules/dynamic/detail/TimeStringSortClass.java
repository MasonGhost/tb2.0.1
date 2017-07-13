package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;

import java.util.Comparator;

/**
 * @Author Jliuer
 * @Date 2017/6/9 16:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TimeStringSortClass implements Comparator<DynamicCommentBean> {

    private boolean reversed = true;

    public TimeStringSortClass(boolean reversed) {
        this.reversed = reversed;
    }

    public TimeStringSortClass() {
    }

    @Override
    public int compare(DynamicCommentBean t1, DynamicCommentBean t2) {
        if (reversed) {
            return t2.getCreated_at().compareTo(t1.getCreated_at());
        } else {
            return t1.getCreated_at().compareTo(t2.getCreated_at());
        }
    }


}