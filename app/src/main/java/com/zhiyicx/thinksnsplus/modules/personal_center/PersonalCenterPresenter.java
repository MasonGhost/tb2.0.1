package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
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
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SendDynamicDataBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUploadRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_UPDATE_TOLL;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class PersonalCenterPresenter extends AppBasePresenter<PersonalCenterContract.View> implements
        PersonalCenterContract.Presenter, OnShareCallbackListener {
    private static final int NEED_INTERFACE_NUM = 2;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    @Inject
    UpLoadRepository mIUploadRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    SendDynamicDataBeanV2GreenDaoImpl mSendDynamicDataBeanV2GreenDao;
    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;
    @Inject
    public SharePolicy mSharePolicy;
    /**
     * 纪录请求接口数量，用于统计接口是否全部请求完成，需要接口全部请求完成后在显示界面
     */
    private int mInterfaceNum = 0;
    SparseArray<Long> msendingStatus = new SparseArray<>();

    @Inject
    public PersonalCenterPresenter(PersonalCenterContract.View rootView) {
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
    public void requestNetData(Long maxId, final boolean isLoadMore, final long user_id) {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        Subscription subscription = mBaseDynamicRepository.getDynamicListForSomeone(user_id, maxId, mRootView.getDynamicType())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(dynamicDetailBeanV2s -> {
                    List<DynamicDetailBeanV2> result = new ArrayList<>();

                    for (DynamicDetailBeanV2 detailBeanV2 : dynamicDetailBeanV2s) {
                        if (detailBeanV2.getUser_id() == user_id) {
                            result.add(detailBeanV2);
                        }
                    }
                    return result;
                })
                .map(listBaseJson -> {
                    // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                    if (!isLoadMore && AppApplication.getmCurrentLoginAuth().getUser_id() == user_id) {
                        List<DynamicDetailBeanV2> data = getDynamicBeenFromDBV2();
                        try {
                            //修改动态条数，把自己正在发布发加上
                            mRootView.updateDynamicCounts(data.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        data.addAll(listBaseJson);
                    }
                    // 把自己发的评论加到评论列表的前面
                    for (int i = 0; i < listBaseJson.size(); i++) {
                        // 处理友好显示数据
                        listBaseJson.get(i).handleData();
                        List<DynamicCommentBean> dynamicCommentBeen = mDynamicCommentBeanGreenDao.getMySendingComment(listBaseJson.get(i)
                                .getFeed_mark());
                        if (!dynamicCommentBeen.isEmpty()) {
                            dynamicCommentBeen.addAll(listBaseJson.get(i).getComments());
                            listBaseJson.get(i).getComments().clear();
                            listBaseJson.get(i).getComments().addAll(dynamicCommentBeen);
                        }
                    }

                    return listBaseJson;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<DynamicDetailBeanV2>>() {
                    @Override
                    protected void onSuccess(List<DynamicDetailBeanV2> data) {
                        mInterfaceNum++;
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                        allready();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (mInterfaceNum >= NEED_INTERFACE_NUM) {
                            mRootView.showMessage(message);
                        } else {
                            mRootView.loadAllError();
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        if (mInterfaceNum >= NEED_INTERFACE_NUM) {
                            mRootView.onResponseError(throwable, isLoadMore);
                        } else {
                            mRootView.loadAllError();
                        }
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public List<DynamicDetailBeanV2> requestCacheData(Long maxId, boolean isLoadMore, long userId) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
        // 如下使用缓存，使用下面
///        List<DynamicDetailBeanV2> myDynamics = mDynamicDetailBeanV2GreenDao.getMyDynamics(userId);
//        mRootView.onCacheResponseSuccess(null, isLoadMore);
        return null;
    }

    @Override
    public void handleFollow(UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
        // ui进行刷新
        mRootView.setFollowState(followFansBean);
    }

    @Override
    public void uploadUserCover(String filePath, UserInfoBean userInfoBean) {
        Subscription subscription = mIUploadRepository.uploadBg(filePath)
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.setUpLoadCoverState(true);
                        // 将本地图片路径作为storageId保存到数据库
                        userInfoBean.setCover(filePath);
                        // 更新用户数据库
                        mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.setUpLoadCoverState(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.setUpLoadCoverState(false);
                        LogUtils.e(throwable, "result");
                    }
                });
        addSubscrebe(subscription);
    }


    @Override
    public void shareUserInfo(UserInfoBean userInfoBean) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(userInfoBean.getName());
        shareContent.setContent(TextUtils.isEmpty(userInfoBean.getIntro()) ? mContext.getString(R.string.intro_default) : userInfoBean.getIntro());
        if (null != mRootView.getUserHeadPic()) {
            shareContent.setBitmap(mRootView.getUserHeadPic());
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_USERINFO, userInfoBean.getUser_id()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public List<ChatUserInfoBean> getChatUserList(UserInfoBean userInfoBean) {
        List<ChatUserInfoBean> list = new ArrayList<>();
        list.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
        list.add(getChatUser(userInfoBean));
        return list;
    }

    private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean){
        ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
        chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
        chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
        chatUserInfoBean.setName(userInfoBean.getName());
        chatUserInfoBean.setSex(userInfoBean.getSex());
        if (userInfoBean.getVerified() != null){
            ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
            verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
            verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
            verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
            verifiedBean.setType(userInfoBean.getVerified().getType());
            chatUserInfoBean.setVerified(verifiedBean);
        }
        return chatUserInfoBean;
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
//        mBaseDynamicRepository.updateOrInsertDynamicV2(data, ApiConfig.DYNAMIC_TYPE_NEW);
        return true;
    }

    @Override
    public void setCurrentUserInfo(Long userId) {
        if (AppApplication.getmCurrentLoginAuth() == null || userId == null) {
            return;
        }
        List<Object> userids = new ArrayList<>();
        userids.add(userId);
        Subscription subscription = Observable.zip(mUserInfoRepository.getUserTags(userId),
                AppApplication.getMyUserIdWithdefault() == userId ? mUserInfoRepository.getCurrentLoginUserInfo()
                        : mUserInfoRepository.getUserInfo(userids)
                        .map(userInfoBeen -> userInfoBeen.get(0)), (userTagBeanList, userInfoBean) -> {
                    userInfoBean.setTags(userTagBeanList);
                    return userInfoBean;
                })
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        mInterfaceNum++;
                        mRootView.setHeaderInfo(data);
                        allready();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.loadAllError();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.loadAllError();
                    }
                });
        addSubscrebe(subscription);
    }

    private void allready() {
        if (mInterfaceNum == NEED_INTERFACE_NUM) {
            mRootView.allDataReady();
            mInterfaceNum = 0;
        }
    }

    @NonNull
    private List<DynamicDetailBeanV2> getDynamicBeenFromDBV2() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        List<DynamicDetailBeanV2> datas = mDynamicDetailBeanV2GreenDao.getMySendingUnSuccessDynamic(AppApplication.getmCurrentLoginAuth()
                .getUser_id());
        msendingStatus.clear();
        for (int i = 0; i < datas.size(); i++) {
            if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
                datas.get(i).setState(DynamicDetailBeanV2.SEND_ERROR);
            }
            msendingStatus.put(i, datas.get(i).getFeed_mark());
        }
        if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
            mDynamicDetailBeanV2GreenDao.insertOrReplace(datas);
        }

        return datas;
    }


    /**
     * handle like or cancle like in background
     *
     * @param isLiked true,do like ,or  cancle like
     * @param feed_id dynamic id
     * @param postion current item position
     */
    @Override
    public void handleLike(boolean isLiked, final Long feed_id, final int postion) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(postion));
        mBaseDynamicRepository.handleLike(isLiked, feed_id);

    }

    @Override
    public void handleViewCount(Long feed_id, int position) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mRootView.getListDatas().get(position).setFeed_view_count(mRootView.getListDatas().get(position).getFeed_view_count() + 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(position));
    }

    @Override
    public void reSendDynamic(int position) {
        // 将动态信息存入数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(position));
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC_V2);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", mRootView.getListDatas().get(position).getFeed_mark());
        params.put("sendDynamicDataBean", mSendDynamicDataBeanV2GreenDao.getSendDynamicDataBeanV2ByFeedMark
                (String.valueOf(mRootView.getListDatas().get(position).getFeed_mark())));
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteCommentV2(DynamicDetailBeanV2 dynamicBean, int dynamicPositon, long comment_id, int commentPosition) {
        mRootView.getListDatas().get(dynamicPositon).setFeed_comment_count(dynamicBean.getFeed_comment_count() - 1);
        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPositon));
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicBean.getComments().get(commentPosition));
        mRootView.getListDatas().get(dynamicPositon).getComments().remove(commentPosition);
        mRootView.refreshData(dynamicPositon);
        mBaseDynamicRepository.deleteCommentV2(dynamicBean.getId(), comment_id);
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mBaseDynamicRepository.sendCommentV2(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(),
                commentBean.getComment_mark());
        mRootView.refreshData();
    }

    @Override
    public void deleteDynamic(DynamicDetailBeanV2 dynamicBean, int position) {
        if (position == -1) {
            return;
        }
        mDynamicDetailBeanV2GreenDao.deleteSingleCache(dynamicBean);
        mRootView.getListDatas().remove(position);
        if (mRootView.getListDatas().isEmpty()) {
            // 增加空数据，用于显示占位图
            DynamicDetailBeanV2 emptyData = new DynamicDetailBeanV2();
            mRootView.getListDatas().add(emptyData);
        }
        mRootView.refreshData();
        if (dynamicBean.getId() != null && dynamicBean.getId() != 0) {
            mBaseDynamicRepository.deleteDynamic(dynamicBean.getId());
        }


    }

    @Override
    public void sendCommentV2(int mCurrentPostion, long replyToUserId, String commentContent) {
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mRootView.getListDatas().get(mCurrentPostion).getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        List<DynamicCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getComments());
        mRootView.getListDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).setFeed_comment_count(mRootView.getListDatas().get(mCurrentPostion).getFeed_comment_count() +
                1);
        mRootView.refreshData();

        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(mCurrentPostion));
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        mBaseDynamicRepository.sendCommentV2(commentContent, mRootView.getListDatas().get(mCurrentPostion)
                .getId(), replyToUserId, creatComment.getComment_mark());
    }

    @Override
    public void handleCollect(DynamicDetailBeanV2 dynamicBean) {
        // 收藏
        // 修改数据
        boolean is_collection = dynamicBean.isHas_collect();// 旧状态
        dynamicBean.setHas_collect(!is_collection);
        boolean newCollectState = !is_collection;
        // 更新数据库
        mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", dynamicBean.getId());
        // 后台处理
        if (newCollectState) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE_V2, params);
            backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_UNCOLLECT_V2_FORMAT,
                    dynamicBean.getId()));
        }
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getFeed_content()) ? mContext.getString(R.string
                .share_dynamic) : dynamicBean.getFeed_content());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getId()
                == null ? "" : dynamicBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public List<RealAdvertListBean> getBannerAdvert() {
        return null;
    }

    @Override
    public List<RealAdvertListBean> getListAdvert() {
        return null;
    }

    @Override
    public void checkNote(int note) {

    }

    @Override
    public void payNote(int dynamicPosition, int imagePosition, int note, boolean isImage) {

        double amount;
        if (isImage) {
            amount = mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition).getAmount();
        } else {
            amount = mRootView.getListDatas().get(dynamicPosition).getPaid_node().getAmount();
        }

        Subscription subscribe = handleIntegrationBlance((long) amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.transaction_doing)))
                .flatMap(o -> mCommentRepository.paykNote(note))
                .flatMap(stringBaseJsonV2 -> {
                    if (isImage) {
                        return Observable.just(stringBaseJsonV2);
                    }
                    return mBaseDynamicRepository.getDynamicDetailBeanV2(mRootView.getListDatas().get(dynamicPosition).getId())
                            .flatMap(detailBeanV2 -> {
                                stringBaseJsonV2.setData(detailBeanV2.getFeed_content());
                                return Observable.just(stringBaseJsonV2);
                            });
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.hideCenterLoading();
                        if (isImage) {
                            DynamicDetailBeanV2.ImagesBean imageBean = mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition);
                            imageBean.setPaid(true);
                            imageBean.setGlideUrl(ImageUtils.imagePathConvertV2(true, imageBean.getFile(), imageBean.getCurrentWith(), imageBean
                                            .getCurrentWith(),
                                    imageBean.getPropPart(), AppApplication.getTOKEN()));
                        } else {
                            mRootView.getListDatas().get(dynamicPosition).getPaid_node().setPaid(true);
                            mRootView.getListDatas().get(dynamicPosition).setFeed_content(data.getData());
                            if (data.getData() != null) {
                                String friendlyContent = data.getData().replaceAll(MarkdownConfig.NETSITE_FORMAT, MarkdownConfig.LINK_EMOJI + Link
                                        .DEFAULT_NET_SITE);
                                if (friendlyContent.length() > DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE) {
                                    friendlyContent = friendlyContent.substring(0, DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE) + "...";
                                }
                                mRootView.getListDatas().get(dynamicPosition).setFriendlyContent(friendlyContent);
                            }
                        }
                        mRootView.refreshData(dynamicPosition);
                        mDynamicDetailBeanV2GreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPosition));
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
        addSubscrebe(subscribe);

    }

    /**
     * 通过 feedMark 获取当前数据的位置
     *
     * @param feedMark
     * @return
     */
    @Override
    public int getCurrenPosiotnInDataList(long feedMark) {
        int position = -1;
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (feedMark == mRootView.getListDatas().get(i).getFeed_mark()) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {
        Subscription subscribe = Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicCommentBean1.getFeed_mark())) {
                            dynamicPosition = i;
                            break;
                        }
                    }
                    // 如果列表有当前评论
                    if (dynamicPosition != -1) {
                        int commentSize = mRootView.getListDatas().get(dynamicPosition).getComments().size();
                        for (int i = 0; i < commentSize; i++) {
                            if (mRootView.getListDatas().get(dynamicPosition).getComments().get(i).getFeed_mark().equals
                                    (dynamicCommentBean1.getFeed_mark())) {
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setState(dynamicCommentBean1.getState());
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_id(dynamicCommentBean1.getComment_id());
                                mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_mark
                                        (dynamicCommentBean1.getComment_mark());
                                break;
                            }
                        }
                    }
                    return dynamicPosition;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);

    }

    /**
     * 动态详情处理了数据
     * 处理更新动态数据
     *
     * @param data
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_DYNAMIC)
    public void updateDynamic(Bundle data) {
        Subscription subscribe = Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(bundle -> {
                    boolean isNeedRefresh = bundle.getBoolean(DYNAMIC_LIST_NEED_REFRESH);
                    int dynamicPosition=-1;
                    if (isNeedRefresh){
                        DynamicDetailBeanV2 dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                        int position = bundle.getInt(DYNAMIC_DETAIL_DATA_POSITION);
                        // 是否是更新收费信息
                        if (bundle.getBoolean(DYNAMIC_UPDATE_TOLL)) {
                            position = mRootView.getListDatas().indexOf(dynamicBean);
                        }

                        int size = mRootView.getListDatas().size();
                        dynamicPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                                dynamicPosition = i;
                                break;
                            }
                        }
                        // 如果列表有当前评论
                        if (dynamicPosition != -1) {
                            mRootView.getListDatas().get(position).setImages(dynamicBean.getImages());
                        }
                    }
                    return isNeedRefresh ? dynamicPosition : -1;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);
    }

    @Subscriber(tag = EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE)
    public void deleteDynamic(DynamicDetailBeanV2 dynamicBean) {
        deleteDynamic(dynamicBean, mRootView.getListDatas().indexOf(dynamicBean));
        LogUtils.d(EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE);
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
}
