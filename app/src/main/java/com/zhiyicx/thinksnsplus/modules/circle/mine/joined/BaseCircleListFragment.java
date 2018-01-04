package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 圈子类别基类
 * @Author Jungle68
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
public class BaseCircleListFragment extends TSListFragment<BaseCircleListContract.Presenter, CircleInfo>
        implements BaseCircleListContract.View, BaseCircleItem.CircleItemItemEvent {

    public static final String BUNDLE_IS_NEED_TOOLBAR = "isNeedToolBar";

    @Inject
    BaseCircleListPresenter mCircleListPresenter;

    private boolean mIsNeedToolBar;

    public static BaseCircleListFragment newInstance(boolean isNeedToolBar) {
        BaseCircleListFragment circleListFragment = new BaseCircleListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(BUNDLE_IS_NEED_TOOLBAR, isNeedToolBar);
        circleListFragment.setArguments(bundle);
        return circleListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mIsNeedToolBar = getArguments().getBoolean(BUNDLE_IS_NEED_TOOLBAR);
        }
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return mIsNeedToolBar;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return mIsNeedToolBar && super.setUseStatusView();
    }

    @Override
    protected int setLeftImg() {
        if (mIsNeedToolBar) {
            return super.setLeftImg();
        } else {
            return 0;
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem(isMineJoined(), mActivity, this, mPresenter));
        return adapter;
    }

    protected boolean isMineJoined() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initDagger();
    }

    protected void initDagger() {
        Observable.create(subscriber -> {
            DaggerBaseCircleListComponent
                    .builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .baseCircleListPresenterModule(new BaseCircleListPresenterModule(BaseCircleListFragment.this))
                    .build()
                    .inject(BaseCircleListFragment.this);

            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    @Override
    public void toAllJoinedCircle(CircleInfo circleInfo) {

    }

    @Override
    public void toCircleDetail(CircleInfo circleInfo) {
        boolean isClosedCircle = CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode())
                || CircleInfo.CirclePayMode.PRIVATE.value.equals(circleInfo.getMode());
        boolean isJoined = circleInfo.getJoined() != null;

        if (isClosedCircle && !isJoined) {
            showSnackErrorMessage(getString(R.string.circle_blocked));
            return;
        }
        Intent intent = new Intent(getActivity(), CircleDetailActivity.class);
        intent.putExtra(CircleDetailFragment.CIRCLE_ID, circleInfo.getId());
        startActivity(intent);
    }

    @Override
    public void changeRecommend() {

    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {
        mPresenter.dealCircleJoinOrExit(position, circleInfo);
    }

    @Override
    protected Long getMaxId(@NotNull List<CircleInfo> data) {
        return (long) mListDatas.size();
    }

    @Override
    public CircleClient.MineCircleType getMineCircleType() {
        return CircleClient.MineCircleType.JOIN;
    }

    @Override
    public String getSearchInput() {
        return "";
    }
}
