package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.RankingModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.RankingListAdapter;
import com.zhiyicx.zhibolibrary.ui.view.RankingView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/4/24.
 */
@ActivityScope
public class RankingPresenter extends ListBasePresenter<SearchResult, RankingModel, RankingView> {
    private MoreLinearAdapter mAdapter;
    private Subscription mSubscription;
    private Subscription mUserinfoSubscription;
    private BaseJson<SearchResult[]> mApiList;

    @Inject
    public RankingPresenter(RankingModel model, RankingView rootView) {
        super(model, rootView);
    }

    public void getList(final boolean isMore) {
        if (mAdapter != null) {
            if (!isMore)
                mAdapter.isShowFooter(false);
            else
                mAdapter.isShowFooter(true);
        }
        prepare(isMore);//加载列表准备
        mSubscription = mModel.getRanking(

                mPage).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isMore) {
                            mRootView.showRefreshing();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .subscribe(new Action1<BaseJson<SearchResult[]>>() {
                    @Override
                    public void call(BaseJson<SearchResult[]> apiList) {
                        getUserInfoForRank(apiList, isMore);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (!isMore) {//隐藏loading
                            mRootView.hideRefreshing();
                            loadForNetBad();
                        } else {
                            mRootView.hideLoadMore();
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        }
                    }
                });

    }

    private void getUserInfoForRank(BaseJson<SearchResult[]> apiList, final boolean isMore) {
        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
            mApiList = apiList;
            /**
             * 通过usid获取用户信息
             */
            String usid = "";
            for (SearchResult searchResult : mApiList.data) {
                usid += searchResult.user.usid + ",";
            }

            if (usid.length() > 0)
                usid = usid.substring(0, usid.length() - 1);
            if (usid.length() > 0) {
                mUserinfoSubscription = mModel.getUsidInfo(usid, "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseJson<UserInfo[]>>() {
                            @Override
                            public void call(BaseJson<UserInfo[]> baseJson) {
                                if (baseJson.code.equals(ZBLApi.REQUEST_SUCESS)) {

                                    for (int i = 0; i < baseJson.data.length; i++) {
                                        baseJson.data[i].gold = mApiList.data[i].user.gold;
                                        mApiList.data[i].user = baseJson.data[i];
                                    }
                                    dealRefreshLayout(isMore);

                                    refresh(mApiList, isMore);//刷新数据
                                } else {
                                    errorDeal(isMore);
                                }
                            }

                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                                dealRefreshLayout(isMore);
                                mRootView.showNetBadPH();
                            }
                        });

            } else {
                dealRefreshLayout(isMore);
                refresh(mApiList, isMore);//刷新数据
            }


        } else {
            mRootView.showMessage(apiList.message);
        }

    }
    private void errorDeal(boolean isMore){
        if (!isMore) {//隐藏loading
            mRootView.hideRefreshing();
            mRootView.hidePlaceHolder();
        }
        else {
            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
        }
    }
    private void dealRefreshLayout(boolean isMore) {
        if (!isMore) {
            mRootView.hideRefreshing();
        } else {
            mRootView.hideLoadMore();
        }
    }


    /**
     * 初始化Item点击事件
     */
    private void initItemListener() {
        mAdapter.setOnItemClickListener(new MoreLinearAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(View view, SearchResult data) {
                watchUser(data);//查看用户信息
            }
        });
    }

    /**
     * 查看用户主页
     *
     * @param data
     */
    private void watchUser(SearchResult data) {
        // TODO: 16/10/10 跳转个人主页
        Intent intent = new Intent(ZhiboApplication.INTENT_ACTION_UESRINFO);
        Bundle bundle = new Bundle();
        bundle.putInt("uid", Integer.parseInt(data.user.uid));
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getAdapter(ArrayList<SearchResult> listDatas) {
        mAdapter = new RankingListAdapter(listDatas);
        return mAdapter;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRootView.setAdapter((MoreLinearAdapter) adapter);
        initItemListener();
    }

    @Override
    public void nonePrompt(boolean isMore) {
        if (!isMore) {
            mRootView.showPlaceHolder();
        } else {
            mRootView.showMessage(UiUtils.getString("str_load_more_prompt"));
        }
    }

    @Override
    protected void cleanStatus() {
        mRootView.hidePlaceHolder();
    }

    @Override
    public void isShowMoreLoading(boolean isShow) {
        mAdapter.isShowFooter(isShow);
        if (!isShow) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mSubscription);
        unSubscribe(mUserinfoSubscription);
    }
}
