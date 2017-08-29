package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.QATopicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_DEFAULT;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class TopicDetailPresenter extends AppBasePresenter<TopicDetailContract.Repository, TopicDetailContract.View>
        implements TopicDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    QATopicBeanGreenDaoImpl mQaTopicBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public TopicDetailPresenter(TopicDetailContract.Repository repository, TopicDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mRepository.getQAQuestionByTopic(String.valueOf(mRootView.getTopicId()),
                "", maxId, mRootView.getCurrentType())
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<QAListInfoBean>>() {

                    @Override
                    protected void onSuccess(List<QAListInfoBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<QAListInfoBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return null;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getTopicDetail(String topic_id) {
        Subscription subscription = mRepository.getTopicDetail(topic_id)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<QATopicBean>() {

                    @Override
                    protected void onSuccess(QATopicBean data) {
                        mRootView.setTopicDetail(data);
                        mQaTopicBeanGreenDao.insertOrReplace(data);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void handleTopicFollowState(String topic_id, boolean isFollow) {
        mRootView.getCurrentTopicBean().setHas_follow(isFollow);
        if (isFollow){
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() + 1);
        } else {
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() - 1);
        }
        mRootView.updateFollowState();
        mQaTopicBeanGreenDao.updateSingleData(mRootView.getCurrentTopicBean());
        EventBus.getDefault().post(mRootView.getCurrentTopicBean(), EventBusTagConfig.EVENT_QA_SUBSCRIB);
        mRepository.handleTopicFollowState(topic_id, isFollow);
    }

    @Override
    public void shareTopic(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentTopicBean().getName());
//        shareContent.setUrl(String.format(Locale.getDefault(), APP_PATH_SHARE_DEFAULT,
//                mRootView.getCurrentTopicBean().getId()));
        shareContent.setUrl(APP_PATH_SHARE_DEFAULT);
        shareContent.setContent(mRootView.getCurrentTopicBean().getDescription());

        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        } else {
            shareContent.setBitmap(bitmap);
        }

        if (mRootView.getCurrentTopicBean().getAvatar() != null) {
            shareContent.setImage(mRootView.getCurrentTopicBean().getAvatar());
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE)
    public void updateList(Bundle bundle) {
        if (bundle != null) {
            QAListInfoBean qaListInfoBean = (QAListInfoBean) bundle.
                    getSerializable(EventBusTagConfig.EVENT_UPDATE_QUESTION_DELETE);
            if (qaListInfoBean != null) {
                for (int i = 0; i < mRootView.getListDatas().size(); i++) {
                    if (qaListInfoBean.getId().equals(mRootView.getListDatas().get(i).getId())){
                        mRootView.getListDatas().remove(i);
                        mRootView.refreshData();
                        mRootView.showDeleteSuccess();
                        break;
                    }
                }
            }
        }
    }
}
