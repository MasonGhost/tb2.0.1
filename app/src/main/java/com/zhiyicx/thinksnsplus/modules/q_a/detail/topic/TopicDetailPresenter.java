package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.QATopicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_DEFAULT;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class TopicDetailPresenter extends AppBasePresenter<TopicDetailContract.View>
        implements TopicDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    QATopicBeanGreenDaoImpl mQaTopicBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;
    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public TopicDetailPresenter(TopicDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getTopicDetail(String topic_id) {
        Subscription subscription = mBaseQARepository.getTopicDetail(topic_id)
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
        if (isFollow) {
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() + 1);
        } else {
            mRootView.getCurrentTopicBean().setFollows_count(mRootView.getCurrentTopicBean().getFollows_count() - 1);
        }
        mRootView.updateFollowState();
        mQaTopicBeanGreenDao.updateSingleData(mRootView.getCurrentTopicBean());
        EventBus.getDefault().post(mRootView.getCurrentTopicBean(), EventBusTagConfig.EVENT_QA_SUBSCRIB);
        mBaseQARepository.handleTopicFollowState(topic_id, isFollow);
    }

    @Override
    public void shareTopic(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentTopicBean().getName());
//        shareContent.setUrl(String.format(Locale.getDefault(), APP_PATH_SHARE_DEFAULT,
//                mRootView.getCurrentTopicBean().getId()));
        shareContent.setUrl(ApiConfig.APP_DOMAIN + APP_PATH_SHARE_DEFAULT);
        shareContent.setContent(mRootView.getCurrentTopicBean().getDescription());

        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
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
    public void saveQuestion(QAPublishBean qaPublishBean) {
        mBaseQARepository.saveQuestion(qaPublishBean);
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
    public void updateList(String message) {
        mRootView.showDeleteSuccess();
    }
}
