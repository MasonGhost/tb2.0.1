package com.zhiyicx.zhibolibrary.ui.view;


import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;

/**
 * Created by jess on 16/5/11.
 */
public interface PublishCoreParentView {
    ZBApiImInfo getImInfo();
    SearchResult getData();
    UserInfo getUserInfo();
    void switchCamera();
    ZBLBaseFragment getPublishCoreview();
    boolean isSelfClose();



}
