package com.zhiyicx.thinksnsplus.modules.circle.detail;

import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/18/18:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class GroupZipBean {
    private GroupInfoBean mGroupInfoBean;
    private List<GroupDynamicListBean> mGroupDynamicList;

    public GroupZipBean(GroupInfoBean groupInfoBean, List<GroupDynamicListBean> groupDynamicList) {
        mGroupInfoBean = groupInfoBean;
        mGroupDynamicList = groupDynamicList;
    }

    public GroupInfoBean getGroupInfoBean() {
        return mGroupInfoBean;
    }

    public void setGroupInfoBean(GroupInfoBean groupInfoBean) {
        mGroupInfoBean = groupInfoBean;
    }

    public List<GroupDynamicListBean> getGroupDynamicList() {
        return mGroupDynamicList;
    }

    public void setGroupDynamicList(List<GroupDynamicListBean> groupDynamicList) {
        mGroupDynamicList = groupDynamicList;
    }
}
