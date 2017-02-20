package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, FollowFansBean> implements FollowFansListContract.View {
    // 当前页面是关注页面还是粉丝页面:pageType
    public static final int FANS_FRAGMENT_PAGE = 0;
    public static final int FOLLOW_FRAGMENT_PAGE = 1;

    // 获取页面类型的key
    public static final String PAGE_TYPE = "page_type";
    @Inject
    FollowFansListPresenter mFollowFansListPresenter;
    private List<FollowFansBean> datas = new ArrayList<>();
    private int pageType;// 页面类型，由上一个页面决定
    private AuthBean mAuthBean;

    @Override
    protected CommonAdapter<FollowFansBean> getAdapter() {
        return new FollowFansListAdapter(getContext(), R.layout.item_follow_fans_list, datas, pageType, mPresenter);
    }

    @Override
    protected void initView(View rootView) {
        pageType = getArguments().getInt(PAGE_TYPE, FOLLOW_FRAGMENT_PAGE);
        mAuthBean = AppApplication.getmCurrentLoginAuth();
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        DaggerFollowFansListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .followFansListPresenterModule(new FollowFansListPresenterModule(FollowFansListFragment.this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
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

    @Override
    protected void requestNetData(int maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, mAuthBean.getUser_id(), pageType);
    }

    @Override
    protected List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, mAuthBean.getUser_id(), pageType);
    }

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index, int followState) {
        List<FollowFansBean> followFansBeanList = mAdapter.getDatas();
        FollowFansBean followFansBean = followFansBeanList.get(index);
        LogUtils.i("new_state--》" + followState);
        followFansBean.setFollowState(followState);
        refreshData(index);
    }

    @Override
    public void upDateUserInfo(List<UserInfoBean> userInfoBeanList) {
        onCacheResponseSuccess(requestCacheData(mMaxId, false), false);
        refreshData();
    }

}
