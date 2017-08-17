package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.DaggerFindSomeOneListPresenterComponent;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListAdapter;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenter;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenterModule;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 附近的人列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneNearbyListFragment extends TSListFragment<FindSomeOneNearbyListContract.Presenter, UserInfoBean> implements FindSomeOneNearbyListContract.View {
    /**
     * 找人的分类
     */
    public static final int TYPE_HOT = 0;
    public static final int TYPE_NEW = 1;
    public static final int TYPE_RECOMMENT = 2;
    public static final int TYPE_NEARBY = 3;

    // 获取页面类型的key
    public static final String PAGE_TYPE = "page_type";

    @Inject
    FindSomeOneNearbyListPresenter mFollowFansListPresenter;
    private int pageType;// 页面类型，由上一个页面决定

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        return new FindSomeOneNearbyListAdapter(getContext(), R.layout.item_find_some_list, mListDatas, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageType = getArguments().getInt(PAGE_TYPE, TYPE_HOT);
        }
    }

    @Override
    protected void initView(View rootView) {
        DaggerFindSomeOneNearbyListPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findSomeOneNearbyListPresenterModule(new FindSomeOneNearbyListPresenterModule(this))
                .build().inject(this);

        //mAuthBean = AppApplication.getmCurrentLoginAuth();
        super.initView(rootView);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore);
    }

    public static FindSomeOneNearbyListFragment initFragment() {
        FindSomeOneNearbyListFragment followFansListFragment = new FindSomeOneNearbyListFragment();
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }

    @Override
    public void upDateFollowFansState() {
        refreshData();
    }

    @Override
    public int getPageType() {
        return pageType;
    }

    /**
     * 此时需要的是之前数据的总和 {@see offset https://github.com/slimkit/thinksns-plus/blob/master/docs/zh-CN/api2/find-users.md}
     *
     * @param data
     * @return
     */
    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return Long.valueOf(data.size());
    }
}
