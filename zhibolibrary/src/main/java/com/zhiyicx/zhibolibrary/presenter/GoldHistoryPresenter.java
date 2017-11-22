package com.zhiyicx.zhibolibrary.presenter;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.GoldHistoryModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.ui.adapter.GoldHistoryListAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.MoreLinearAdapter;
import com.zhiyicx.zhibolibrary.ui.view.GoldHistoryView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 16/4/26.
 */
@ActivityScope
public class GoldHistoryPresenter extends ListBasePresenter<GoldHistoryJson,GoldHistoryModel,GoldHistoryView> {
    private MoreLinearAdapter mAdapter;
    private Subscription mSubscription;

    @Inject
    public GoldHistoryPresenter(GoldHistoryModel model, GoldHistoryView rootView) {
        super(model, rootView);
    }


    public void getList(final boolean isMore) {
        prepare(isMore);//加载列表准备
        mSubscription = mModel.getGoldList(mRootView.getType()
                , mPage
               )
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (!isMore) {
                            mRootView.showLoading();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                        if (!isMore) {
                            mRootView.hideLoading();
                        }
                    }
                })
                .subscribe(new Action1<BaseJson<GoldHistoryJson[]>>() {
                    @Override
                    public void call(BaseJson<GoldHistoryJson[]> apiList) {
                        if (apiList.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            refresh(apiList, isMore);//刷新数据
                        } else {
                            mRootView.showMessage(apiList.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                        if (!isMore) {//隐藏loading
                            mRootView.hideLoading();
                            loadForNetBad();
                        }else{
                            mRootView.showMessage(UiUtils.getString("str_net_erro"));//提示用户
                        }
                    }
                });
    }

    @Override
    public RecyclerView.Adapter getAdapter(ArrayList<GoldHistoryJson> listDatas) {
        mAdapter = new GoldHistoryListAdapter(listDatas);
        return mAdapter;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRootView.setAdapter((MoreLinearAdapter) adapter);
    }

    @Override
    public void nonePrompt(boolean isMore) {
        if (isMore) {//加载更多时数据为空
            mRootView.showMessage(UiUtils.getString("str_load_more_prompt"));
        } else {//上拉刷新时数据为空
//            mRootView.showMessage(UiUtils.getString("str_not_exchange_gold"));
            mRootView.showPlaceHolder();
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
        unSubscribe(mSubscription);//解除订阅
    }
}
