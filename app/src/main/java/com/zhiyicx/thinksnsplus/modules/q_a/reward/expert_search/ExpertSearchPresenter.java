package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class ExpertSearchPresenter extends AppBasePresenter<ExpertSearchContract.Repository, ExpertSearchContract.View>
        implements ExpertSearchContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public ExpertSearchPresenter(ExpertSearchContract.Repository repository, ExpertSearchContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(Long maxId, int topic_id, boolean isLoadMore) {
        Subscription subscribe = mRepository.getTopicExperts(maxId, topic_id)
                .subscribe(new BaseSubscribeForV2<List<ExpertBean>>() {
                    @Override
                    protected void onSuccess(List<ExpertBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestNetData(int size, String topic_ids, String keyword, boolean isLoadMore) {
        // mRepository.getExpertList(size, topic_ids, keyword)
        Observable<List<ExpertBean>> observable;
        if (!TextUtils.isEmpty(topic_ids)) {
            observable = mRepository.getExpertList(size, topic_ids, keyword);
        } else {
            observable = mUserInfoRepository.searchUserInfo(null, keyword, null, null, null)
                    .flatMap(new Func1<List<UserInfoBean>, Observable<List<ExpertBean>>>() {
                        @Override
                        public Observable<List<ExpertBean>> call(List<UserInfoBean> userInfoBeen) {
                            Gson gson = new Gson();
                            java.lang.reflect.Type needType = new TypeToken<List<ExpertBean>>() {
                            }.getType();
                            String result = gson.toJson(userInfoBeen);
                            return Observable.just(gson.fromJson(result, needType));
                        }
                    });
        }
        Subscription subscription =
                observable
                        .subscribe(new BaseSubscribeForV2<List<ExpertBean>>() {
                            @Override
                            protected void onSuccess(List<ExpertBean> data) {
                                mRootView.onNetResponseSuccess(data, isLoadMore);
                            }

                            @Override
                            protected void onFailure(String message, int code) {
                                super.onFailure(message, code);
                                mRootView.showMessage(message);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                super.onException(throwable);
                                mRootView.onResponseError(throwable, isLoadMore);
                            }
                        });
        addSubscrebe(subscription);
    }

    @Override
    public void handleFollowUser(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);
    }

    @Override
    public List<ExpertBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ExpertBean> data, boolean isLoadMore) {
        return false;
    }
}
