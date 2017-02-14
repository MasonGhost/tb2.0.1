package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, FollowFansItemBean> {

    @Override
    protected CommonAdapter<FollowFansItemBean> getAdapter() {
        List<FollowFansItemBean> datas = new ArrayList<>();
        datas.add(new FollowFansItemBean());
        datas.add(new FollowFansItemBean());
        datas.add(new FollowFansItemBean());
        datas.add(new FollowFansItemBean());
        datas.add(new FollowFansItemBean());
        datas.add(new FollowFansItemBean());
        return new CommonAdapter<FollowFansItemBean>(getContext(), R.layout.item_follow_fans_list, datas) {
            @Override
            protected void convert(ViewHolder holder, FollowFansItemBean followFansItemBean, int position) {

            }
        };
    }

    @Override
    protected boolean insertOrUpdateData(@NotNull List<FollowFansItemBean> data) {
        return false;
    }

    @Override
    public void setPresenter(FollowFansListContract.Presenter presenter) {
        mPresenter = presenter;
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

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }
}
