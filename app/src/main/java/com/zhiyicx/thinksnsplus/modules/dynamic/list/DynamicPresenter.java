package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action0;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicPresenter extends BasePresenter<DynamicContract.Repository, DynamicContract.View> implements DynamicContract.Presenter {

    @Inject
    AuthRepository mAuthRepository;

    @Inject
    public DynamicPresenter(DynamicContract.Repository repository, DynamicContract.View rootView) {
        super(repository, rootView);
    }

    /**
     *
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore 加载状态
     */
    @Override
    public void requestNetData(int maxId, final boolean isLoadMore) {
        mRepository.getDynamicList(mRootView.getDynamicType(), (long) maxId,mRootView.getPage())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new BaseSubscribe<List<DynamicBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
    }

    @Override
    public List<DynamicBean> requestCacheData(int minTime, boolean isLoadMore) {
        List<DynamicBean> datas = new ArrayList<>();
//        for (int i = 0; i < 9; i++) {
//            DynamicBean dynamicBean = new DynamicBean();
//            dynamicBean.setUser_id(3);
//            UserInfoBean userInfoBean = AppApplication.AppComponentHolder.getAppComponent()
//                    .userInfoBeanGreenDao().getSingleDataFromCache((long) 3);
//            if (userInfoBean == null) {
//                userInfoBean = new UserInfoBean();
//                userInfoBean.setName("我的天");
//                userInfoBean.setUserIcon("www.baiu.com");
//            }
//            dynamicBean.setUserInfoBean(userInfoBean);
//            DynamicToolBean toolBean = new DynamicToolBean();
//            toolBean.setFeed_comment_count(i);
//            toolBean.setFeed_digg_count(2 * i);
//            toolBean.setFeed_view_count(3 * i);
//            toolBean.setIs_digg_feed(i % 2);
//            dynamicBean.setTool(toolBean);
//            DynamicDetailBean dynamicDetailBean = new DynamicDetailBean();
//            dynamicDetailBean.setContent("今天是个好日志" + i);
//            dynamicDetailBean.setTitle(i + "我知道觉得关键看感觉绝对客观艰苦的房价过快封建快攻打法就是");
//            List<Integer> images = new ArrayList<>();
//            for (int i1 = 0; i1 < i; i1++) {
//                images.add(i1);
//            }
//            dynamicDetailBean.setStorage(images);
//            dynamicBean.setFeed(dynamicDetailBean);
//            datas.add(dynamicBean);
//        }
        

        return datas;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return true;
    }

}