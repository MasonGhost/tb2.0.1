package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.GoldRankModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.ui.adapter.GoldRankListAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.view.GoldRankView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jungle on 16/5/24.
 */
@ActivityScope
public class GoldRankPresenter extends ListBasePresenter<SearchResult, GoldRankModel, GoldRankView> {
    private MoreLinearAdapter mAdapter;
    private Subscription mSubscription;
    private Subscription mUsdiSubscription;
    private BaseJson<SearchResult[]> mApiList;//礼物排行榜数据
    @Inject
    public GoldRankPresenter(GoldRankModel model, GoldRankView rootView) {
        super(model, rootView);
    }


    /**
     * 获取列表数据
     * @param isMore
     * @param usid
     */
    public void getList(final boolean isMore, String usid) {
        prepare(isMore);//加载列表准备
        mSubscription = mModel.getRanking(usid
                , mPage).subscribeOn(Schedulers.io())
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
                        if (!isMore) {
                            mRootView.hideRefreshing();
                        }
                    }
                })
                .subscribe(new Action1<BaseJson<SearchResult[]>>() {
                    @Override
                    public void call(BaseJson<SearchResult[]> apiList) {
                        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            getUserInfoFroRankList(apiList,isMore);

                        } else {
                            mRootView.showMessage(apiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (!isMore) {//隐藏loading
                            mRootView.hideRefreshing();
                            loadForNetBad();
                        } else {
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        }
                    }
                });

    }

    /**
     * 通过Usid获取用户信息
     * @param apiList
     * @param isMore
     */
    private void getUserInfoFroRankList(BaseJson<SearchResult[]> apiList, final boolean isMore) {
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
                mUsdiSubscription=   mModel.getUsidInfo(usid, "").subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<BaseJson<UserInfo[]>>() {
                    @Override
                    public void call(BaseJson<UserInfo[]> baseJson) {
                        if (baseJson.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            for (int i = 0; i < baseJson.data.length; i++) {
                                baseJson.data[i].gold = mApiList.data[i].user.gold;
                                mApiList.data[i].user = baseJson.data[i];
                            }
                            refresh(mApiList, isMore);//刷新数据
                        }else {
                            errorDeal(isMore);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

            }


        }
        else {
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
    /**
     * 隐藏缺省图
     */
    @Override
    protected void cleanStatus() {
        mRootView.hidePlaceHolder();
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
        // TODO: 16/10/10 跳转到用户信息
        Intent intent = new Intent(ZhiboApplication.INTENT_ACTION_UESRINFO);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", data.user.uid);
        intent.putExtras(bundle);
        UiUtils.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getAdapter(ArrayList<SearchResult> listDatas) {
        mAdapter = new GoldRankListAdapter(listDatas);
        return mAdapter;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRootView.setAdapter((MoreLinearAdapter) adapter);
        initItemListener();
    }

    /**
     * 没有数据处理
     * @param isMore
     */
    @Override
    public void nonePrompt(boolean isMore) {
        if (!isMore) {
            mRootView.showMessage("排行榜还没有人哦,赶快努力吧~");
        } else {
            mRootView.showMessage(UiUtils.getString("str_load_more_prompt"));
        }
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
        unSubscribe(mUsdiSubscription);
    }
}
