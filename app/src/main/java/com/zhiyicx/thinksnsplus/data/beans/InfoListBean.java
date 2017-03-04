package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoListBean extends BaseListBean {
    private List<String> iamges = new ArrayList<>();

    public List<String> getIamges() {
        return iamges;
    }

    public void setIamges(List<String> iamges) {
        this.iamges = iamges;
    }
}
