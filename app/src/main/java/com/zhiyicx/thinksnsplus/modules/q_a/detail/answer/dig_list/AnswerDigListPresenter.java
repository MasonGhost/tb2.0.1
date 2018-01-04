package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AnswerInfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class AnswerDigListPresenter extends AppBasePresenter<AnswerDigListContract.View> implements AnswerDigListContract.Presenter {
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    AnswerInfoListBeanGreenDaoImpl mAnswerInfoListBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public AnswerDigListPresenter(AnswerDigListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AnswerDigListBean> data, boolean isLoadMore) {
        AnswerInfoBean answerInfoBean = mRootView.getAnswerInfoBean();
        answerInfoBean.setLikes(data);
        return false;
    }

    @Override
    public void handleFollowUser(int position, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        mRootView.upDataFollowState(position);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore, long id) {
        Subscription subscribe = mBaseQARepository.getAnswerDigListV2(id, maxId).subscribe(new BaseSubscribeForV2<List<AnswerDigListBean>>() {
            @Override
            protected void onSuccess(List<AnswerDigListBean> data) {
                LogUtils.i("digList_netData" + data.toString());
                mRootView.onNetResponseSuccess(data, isLoadMore);
            }

            @Override
            protected void onFailure(String message, int code) {
                mRootView.showMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                mRootView.onResponseError(throwable, isLoadMore);
            }
        });
        addSubscrebe(subscribe);
    }

    @Override
    public List<AnswerDigListBean> requestCacheData(Long maxId, boolean isLoadMore, AnswerInfoBean answerInfoBean) {
        List<AnswerDigListBean> likes = answerInfoBean.getLikes();
        mRootView.onCacheResponseSuccess(likes, isLoadMore);
        return likes;
    }
}
