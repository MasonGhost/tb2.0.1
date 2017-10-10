package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.HomeModel;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.EndStreamingActivity;
import com.zhiyicx.zhibolibrary.ui.activity.PublishLiveActivity;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.fragment.LiveListFragment;
import com.zhiyicx.zhibolibrary.ui.view.HomeView;
import com.zhiyicx.zhibolibrary.util.KnifeUtil;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;
import com.zhiyicx.zhibosdk.manage.listener.OnCloseStatusListener;
import com.zhiyicx.zhibosdk.manage.listener.OncheckSteamStatusListener;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zhiyicx on 2016/3/16.
 */
@ActivityScope
public class HomePresenter extends BasePresenter<HomeModel, HomeView> {

    public final String TAG = this.getClass().getSimpleName();

    @Inject
    public HomePresenter(HomeModel model, HomeView rootView) {
        super(model, rootView);
        KnifeUtil.bindTarget(this, rootView);
    }


    /**
     * 返回主页的分页
     *
     * @return
     */
    public List<ZBLBaseFragment> getFragments() {
        List<ZBLBaseFragment> fragmentList = new ArrayList<>();//将主页的4个分页加入集合展示
        fragmentList.add(new LiveListFragment());
//        fragmentList.add(new ReplayFragment());
//        fragmentList.add(new MessageFragment());
//        fragmentList.add(new MyFragment());
//        fragmentList.add(new LiveListFragment());
        return fragmentList;
    }


    /**
     * 初始化流信息
     */
    public void initStream() {

        ZBStreamingClient.checkStrem(new OncheckSteamStatusListener() {
            @Override
            public void onStartCheck() {
                mRootView.showLoading();//显示loading
            }

            @Override
            public void onSuccess() {
                Intent intent = new Intent(UiUtils.getContext(), PublishLiveActivity.class);
                mRootView.launchLiveRoom(intent);
                mRootView.hideLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage("创建房间失败");
                mRootView.hideLoading();
            }

            @Override
            public void onFial(String code, String message) {
                mRootView.showMessage(message);
                mRootView.hideLoading();
            }

            @Override
            public void onDisable(String time) {
                mRootView.hideLoading();
                mRootView.showMessage(String.format(UiUtils.getString(R.string.str_limit_play_prompt)
                        , time));

            }
        });

    }

    /**
     * 关闭流
     */
    @org.simple.eventbus.Subscriber(tag = "close_stearm")
    public void close(Bundle bundle) {
        final ZBEndStreamJson json = (ZBEndStreamJson) bundle.getSerializable("endStream");

        ZBStreamingClient.getInstance().closePlay(new OnCloseStatusListener() {
            @Override
            public void onSuccess(ZBEndStreamJson endStreamJson) {
//                if (json != null)
//                    launchEndStreamActivity(json);
//                else
//                    launchEndStreamActivity(endStreamJson);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
//                if(json != null)
//                launchEndStreamActivity(json);
            }

            @Override
            public void onFial(String code, String message) {
                LogUtils.warnInfo(TAG, message);
            }
        });
        launchEndStreamActivity(json, (UserInfo) bundle.getSerializable("presenter"));

    }

    /**
     * 发送数据到结束直播页面
     *
     * @param endStreamJson
     */
    private void launchEndStreamActivity(ZBEndStreamJson endStreamJson, UserInfo userInfo) {
        Intent intent = new Intent(UiUtils.getContext(), EndStreamingActivity.class);
        intent.putExtra("isAudience", false);//是否为观众
        intent.putExtra("isException", endStreamJson.isException);//是异常结束
        Bundle bundle = new Bundle();
        bundle.putSerializable("income", endStreamJson.data);
        bundle.putSerializable("presenter", userInfo);
        intent.putExtras(bundle);
        mRootView.launchEndStreamActivity(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
