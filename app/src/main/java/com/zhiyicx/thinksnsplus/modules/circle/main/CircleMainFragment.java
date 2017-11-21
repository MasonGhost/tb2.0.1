package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleTypeItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Author Jliuer
 * @Date 2017/11/14/11:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainFragment extends TSListFragment<CircleMainContract.Presenter, GroupInfoBean> implements CircleMainContract.View {

    private CircleMainHeader mCircleMainHeader;

    @Override
    protected boolean setUseCenterLoading() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.group);
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

    public static CircleMainFragment newInstance() {
        CircleMainFragment circleMainFragment = new CircleMainFragment();
        return circleMainFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        for (int i = 0; i < 12; i++) {
            GroupInfoBean groupInfoBean = new GroupInfoBean();
            groupInfoBean.setId((i == 0 || i == 6) ? -1 : i);
            groupInfoBean.setTitle("我加入");
            groupInfoBean.setIntro("查看更多");
            mListDatas.add(groupInfoBean);
        }
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem());
        adapter.addItemViewDelegate(new CircleTypeItem());
        return adapter;
    }

    @Override
    protected void initData() {
        mCircleMainHeader = new CircleMainHeader(getActivity(), null, 2341);
        mHeaderAndFooterWrapper.addHeaderView(mCircleMainHeader.getCircleMainHeader());
        super.initData();
    }
}
