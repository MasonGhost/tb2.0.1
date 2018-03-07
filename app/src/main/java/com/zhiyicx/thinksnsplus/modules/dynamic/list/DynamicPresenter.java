package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;

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
import com.zhiyicx.thinksnsplus.data.beans.PurChasesBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SendDynamicDataBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.TopDynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareFragment;
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
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean.TYPE_HOT;
import static com.zhiyicx.thinksnsplus.data.beans.TopDynamicBean.TYPE_NEW;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicPresenter extends AppBasePresenter<DynamicContract.View>
        implements DynamicContract.Presenter, OnShareCallbackListener {


    private DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;
    private DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    private SendDynamicDataBeanV2GreenDaoImpl mSendDynamicDataBeanV2GreenDao;
    private TopDynamicBeanGreenDaoImpl mTopDynamicBeanGreenDao;

    private SharePolicy mSharePolicy;
    private AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;
    private BaseDynamicRepository mDynamicRepository;
    private UserInfoRepository mUserInfoRepository;

    private SparseArray<Long> msendingStatus = new SparseArray<>();

    @Inject
    public DynamicPresenter(DynamicContract.View rootView
            , AllAdvertListBeanGreenDaoImpl allAdvertListBeanGreenDao
            , DynamicDetailBeanV2GreenDaoImpl dynamicDetailBeanV2GreenDao
            , DynamicCommentBeanGreenDaoImpl dynamicCommentBeanGreenDao
            , SendDynamicDataBeanV2GreenDaoImpl sendDynamicDataBeanV2GreenDao
            , TopDynamicBeanGreenDaoImpl topDynamicBeanGreenDao
            , BaseDynamicRepository baseDynamicRepository
            , UserInfoRepository userInfoRepository
    ) {
        super(rootView);
        mAllAdvertListBeanGreenDao = allAdvertListBeanGreenDao;
        mDynamicDetailBeanV2GreenDao = dynamicDetailBeanV2GreenDao;
        mDynamicCommentBeanGreenDao = dynamicCommentBeanGreenDao;
        mSendDynamicDataBeanV2GreenDao = sendDynamicDataBeanV2GreenDao;
        mTopDynamicBeanGreenDao = topDynamicBeanGreenDao;
        if (rootView instanceof Fragment) {
            mSharePolicy = new UmengSharePolicyImpl(((Fragment) rootView).getActivity());
        }
        mDynamicRepository = baseDynamicRepository;
        mUserInfoRepository = userInfoRepository;
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * @param maxId      当前获取到数据的最大 id
     * @param isLoadMore 加载状态
     */
    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        if (mRootView.getMcurrentUser() != null) {

            Subscription subscription = mDynamicRepository.getDynamicListForSomeone(mRootView.getMcurrentUser().getUser_id(), maxId, mRootView
                    .getDynamicType())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(dynamicDetailBeanV2s -> {
                        List<DynamicDetailBeanV2> result = new ArrayList<>();

                        for (DynamicDetailBeanV2 detailBeanV2 : dynamicDetailBeanV2s) {
                            if (detailBeanV2.getUser_id().intValue() == mRootView.getMcurrentUser().getUser_id()) {
                                result.add(detailBeanV2);
                            }
                        }
                        return result;
                    })
                    .map(listBaseJson -> {
                        // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                        if (!isLoadMore && AppApplication.getmCurrentLoginAuth().getUser_id() == mRootView.getMcurrentUser().getUser_id()) {
                            List<DynamicDetailBeanV2> data = getDynamicBeenFromDBV2();
                            try {
                                //修改动态条数，把自己正在发布发加上
//                                mRootView.updateDynamicCounts(data.size());
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
//                            mInterfaceNum++;
                            mRootView.onNetResponseSuccess(data, isLoadMore);
//                            allready();
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

            addSubscrebe(subscription);

            return;
        }


        if (ApiConfig.DYNAMIC_TYPE_EMPTY.equals(mRootView.getDynamicType())) {
            mRootView.onNetResponseSuccess(new ArrayList<>(), isLoadMore);
            return;
        }
        Subscription dynamicLisSub = mDynamicRepository.getDynamicListV2(mRootView.getDynamicType(), maxId, null, isLoadMore, null)
                .observeOn(Schedulers.io())
                .map(listBaseJson -> {
                    // 更新数据库
                    insertOrUpdateDynamicDBV2(listBaseJson);
                    // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                    if (!isLoadMore) {
                        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW) || mRootView.getDynamicType().equals((ApiConfig
                                .DYNAMIC_TYPE_FOLLOWS))) {
                            List<DynamicDetailBeanV2> data = getDynamicBeenFromDBV2();
                            data.addAll(listBaseJson);
                        }
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

        addSubscrebe(dynamicLisSub);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
                    mRootView.onCacheResponseSuccess(null, isLoadMore);
//        Subscription subscribe = Observable.just(1)
//                .observeOn(Schedulers.io())
//                .map(aLong -> {
//                    List<DynamicDetailBeanV2> datas;
//                    switch (mRootView.getDynamicType()) {
//                        case ApiConfig.DYNAMIC_TYPE_FOLLOWS:
//                            if (!isLoadMore) {
//                                datas = getDynamicBeenFromDBV2();
//                                datas.addAll(mDynamicDetailBeanV2GreenDao.getFollowedDynamicList(maxId));
//                            } else {
//                                datas = mDynamicDetailBeanV2GreenDao.getFollowedDynamicList(maxId);
//                            }
//                            break;
//                        case ApiConfig.DYNAMIC_TYPE_HOTS:
//                            datas = mDynamicDetailBeanV2GreenDao.getHotDynamicList(maxId);
//                            List<DynamicDetailBeanV2> topHotDynamics = mTopDynamicBeanGreenDao.getTopDynamicByType(TYPE_HOT);
//                            if (topHotDynamics != null) {
//                                datas.addAll(0, topHotDynamics);
//                            }
//                            break;
//                        case ApiConfig.DYNAMIC_TYPE_NEW:
//                            // 刷新
//                            if (!isLoadMore) {
//                                datas = getDynamicBeenFromDBV2();
//                                datas.addAll(mDynamicDetailBeanV2GreenDao.getNewestDynamicList(maxId));
//                                List<DynamicDetailBeanV2> topNewDynamics = mTopDynamicBeanGreenDao.getTopDynamicByType(TYPE_NEW);
//                                if (topNewDynamics != null) {
//                                    datas.addAll(0, topNewDynamics);
//                                }
//                            } else {
//                                datas = mDynamicDetailBeanV2GreenDao.getNewestDynamicList(maxId);
//                            }
//                            break;
//                        case ApiConfig.DYNAMIC_TYPE_MY_COLLECTION:
//                            datas = mDynamicDetailBeanV2GreenDao.getMyCollectDynamic();
//                            break;
//                        default:
//                            datas = new ArrayList<>();
//                    }
//                    for (int i = 0; i < datas.size(); i++) {
//                        // 处理友好显示数据
//                        datas.get(i).handleData();
//                        if (datas.get(i).getFeed_mark() != null) {
//                            datas.get(i).setComments(mDynamicCommentBeanGreenDao.getLocalComments(datas.get(i).getFeed_mark()));
//                        }
//                    }
//                    return datas;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(dynamicDetailBeanV2s -> {
//                    mRootView.onCacheResponseSuccess(dynamicDetailBeanV2s, isLoadMore);
//                }, Throwable::printStackTrace);
//        addSubscrebe(subscribe);


    }

    private void insertOrUpdateDynamicDBV2(@NotNull List<DynamicDetailBeanV2> data) {
        Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(dynamicDetailBeanV2s -> mDynamicRepository.updateOrInsertDynamicV2(data, mRootView.getDynamicType()),
                        Throwable::printStackTrace);
    }

    /**
     * 此处需要先存入数据库，方便处理动态的状态，故此处不需要再次更新数据库
     *
     * @param data
     * @param isLoadMore
     * @return
     */
    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        return true;
    }

    /**
     * 列表中是否有了
     *
     * @param dynamicBean
     * @return
     */
    private int hasDynamicContanied(DynamicDetailBeanV2 dynamicBean) {
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                mRootView.getListDatas().get(i).setState(dynamicBean.getState());
                mRootView.getListDatas().get(i).setId(dynamicBean.getId());
                mRootView.getListDatas().get(i).setId(dynamicBean.getId());
                return i;
            }
        }
        return -1;
    }

    @NonNull
    private List<DynamicDetailBeanV2> getDynamicBeenFromDBV2() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        List<DynamicDetailBeanV2> datas = mDynamicDetailBeanV2GreenDao.getMySendingUnSuccessDynamic(AppApplication.getMyUserIdWithdefault());

        msendingStatus.clear();
        for (int i = 0; i < datas.size(); i++) {
            // 第一次加载的时候将自己没有发送成功的动态状态修改为失败
            if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {
                datas.get(i).setState(DynamicDetailBeanV2.SEND_ERROR);
            }
            msendingStatus.put(i, datas.get(i).getFeed_mark());
        }
        // 第一次加载的时候将自己没有发送成功的动态状态修改为失败
        if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {
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
        mDynamicRepository.handleLike(isLiked, feed_id);
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
        mDynamicRepository.deleteCommentV2(dynamicBean.getId(), comment_id);
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mDynamicRepository.sendCommentV2(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(),
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
        mRootView.refreshData();
        if (dynamicBean.getId() != null && dynamicBean.getId() != 0) {
            mDynamicRepository.deleteDynamic(dynamicBean.getId());
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
        //当回复动态的时候
        if (replyToUserId == 0) {
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
        mDynamicRepository.sendCommentV2(commentContent, mRootView.getListDatas().get(mCurrentPostion)
                .getId(), replyToUserId, creatComment.getComment_mark());
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
            if (mRootView.getListDatas().get(i).getFeed_mark() != null && feedMark == mRootView.getListDatas().get(i).getFeed_mark()) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public void handleCollect(DynamicDetailBeanV2 dynamicBean) {
        // 收藏
        // 修改数据
        // 旧状态
        boolean is_collection = dynamicBean.isHas_collect();
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
        if (mSharePolicy == null) {
            if (mRootView instanceof Fragment) {
                mSharePolicy = new UmengSharePolicyImpl(((Fragment) mRootView).getActivity());
            } else {
                return;
            }
        }
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
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getDynamicBannerAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getDynamicBannerAdvert().getMRealAdvertListBeen();
    }

    @Override
    public List<RealAdvertListBean> getListAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getDynamicListAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getDynamicListAdvert().getMRealAdvertListBeen();
    }

    @Override
    public void checkNote(int note) {
        mCommentRepository.checkNote(note)
                .subscribe(new BaseSubscribeForV2<PurChasesBean>() {
                    @Override
                    protected void onSuccess(PurChasesBean data) {

                    }
                });
    }

    @Override
    public void payNote(final int dynamicPosition, final int imagePosition, int note, final boolean isImage) {
        if (handleTouristControl()) {
            return;
        }

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
                    return mDynamicRepository.getDynamicDetailBeanV2(mRootView.getListDatas().get(dynamicPosition).getId())
                            .flatMap(detailBeanV2 -> {
                                stringBaseJsonV2.setData(detailBeanV2.getFeed_content());
                                return Observable.just(stringBaseJsonV2);
                            });
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.hideCenterLoading();
                        mRootView.paySuccess();
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

                        Bundle bundle = new Bundle();
                        DynamicDetailBeanV2 dynamicDetailBeanV2 = mRootView.getListDatas().get(dynamicPosition);

                        bundle.putParcelable(DYNAMIC_DETAIL_DATA, dynamicDetailBeanV2);
                        bundle.putBoolean(DYNAMIC_LIST_NEED_REFRESH, true);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_DYNAMIC);
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

    @Override
    public void followUser(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);
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

    /**
     * 处理发送评论数据
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
                        if (dynamicCommentBean1.getFeed_mark().equals(mRootView.getListDatas().get(i).getFeed_mark())) {
                            dynamicPosition = i;
                            break;
                        }
                    }// 如果列表有当前评论
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
     * 处理发送动态数据
     *
     * @param dynamicBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST)
    public void handleSendDynamic(DynamicDetailBeanV2 dynamicBean) {

        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)
                || mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {
            Subscription subscribe = Observable.just(dynamicBean)
                    .observeOn(Schedulers.computation())
                    .map(dynamicDetailBeanV2 -> {
                        int position = -1;
                        position = hasDynamicContanied(dynamicBean);
                        return position;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(position -> {
                        // 如果列表有当前数据
                        if (position != -1) {
                            mRootView.showNewDynamic(position);
                        } else {
                            List<DynamicDetailBeanV2> temps = new ArrayList<>();
                            temps.add(dynamicBean);
                            temps.addAll(mRootView.getListDatas());
                            mRootView.getListDatas().clear();
                            mRootView.getListDatas().addAll(temps);
                            temps.clear();
                            mRootView.showNewDynamic(-1);
                        }
                    }, Throwable::printStackTrace);
            addSubscrebe(subscribe);

        }
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
                .observeOn(Schedulers.computation())
                .map(bundle -> {
                    boolean isNeedRefresh = bundle.getBoolean(DYNAMIC_LIST_NEED_REFRESH);
                    int dynamicPosition = -1;
                    if (isNeedRefresh) {
                        DynamicDetailBeanV2 dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                        dynamicPosition = mRootView.getListDatas().indexOf(dynamicBean);
                        // 如果列表有当前评论
                        if (dynamicPosition != -1) {
                            mRootView.getListDatas().set(dynamicPosition, dynamicBean);
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

}