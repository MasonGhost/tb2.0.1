package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_INFO_LIST_COLLECT;
import static com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean.SEND_ING;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoDetailsPresenter extends BasePresenter<InfoDetailsConstract.Repository,
        InfoDetailsConstract.View> implements InfoDetailsConstract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    InfoCommentListBeanDaoImpl mInfoCommentListBeanDao;

    @Inject
    InfoListBeanGreenDaoImpl mInfoListBeanGreenDao;

    @Inject
    public InfoDetailsPresenter(InfoDetailsConstract.Repository repository, InfoDetailsConstract
            .View rootView) {
        super(repository, rootView);
    }

    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        mRepository.getInfoCommentList(mRootView.getNewsId() + "", maxId, 0L)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribe<List<InfoCommentListBean>>() {
                    @Override
                    protected void onSuccess(List<InfoCommentListBean> data) {
                        mInfoCommentListBeanDao.saveMultiData(data);

                        List<InfoCommentListBean> localComment = mInfoCommentListBeanDao
                                .getMySendingComment(mRootView.getNewsId());

                        if (!localComment.isEmpty()) {
                            for (int i = 0; i < localComment.size(); i++) {
                                localComment.get(i).setFromUserInfoBean(mUserInfoBeanGreenDao
                                        .getSingleDataFromCache(localComment.get(i).getUser_id()));
                                if (localComment.get(i).getReply_to_user_id() != 0) {
                                    localComment.get(i).setToUserInfoBean(mUserInfoBeanGreenDao
                                            .getSingleDataFromCache(localComment.get(i)
                                                    .getReply_to_user_id()));
                                }
                            }
                            localComment.addAll(data);
                            data.clear();
                            data.addAll(localComment);
                        }

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
    }

    @Override
    public void shareInfo() {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle("ThinkSNS+\b\b资讯");
        shareContent.setUrl(String.format(APP_DOMAIN + APP_PATH_INFO_DETAILS_FORMAT,
                mRootView.getCurrentInfo().getId()));
        shareContent.setContent(mRootView.getCurrentInfo().getTitle());
        if (mRootView.getCurrentInfo().getStorage() != null) {
            shareContent.setImage(ImageUtils.imagePathConvert(mRootView.getCurrentInfo()
                    .getStorage().getId() + "", 100));
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void handleCollect(boolean isUnCollected, String news_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        mRootView.setCollect(isUnCollected);
        int is_collection_news = isUnCollected ? 1 : 0;
        mRootView.getCurrentInfo().setIs_collection_news(is_collection_news);

        if (mRootView.getInfoType() == -100) {
            //return;// 搜索出来的资讯，收藏状态有待优化  已处理
        }
        mInfoListBeanGreenDao.saveCollect(mRootView.getCurrentInfo(), is_collection_news);
        EventBus.getDefault().post(mRootView.getCurrentInfo(), EVENT_SEND_INFO_LIST_COLLECT);
        mRepository.handleCollect(isUnCollected, news_id);
    }

    @Override
    public void handleLike(boolean isLiked, String news_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        mRootView.setDigg(isLiked);
        int is_dig_news = isLiked ? 1 : 0;
        mRootView.getCurrentInfo().setIs_digg_news(is_dig_news);

        if (mRootView.getInfoType() == -100) {
            //return;// 搜索出来的资讯，收藏状态有待优化  已处理
        }
        mInfoListBeanGreenDao.saveDig(mRootView.getCurrentInfo(), is_dig_news);
        mRepository.handleLike(isLiked, news_id);
    }

    @Override
    public boolean isCollected() {
        return mInfoListBeanGreenDao.isCollected(mRootView.getNewsId().intValue());
    }

    @Override
    public boolean isDiged() {
        return mInfoListBeanGreenDao.isDiged(mRootView.getNewsId().intValue());
    }

    @Override
    public void deleteComment(InfoCommentListBean data) {
        mInfoCommentListBeanDao.deleteSingleCache(data);
        mRootView.getListDatas().remove(data);
        if (mRootView.getListDatas().size() == 1) {// 占位
            InfoCommentListBean position_zero = new InfoCommentListBean();
            position_zero.setId(mRootView.getNewsId().intValue());
            InfoCommentListBean emptyData = new InfoCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mRepository.deleteComment(mRootView.getNewsId().intValue(), data.getId());
    }

    /**
     * 处理发送动态数据
     *
     * @param infoCommentListBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_INFO_LIST)
    public void handleSendComment(InfoCommentListBean infoCommentListBean) {
        System.out.println("dynamicCommentBean = " + infoCommentListBean.toString());
        Observable.just(infoCommentListBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<InfoCommentListBean, Integer>() {
                    @Override
                    public Integer call(InfoCommentListBean infoCommentListBean1) {
                        int size = mRootView.getListDatas().size();
                        int infoPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getListDatas().get(i).getComment_mark()
                                    == infoCommentListBean1.getComment_mark()) {
                                infoPosition = i;
                                mRootView.getListDatas().get(i).setState(infoCommentListBean1
                                        .getState());
                                mRootView.getListDatas().get(i).setId(infoCommentListBean1.getId());
                                mRootView.getListDatas().get(i).setComment_mark
                                        (infoCommentListBean1.getComment_mark());
                                break;
                            }
                        }
                        return infoPosition;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer > 0) {
                            mRootView.refreshData(); // 加上 header
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    @Override
    public void sendComment(int reply_id, String content) {
        InfoCommentListBean createComment = new InfoCommentListBean();

        createComment.setInfo_id(mRootView.getNewsId().intValue());

        createComment.setState(SEND_ING);

        createComment.setComment_content(content);

        createComment.setReply_to_user_id(reply_id);

        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        if (reply_id == 0) {// 回复资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id((long) reply_id);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache((long)
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        mInfoCommentListBeanDao.insertOrReplace(createComment);
        if (mRootView.getListDatas().get(1).getComment_content() == null) {
            mRootView.getListDatas().remove(1);// 去掉占位图
        }
        mRootView.getListDatas().add(1, createComment);
        mRootView.refreshData();
        mRepository.sendComment(content, mRootView.getNewsId(), reply_id,
                createComment.getComment_mark());
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
    public List<InfoCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        return false;
    }
}
