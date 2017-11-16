package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainFragment extends TSListFragment<CircleMainContract.Presenter,GroupInfoBean> implements CircleMainContract.View{

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);

        return adapter;
    }
}
