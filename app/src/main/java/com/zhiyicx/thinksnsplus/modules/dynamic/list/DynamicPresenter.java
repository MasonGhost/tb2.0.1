package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJson;
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
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicPresenter extends AppBasePresenter<DynamicContract.Repository, DynamicContract.View>
        implements DynamicContract.Presenter, OnShareCallbackListener {

    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicDetailBeanGreenDaoImpl mDynamicDetailBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    DynamicToolBeanGreenDaoImpl mDynamicToolBeanGreenDao;

    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    AuthRepository mAuthRepository;

    @Inject
    public SharePolicy mSharePolicy;

    SparseArray<Long> msendingStatus = new SparseArray<>();

    @Inject
    public DynamicPresenter(DynamicContract.Repository repository, DynamicContract.View rootView) {
        super(repository, rootView);
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
        Subscription dynamicLisSub = mRepository.getDynamicList(mRootView.getDynamicType(), maxId, mRootView.getPage(), "", isLoadMore)
                .map(new Func1<BaseJson<List<DynamicBean>>, BaseJson<List<DynamicBean>>>() {
                    @Override
                    public BaseJson<List<DynamicBean>> call(BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus()) {
                            insertOrUpdateDynamicDB(listBaseJson.getData()); // 更新数据库
                            if (!isLoadMore) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                                if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW) || mRootView.getDynamicType().equals((ApiConfig.DYNAMIC_TYPE_FOLLOWS))) {
                                    List<DynamicBean> data = getDynamicBeenFromDB();
                                    data.addAll(listBaseJson.getData());
                                    listBaseJson.setData(data);
                                }
                            }
                            for (int i = 0; i < listBaseJson.getData().size(); i++) { // 把自己发的评论加到评论列表的前面
                                List<DynamicCommentBean> dynamicCommentBeen = mDynamicCommentBeanGreenDao.getMySendingComment(listBaseJson.getData().get(i).getFeed_mark());
                                if (!dynamicCommentBeen.isEmpty()) {
                                    dynamicCommentBeen.addAll(listBaseJson.getData().get(i).getComments());
                                    listBaseJson.getData().get(i).getComments().clear();
                                    listBaseJson.getData().get(i).getComments().addAll(dynamicCommentBeen);
                                }
                            }

                        }
                        return listBaseJson;
                    }
                })
                .subscribe(new BaseSubscribe<List<DynamicBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicBean> data) {
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
    public List<DynamicBean> requestCacheData(Long maxId, boolean isLoadMore) {
        List<DynamicBean> datas = null;
        switch (mRootView.getDynamicType()) {
            case ApiConfig.DYNAMIC_TYPE_FOLLOWS:
                if (!isLoadMore) {// 刷新
                    datas = getDynamicBeenFromDB();
                    datas.addAll(mDynamicBeanGreenDao.getFollowedDynamicList(maxId));
                } else {
                    datas = mDynamicBeanGreenDao.getFollowedDynamicList(maxId);
                }
                break;
            case ApiConfig.DYNAMIC_TYPE_HOTS:
                datas = mDynamicBeanGreenDao.getHotDynamicList(maxId);
                break;
            case ApiConfig.DYNAMIC_TYPE_NEW:
                if (!isLoadMore) {// 刷新
                    datas = getDynamicBeenFromDB();
                    datas.addAll(mDynamicBeanGreenDao.getNewestDynamicList(maxId));
                } else {
                    datas = mDynamicBeanGreenDao.getNewestDynamicList(maxId);
                }
                break;
            case ApiConfig.DYNAMIC_TYPE_MY_COLLECTION:
                datas = mDynamicBeanGreenDao.getMyCollectDynamic();
                break;
            default:
        }
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getFeed_mark() != null) {
                datas.get(i).setComments(mDynamicCommentBeanGreenDao.getLocalComments(datas.get(i).getFeed_mark()));
            }
        }
        LogUtils.i("requestCacheData DYNAMIC_TYPE_MY_COLLECTION");
        return datas;
    }

    /**
     * 此处需要先存入数据库，方便处理动态的状态，故此处不需要再次更新数据库
     *
     * @param data
     * @param isLoadMore
     * @return
     */
    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data, boolean isLoadMore) {
        return true;
    }

    /**
     * 动态数据库更新
     *
     * @param data
     */

    private void insertOrUpdateDynamicDB(@NotNull List<DynamicBean> data) {
        mRepository.updateOrInsertDynamic(data, mRootView.getDynamicType());
    }

    /**
     * 列表中是否有了
     *
     * @param dynamicBean
     * @return
     */
    private int hasDynamicContanied(DynamicBean dynamicBean) {
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                mRootView.getListDatas().get(i).setState(dynamicBean.getState());
                mRootView.getListDatas().get(i).setFeed_id(dynamicBean.getFeed_id());
                mRootView.getListDatas().get(i).getFeed().setFeed_id(dynamicBean.getFeed_id());
                return i;
            }
        }
        return -1;
    }

    @NonNull
    private List<DynamicBean> getDynamicBeenFromDB() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<DynamicBean>();
        }
        List<DynamicBean> datas = mDynamicBeanGreenDao.getMySendingUnSuccessDynamic((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        msendingStatus.clear();
        for (int i = 0; i < datas.size(); i++) {
            if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
                datas.get(i).setState(DynamicBean.SEND_ERROR);
            }
            msendingStatus.put(i, datas.get(i).getFeed_mark());
        }
        if (mRootView.getListDatas() == null || mRootView.getListDatas().size() == 0) {// 第一次加载的时候将自己没有发送成功的动态状态修改为失败
            mDynamicBeanGreenDao.insertOrReplace(datas);
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
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(postion).getTool());
        mRepository.handleLike(isLiked, feed_id);

    }

    @Override
    public void handleViewCount(Long feed_id, int position) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mRootView.getListDatas().get(position).getTool().setFeed_view_count(mRootView.getListDatas().get(position).getTool().getFeed_view_count() + 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(position).getTool());
        mRepository.handleDynamicViewCount(feed_id);
        mRootView.refreshData();
    }

    @Override
    public void reSendDynamic(int position) {
        // 将动态信息存入数据库
        mDynamicBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(position));
        mDynamicDetailBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(position).getFeed());
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", mRootView.getListDatas().get(position).getFeed_mark());
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(DynamicBean dynamicBean, int dynamicPosition, long comment_id, int commentPositon) {
        mRootView.getListDatas().get(dynamicPosition).getTool().setFeed_comment_count(dynamicBean.getTool().getFeed_comment_count() - 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(dynamicPosition).getTool());
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicBean.getComments().get(commentPositon));
        mRootView.getListDatas().get(dynamicPosition).getComments().remove(commentPositon);
        mRootView.refreshData(dynamicPosition);
        mRepository.deleteComment(dynamicBean.getFeed_id(), comment_id);
    }

    @Override
    public void reSendComment(DynamicCommentBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mRepository.sendComment(commentBean.getComment_content(), feed_id, commentBean.getReply_to_user_id(), commentBean.getComment_mark());
        mRootView.refreshData();
    }

    @Override
    public void deleteDynamic(DynamicBean dynamicBean, int position) {
        mDynamicBeanGreenDao.deleteSingleCache(dynamicBean);
        mRootView.getListDatas().remove(position);
        mRootView.refreshData();
        if (dynamicBean.getFeed_id() != null && dynamicBean.getFeed_id() != 0) {
            mRepository.deleteDynamic(dynamicBean.getFeed_id());
        }
    }

    /**
     * send a commment
     *
     * @param mCurrentPostion current dynamic position
     * @param replyToUserId   comment  to who
     * @param commentContent  comment content
     */
    @Override
    public void sendComment(int mCurrentPostion, long replyToUserId, String commentContent) {
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
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        List<DynamicCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getComments());
        mRootView.getListDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).getTool().setFeed_comment_count(mRootView.getListDatas().get(mCurrentPostion).getTool().getFeed_comment_count() + 1);
        mRootView.refreshData(mCurrentPostion);

        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getListDatas().get(mCurrentPostion).getTool());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendComment(commentContent, mRootView.getListDatas().get(mCurrentPostion).getFeed_id(), replyToUserId, creatComment.getComment_mark());

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

    @Override
    public void handleCollect(DynamicBean dynamicBean) {
        // 收藏
        // 修改数据
        DynamicToolBean collectToolBean = dynamicBean.getTool();
        int is_collection = collectToolBean.getIs_collection_feed();// 旧状态
        // 新状态
        is_collection = is_collection == DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED
                ? DynamicToolBean.STATUS_COLLECT_FEED_CHECKED : DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED;
        collectToolBean.setIs_collection_feed(is_collection);
        boolean newCollectState = is_collection == DynamicToolBean.STATUS_COLLECT_FEED_UNCHECKED ? false : true;
        // 更新数据库
        mDynamicToolBeanGreenDao.insertOrReplace(collectToolBean);
        // 通知服务器
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("feed_id", dynamicBean.getFeed_id());
        // 后台处理
        if (newCollectState) {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
        } else {
            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
        }
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_HANDLE_COLLECT_FORMAT, dynamicBean.getFeed_id()));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
        EventBus.getDefault().post(dynamicBean, EventBusTagConfig.EVENT_COLLECT_DYNAMIC);
    }

    @Override
    public void shareDynamic(DynamicBean dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(TextUtils.isEmpty(dynamicBean.getFeed().getTitle()) ? mContext.getString(R.string.share) : dynamicBean.getFeed().getTitle());
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getFeed().getContent()) ? mContext.getString(R.string.share_dynamic) : dynamicBean.getFeed().getContent());
//        if (dynamicBean.getFeed().getStorages() != null && dynamicBean.getFeed().getStorages().size() > 0) {
//            shareContent.setImage(ImageUtils.imagePathConvert(dynamicBean.getFeed().getStorages().get(0).getStorage_id() + "", 100));
        if(bitmap!=null){
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.icon_256)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getFeed_id() == null ? "" : dynamicBean.getFeed_id()));
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

    /**
     * 处理发送评论数据
     *
     * @param dynamicCommentBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST)
    public void handleSendComment(DynamicCommentBean dynamicCommentBean) {
        Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<DynamicCommentBean, Integer>() {
                    @Override
                    public Integer call(DynamicCommentBean dynamicCommentBean) {
                        int size = mRootView.getListDatas().size();
                        int dynamicPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getListDatas().get(i).getFeed_mark().equals(dynamicCommentBean.getFeed_mark())) {
                                dynamicPosition = i;
                                break;
                            }
                        }
                        if (dynamicPosition != -1) {// 如果列表有当前评论
                            int commentSize = mRootView.getListDatas().get(dynamicPosition).getComments().size();
                            for (int i = 0; i < commentSize; i++) {
                                if (mRootView.getListDatas().get(dynamicPosition).getComments().get(i).getFeed_mark().equals(dynamicCommentBean.getFeed_mark())) {
                                    mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setState(dynamicCommentBean.getState());
                                    mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_id(dynamicCommentBean.getComment_id());
                                    mRootView.getListDatas().get(dynamicPosition).getComments().get(i).setComment_mark(dynamicCommentBean.getComment_mark());
                                    break;
                                }
                            }
                        }
                        return dynamicPosition;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer != -1) {
                            mRootView.refreshData(integer);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST)
    public void handleSendDynamic(DynamicBean dynamicBean) {
        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)
                || mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_FOLLOWS)) {
            int position = hasDynamicContanied(dynamicBean);
            if (position != -1) {// 如果列表有当前数据
                mRootView.showNewDynamic(position);
            } else {
                List<DynamicBean> temps = new ArrayList<>();
                temps.add(dynamicBean);
                temps.addAll(mRootView.getListDatas());
                mRootView.getListDatas().clear();
                mRootView.getListDatas().addAll(temps);
                temps.clear();
                mRootView.showNewDynamic(-1);
            }

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
        Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Bundle, Integer>() {
                    @Override
                    public Integer call(Bundle bundle) {
                        boolean isNeedRefresh = bundle.getBoolean(DYNAMIC_LIST_NEED_REFRESH);
                        DynamicBean dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                        int dynamicPosition = mRootView.getListDatas().indexOf(dynamicBean);
                        if (dynamicPosition != -1) {// 如果列表有当前评论
                            mRootView.getListDatas().set(dynamicPosition, dynamicBean);
                        }

                        return isNeedRefresh ? dynamicPosition : -1;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer != -1) {
                            mRootView.refreshData(integer);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });


    }

    @Subscriber(tag = EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE)
    public void deleteDynamic(DynamicBean dynamicBean) {
        deleteDynamic(dynamicBean, mRootView.getListDatas().indexOf(dynamicBean));
        LogUtils.d(EventBusTagConfig.DYNAMIC_LIST_DELETE_UPDATE);
    }

}