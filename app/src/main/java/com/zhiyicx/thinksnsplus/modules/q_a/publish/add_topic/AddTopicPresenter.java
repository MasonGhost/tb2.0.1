package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import com.google.gson.Gson;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.repository.QA$RewardRepositoryPublish;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

@FragmentScoped
public class AddTopicPresenter extends AppBasePresenter<AddTopicContract.Repository, AddTopicContract.View>
        implements AddTopicContract.Presenter {

    @Inject
    QA$RewardRepositoryPublish mQA$RewardRepositoryPublish;

    @Inject
    public AddTopicPresenter(AddTopicContract.Repository repository, AddTopicContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestNetData(String name, Long maxId, Long follow, boolean isLoadMore) {
        mRepository.getAllTopic(name, maxId, follow).subscribe(new BaseSubscribeForV2<List<QATopicBean>>() {
            @Override
            protected void onSuccess(List<QATopicBean> data) {
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
    }

    @Override
    public void updateQuestion(QAPublishBean qaPublishBean) {
        mQA$RewardRepositoryPublish.updateQuestion(qaPublishBean)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.publish_doing)))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 解析数据，在跳转到问题详情页时需要用到
                        QAListInfoBean qaListInfoBean = new QAListInfoBean();
                        qaListInfoBean.setUser_id(AppApplication.getMyUserIdWithdefault());
                        qaListInfoBean.setLook(qaPublishBean.getLook());
                        mRootView.updateSuccess(qaListInfoBean);
                        mRootView.showSnackSuccessMessage("修改成功");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
    }

    @Override
    public List<QATopicBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QATopicBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mRepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mRepository.saveQuestion(qestion);
    }
}
