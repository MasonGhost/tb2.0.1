package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @Describe 找人列表页
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneListFragment extends TSListFragment<FindSomeOneListContract.Presenter, UserInfoBean> implements FindSomeOneListContract.View {
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
    FindSomeOneListPresenter mFollowFansListPresenter;
    private int pageType;// 页面类型，由上一个页面决定

    private int mRecommentUserSize = 0;// 后台推荐用户数量

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        return new FindSomeOneListAdapter(getContext(), R.layout.item_find_some_list, mListDatas, mPresenter);
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
        DaggerFindSomeOneListPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findSomeOneListPresenterModule(new FindSomeOneListPresenterModule(FindSomeOneListFragment.this))
                .build().inject(FindSomeOneListFragment.this);
        super.initView(rootView);

    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            super.initData();
        }
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
    protected boolean sethasFixedSize() {
        return true;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, pageType);
    }

    public static FindSomeOneListFragment initFragment(Bundle bundle) {
        FindSomeOneListFragment followFansListFragment = new FindSomeOneListFragment();
        followFansListFragment.setArguments(bundle);
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
        return Long.valueOf(mListDatas.size() - mRecommentUserSize);
    }

    @Override
    public void setRecommentUserSize(int recommentUserSize) {
        this.mRecommentUserSize = recommentUserSize;
    }

    @Override
    protected int getPagesize() {
        return FindSomeOneListPresenter.DEFAULT_PAGE_SIZE;
    }
}
