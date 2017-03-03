package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/3
 * @contact email:450127106@qq.com
 */

public class DigListFragment extends TSListFragment<DigListContract.Presenter,DynamicBean> {
    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        return null;
    }

    @Override
    public void setPresenter(DigListContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }
}
