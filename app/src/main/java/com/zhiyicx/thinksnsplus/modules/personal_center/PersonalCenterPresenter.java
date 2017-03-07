package com.zhiyicx.thinksnsplus.modules.personal_center;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterPresenter extends BasePresenter<PersonalCenterContract.Repository,PersonalCenterContract.View> implements PersonalCenterContract.Presenter {
    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public List<DynamicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return false;
    }
}
