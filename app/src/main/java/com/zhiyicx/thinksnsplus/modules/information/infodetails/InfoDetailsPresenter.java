package com.zhiyicx.thinksnsplus.modules.information.infodetails;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.TimeUtils;
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
        InfoDetailsConstract.View> implements InfoDetailsConstract.Presenter {

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
    public void shareInfo() {
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentInfo().getTitle());
        shareContent.setUrl(String.format(APP_DOMAIN + APP_PATH_INFO_DETAILS_FORMAT,
                mRootView.getCurrentInfo().getId()));
        if (mRootView.getCurrentInfo().getStorage()!=null){
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
        mInfoListBeanGreenDao.saveCollect(mRootView.getInfoType(), mRootView.getNewsId().intValue
                (), is_collection_news);
        EventBus.getDefault().post(mRootView.getCurrentInfo(), EVENT_SEND_INFO_LIST_COLLECT);
        mRepository.handleCollect(isUnCollected, news_id);
    }

    @Override
    public void deleteComment(InfoCommentListBean data) {
        mInfoCommentListBeanDao.deleteSingleCache(data);
        mRootView.getListDatas().remove(data);
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
        mRootView.getListDatas().add(1, createComment);
        mRootView.refreshData();
        mRepository.sendComment(content, mRootView.getNewsId(), reply_id,
                createComment.getComment_mark());
    }

    @Override
    public List<InfoCommentListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<InfoCommentListBean> data) {
        return false;
    }
}
