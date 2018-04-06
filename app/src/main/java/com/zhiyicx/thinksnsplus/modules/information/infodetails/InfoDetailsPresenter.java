package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.EmptySubscribe;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseRewardRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.VertifyCodeRepository;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
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
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_INFO_LIST_COLLECT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_INFO_LIST_DELETE_UPDATE;
import static com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean.SEND_ING;

/**
 * @Author Jliuer
 * @Date 2017/03/15
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoDetailsPresenter extends AppBasePresenter<InfoDetailsConstract.View> implements InfoDetailsConstract.Presenter,
        OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    InfoCommentListBeanDaoImpl mInfoCommentListBeanDao;

    @Inject
    InfoListBeanGreenDaoImpl mInfoListBeanGreenDao;

    @Inject
    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;
    @Inject
    BaseInfoRepository mBaseInfoRepository;
    @Inject
    BaseRewardRepository mBaseRewardRepository;

    @Inject
    public InfoDetailsPresenter(InfoDetailsConstract
                                        .View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        if (mRootView.getCurrentInfo().getRelateInfoList() == null
                || mRootView.getCurrentInfo().getRelateInfoList().size() == 0) {
            Subscription subscribe = Observable.zip(mBaseInfoRepository.getInfoDetail(String.valueOf(mRootView.getNewsId())),
                    mBaseInfoRepository.getInfoDigListV2(String.valueOf(mRootView.getNewsId()), 0L),
                    mBaseInfoRepository.getRelateInfoList(String.valueOf(mRootView.getNewsId())),
                    mBaseInfoRepository.getInfoCommentListV2(String.valueOf(mRootView.getNewsId()), 0L, 0L),
                    mBaseRewardRepository.getRewardCount(mRootView.getCurrentInfo().getId()),
                    mBaseRewardRepository.rewardInfoList(mRootView.getCurrentInfo().getId(),
                            TSListFragment.DEFAULT_PAGE_SIZE, null, null, null),
                    (infoListDataBean, infoDigListBeen, infoRelateBean, infoCommentBean, rewardsCountBean, rewardsListBeen) -> {
                        infoListDataBean.setDigList(infoDigListBeen);
                        infoListDataBean.setRelateInfoList(infoRelateBean);
                        infoListDataBean.setCommentList(dealComment(infoCommentBean, 0));
                        Observable.empty()
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new EmptySubscribe<Object>() {
                                    @Override
                                    public void onCompleted() {
                                        mRootView.updateReWardsView(rewardsCountBean, rewardsListBeen);
                                    }
                                });

                        return infoListDataBean;
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscribeForV2<InfoListDataBean>() {
                        @Override
                        protected void onSuccess(InfoListDataBean data) {
                            mRootView.updateInfoHeader(data);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            handleInfoHasBeDeleted(code);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
            addSubscrebe(subscribe);

        } else {
            Subscription subscribe = mBaseInfoRepository.getInfoCommentListV2(mRootView.getNewsId() + "", maxId, 0L)
                    .compose(mSchedulersTransformer)
                    .subscribe(new BaseSubscribeForV2<InfoCommentBean>() {
                        @Override
                        protected void onSuccess(InfoCommentBean data) {
                            List<InfoCommentListBean> newList = dealComment(data, maxId);
                            mRootView.onNetResponseSuccess(newList, isLoadMore);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            handleInfoHasBeDeleted(code);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
            addSubscrebe(subscribe);
        }

    }

    private void handleInfoHasBeDeleted(int code) {
        if (code == ErrorCodeConfig.DATA_HAS_BE_DELETED) {
            mInfoListBeanGreenDao.deleteInfo(mRootView.getCurrentInfo());
            EventBus.getDefault().post(mRootView.getCurrentInfo(), EVENT_SEND_INFO_LIST_DELETE_UPDATE);
            mRootView.infoMationHasBeDeleted();
        } else {
            mRootView.loadAllError();
        }
    }

    @Override
    public void shareInfo(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        //shareContent.setTitle(mContext.getString(R.string.app_name_info, mContext.getString(R.string.app_name)));
        shareContent.setTitle(mRootView.getCurrentInfo().getTitle());
        shareContent.setUrl(String.format(Locale.getDefault(), APP_DOMAIN + APP_PATH_INFO_DETAILS_FORMAT,
                mRootView.getCurrentInfo().getId()));
        //shareContent.setContent(mRootView.getCurrentInfo().getTitle());
        shareContent.setContent(mRootView.getCurrentInfo().getSubject());

        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        } else {
            shareContent.setBitmap(bitmap);
        }

        if (mRootView.getCurrentInfo().getImage() != null) {
            shareContent.setImage(ImageUtils.imagePathConvertV2(mRootView.getCurrentInfo()
                            .getImage().getId()
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_home)
                    , ImageZipConfig.IMAGE_70_ZIP));
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
        mRootView.getCurrentInfo().setHas_collect(isUnCollected);

        if (mRootView.getInfoType() == -100) {
            //return;// 搜索出来的资讯，收藏状态有待优化  已处理
        }
        mRootView.setCollect(isUnCollected);
        mInfoListBeanGreenDao.updateInfo(mRootView.getCurrentInfo());
        EventBus.getDefault().post(mRootView.getCurrentInfo(), EVENT_SEND_INFO_LIST_COLLECT);
        mBaseInfoRepository.handleCollect(isUnCollected, news_id);
    }

    @Override
    public void handleLike(boolean isLiked, String news_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao
                .getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
        InfoDigListBean digListBean = new InfoDigListBean();
        digListBean.setUser_id(userInfoBean.getUser_id());
        digListBean.setId(System.currentTimeMillis());
        digListBean.setDiggUserInfo(userInfoBean);
        if (mRootView.getCurrentInfo().getDigList() == null) {
            mRootView.getCurrentInfo().setDigList(new ArrayList<>());
        }
        if (isLiked) {
            mRootView.getCurrentInfo().getDigList().add(0, digListBean); // 放到第一个
            mRootView.getCurrentInfo().setDigg_count(mRootView.getCurrentInfo().getDigg_count() + 1);
        } else {
            for (InfoDigListBean infoDigListBean : mRootView.getCurrentInfo().getDigList()) {
                if (infoDigListBean.getUser_id().equals(userInfoBean.getUser_id())) {
                    mRootView.getCurrentInfo().getDigList().remove(infoDigListBean);
                    mRootView.getCurrentInfo().setDigg_count(mRootView.getCurrentInfo().getDigg_count() - 1);
                    break;
                }
            }
        }
        mRootView.getCurrentInfo().setHas_like(isLiked);
        mRootView.setDigg(isLiked);
        if (mRootView.getInfoType() == -100) {
            //return;// 搜索出来的资讯，收藏状态有待优化  已处理
        }
        mInfoListBeanGreenDao.updateInfo(mRootView.getCurrentInfo());
        mBaseInfoRepository.handleLike(isLiked, news_id);
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
    public void reqReWardsData(int id) {
        Subscription subscribe = Observable.zip(mBaseRewardRepository.getRewardCount(id), mBaseRewardRepository.rewardInfoList(id
                , TSListFragment.DEFAULT_PAGE_SIZE, null, null, null)
                , (Func2<RewardsCountBean, List<RewardsListBean>, Object>) (rewardsCountBean, rewardsListBeen) -> {

                    mRootView.updateReWardsView(rewardsCountBean, rewardsListBeen);
                    return rewardsCountBean;
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);
    }

    @Override
    public void getInfoDetail(String news_id) {
        Subscription subscription = Observable.zip(mBaseInfoRepository.getInfoDetail(news_id),
                mBaseInfoRepository.getInfoDigListV2(news_id, 0L),
                mBaseInfoRepository.getRelateInfoList(news_id),
                mBaseInfoRepository.getInfoCommentListV2(news_id, 0L, 0L),
                (infoListDataBean, infoDigListBeen, infoRelateBean, infoCommentBean) -> {
                    infoListDataBean.setDigList(infoDigListBeen);
                    infoListDataBean.setRelateInfoList(infoRelateBean);
                    infoListDataBean.setCommentList(dealComment(infoCommentBean, 0));
                    return infoListDataBean;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<InfoListDataBean>() {
                    @Override
                    protected void onSuccess(InfoListDataBean data) {
                        mRootView.updateInfoHeader(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }
                });

        addSubscrebe(subscription);
    }

    private List<InfoCommentListBean> dealComment(InfoCommentBean infoCommentBean, long max_id) {
        List<InfoCommentListBean> all = new ArrayList<>();
        if (max_id == 0) {
            if (infoCommentBean.getPinneds() != null) {
                for (InfoCommentListBean commentListBean : infoCommentBean.getPinneds()) {
                    commentListBean.setPinned(true);
                }
                mInfoCommentListBeanDao.saveMultiData(infoCommentBean.getPinneds());
                all.addAll(infoCommentBean.getPinneds());
            }
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
                all.addAll(localComment);
            }
        }
        if (infoCommentBean.getComments() != null) {
            mInfoCommentListBeanDao.saveMultiData(infoCommentBean.getComments());
            all.addAll(infoCommentBean.getComments());
        }
        return all;
    }

    @Override
    public void deleteInfo() {
        mRootView.deleteInfo(true, false, "");
        Subscription subscription = mBaseInfoRepository.deleteInfo(String.valueOf(mRootView.getCurrentInfo().getCategory().getId()),
                String.valueOf(mRootView.getNewsId()))
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mInfoListBeanGreenDao.deleteInfo(mRootView.getCurrentInfo());
                        mRootView.deleteInfo(false, true, data.getMessage().get(0));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.deleteInfo(false, false, message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.deleteInfo(false, false, throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<RealAdvertListBean> getAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getInfoDetailAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getInfoDetailAdvert().getMRealAdvertListBeen();
    }

    @Override
    public void deleteComment(InfoCommentListBean data) {
        mInfoCommentListBeanDao.deleteSingleCache(data);
        mRootView.getListDatas().remove(data);
        mRootView.getCurrentInfo().setComment_count(mRootView.getCurrentInfo().getComment_count() - 1);
        if (mRootView.getListDatas().size() == 0) {// 占位
            InfoCommentListBean emptyData = new InfoCommentListBean();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        mBaseInfoRepository.deleteComment(mRootView.getNewsId().intValue(), data.getId().intValue());
    }

    /**
     * 处理发送动态数据
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_INFO_LIST)
    public void handleSendComment(InfoCommentListBean infoCommentListBean) {
        LogUtils.d(TAG, "dynamicCommentBean = " + infoCommentListBean.toString());
        mInfoCommentListBeanDao.insertOrReplace(infoCommentListBean);
        Subscription subscribe = Observable.just(infoCommentListBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(infoCommentListBean1 -> {
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
                })
                .subscribe(integer -> {
                    if (integer > 0) {
                        mRootView.refreshData(); // 加上 header
                    }

                }, throwable -> throwable.printStackTrace());
        addSubscrebe(subscribe);

    }

    @Override
    public void sendComment(int reply_id, String content) {
        InfoCommentListBean createComment = new InfoCommentListBean();

        createComment.setInfo_id(mRootView.getNewsId().intValue());

        createComment.setState(SEND_ING);

        createComment.setComment_content(content);

        createComment.setReply_to_user_id(reply_id);

        createComment.setId(-1L);

        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        createComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        if (reply_id == 0) {// 评论资讯
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(0L);
            createComment.setToUserInfoBean(userInfoBean);
        } else {
            createComment.setToUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                    (long) reply_id));
        }
        createComment.setFromUserInfoBean(mUserInfoBeanGreenDao.getSingleDataFromCache(
                AppApplication.getmCurrentLoginAuth().getUser_id()));
        //mInfoCommentListBeanDao.insertOrReplace(createComment);
//        if (mRootView.getListDatas().get(0).getComment_content() == null) {
//            mRootView.getListDatas().remove(0);// 去掉占位图
//        }
//        mRootView.getListDatas().add(0, createComment);
//        mRootView.getCurrentInfo().setComment_count(mRootView.getCurrentInfo().getComment_count() + 1);
//        mRootView.refreshData();
        mBaseInfoRepository.sendComment(content, mRootView.getNewsId(), reply_id,
                createComment.getComment_mark());
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_has_send_wait_review));

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
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoCommentListBean> data, boolean isLoadMore) {
        return false;
    }
}
