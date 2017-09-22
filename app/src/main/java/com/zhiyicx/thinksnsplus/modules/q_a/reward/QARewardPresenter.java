package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.google.gson.Gson;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_COMMENT_TO_ANSWER_LIST;

/**
 * @author Catherine
 * @describe 问题悬赏界面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QARewardPresenter extends AppBasePresenter<QARewardContract.RepositoryPublish, QARewardContract.View>
        implements QARewardContract.Presenter {

    @Inject
    CommentRepository mCommentRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;

    @Inject
    public QARewardPresenter(QARewardContract.RepositoryPublish repository, QARewardContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void publishQuestion(final QAPublishBean qaPublishBean) {
        handleWalletBlance((long) qaPublishBean.getAmount())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(com.zhiyicx.thinksnsplus.R
                        .string.publish_doing)))
                .flatMap(new Func1<Object, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(Object o) {
                        return qaPublishBean.isHasAgainEdite() ? mRepository.updateQuestion(qaPublishBean)
                                : mRepository.publishQuestion(qaPublishBean);
                    }
                })
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 解析数据，在跳转到问题详情页时需要用到
                        if (data == null) {
                            mRootView.showSnackMessage("编辑成功", Prompt.DONE);
                        } else {
                            try {
                                mRepository.deleteQuestion(qaPublishBean);
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                                QAListInfoBean qaListInfoBean = new Gson().fromJson
                                        (jsonObject.getString("question"), QAListInfoBean.class);
                                mRootView.publishQuestionSuccess(qaListInfoBean);
                                JSONArray array = jsonObject.getJSONArray("message");
                                mRootView.showSnackMessage(array.getString(0), Prompt.DONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showSnackErrorMessage(e.toString());
                            }
                        }
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
    public void resetReward(Long question_id, double amount) {
        Subscription subscription = mCommentRepository.getCurrentLoginUserInfo()
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(new Func1<UserInfoBean, Observable<BaseJsonV2<Object>>>() {
                    @Override
                    public Observable<BaseJsonV2<Object>> call(UserInfoBean userInfoBean) {
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                        if (userInfoBean.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(userInfoBean.getWallet());
                            if (userInfoBean.getWallet().getBalance() < amount) {
                                mRootView.goRecharge(WalletActivity.class);
                                return Observable.error(new RuntimeException(""));
                            }
                        }
                        return mRepository.resetReward(question_id, amount);
                    }
                }, throwable -> {
                    mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    return null;
                }, () -> null)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.qa_reset_reward_success));
                        mRootView.resetRewardSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
        addSubscrebe(subscription);
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
