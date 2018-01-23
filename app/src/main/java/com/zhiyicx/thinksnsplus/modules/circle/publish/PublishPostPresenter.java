package com.zhiyicx.thinksnsplus.modules.circle.publish;

import android.os.Bundle;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostPublishBean;
import com.zhiyicx.thinksnsplus.data.source.local.PostDraftBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailFragment;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownPresenter;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2018/01/23/16:15
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishPostPresenter extends MarkdownPresenter<PublishPostContract.View> implements PublishPostContract.Presenter {

    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    PostDraftBeanGreenDaoImpl mPostDraftBeanGreenDao;

    @Inject
    public PublishPostPresenter(PublishPostContract.View rootView) {
        super(rootView);
    }

    @Override
    public void publishPost(PostPublishBean postPublishBean) {
        Subscription subscribe = mBaseCircleRepository.sendCirclePost(postPublishBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.post_publishing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CirclePostListBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CirclePostListBean> data) {
                        mRootView.dismissSnackBar();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CirclePostDetailFragment.POST_DATA, data.getData());
                        bundle.putBoolean(CirclePostDetailFragment.POST_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CIRCLE_POST);
                        mRootView.sendPostSuccess(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string
                                .info_publishfailed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void saveDraft(BaseDraftBean postDraftBean) {
        if (postDraftBean instanceof PostDraftBean) {
            mPostDraftBeanGreenDao.saveSingleData((PostDraftBean) postDraftBean);
        }
    }
}
