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
public class InfoBannerBean extends BaseListBean {

    private List<String> iamges = new ArrayList<>();

    public List<String> getIamges() {
        iamges.clear();
        iamges.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962," +
                "2666166364&fm=21&gp=0.jpg");
        iamges.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962," +
                "2666166364&fm=21&gp=0.jpg");
        iamges.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962," +
                "2666166364&fm=21&gp=0.jpg");
        iamges.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962," +
                "2666166364&fm=21&gp=0.jpg");
        return iamges;
    }

    public void setIamges(List<String> iamges) {
        this.iamges = iamges;
    }
}
