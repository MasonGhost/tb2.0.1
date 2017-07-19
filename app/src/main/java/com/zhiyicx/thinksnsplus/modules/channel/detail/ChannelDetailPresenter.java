package com.zhiyicx.thinksnsplus.modules.channel.detail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.GroupDynamicCommentListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupDynamicListBeanGreenDaoimpl;
import com.zhiyicx.thinksnsplus.data.source.local.GroupInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_LIST_NEED_REFRESH;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class ChannelDetailPresenter extends AppBasePresenter<ChannelDetailContract.Repository, ChannelDetailContract.View>
        implements ChannelDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    GroupDynamicCommentListBeanGreenDaoImpl mGroupDynamicCommentBeanGreenDaoImpl;
    @Inject
    GroupDynamicListBeanGreenDaoimpl mGroupDynamicListBeanGreenDaoimpl;
    @Inject
    public SharePolicy mSharePolicy;
    @Inject
    GroupInfoBeanGreenDaoImpl mGroupInfoBeanGreenDao;

    @Override
    public void onStart(Share share) {

    }

    @Inject
    public ChannelDetailPresenter(ChannelDetailContract.Repository repository, ChannelDetailContract.View rootView) {
        super(repository, rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {

        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        long group_id = mRootView.getGroupId();

        if (!isLoadMore) {
            Subscription subscription = Observable.zip(mRepository.getGroupDetail(group_id), mRepository.getDynamicListFromGroup(group_id, maxId)
                    , GroupZipBean::new)
                    .map(groupZipBean -> {
                        List<GroupDynamicListBean> data = groupZipBean.getGroupDynamicList();
                        for (int i = 0; i < data.size(); i++) { // 把自己发的评论加到评论列表的前面
                            List<GroupDynamicCommentListBean> dynamicCommentBeen = mGroupDynamicCommentBeanGreenDaoImpl.getMySendingComment(data.get(i).getMaxId().intValue());
                            if (!dynamicCommentBeen.isEmpty()) {
                                dynamicCommentBeen.addAll(data.get(i).getNew_comments());
                                data.get(i).getNew_comments().clear();
                                data.get(i).getNew_comments().addAll(dynamicCommentBeen);
                            }
                        }
                        return groupZipBean;
                    })
                    .subscribe(new BaseSubscribeForV2<GroupZipBean>() {
                        @Override
                        protected void onSuccess(GroupZipBean zipData) {
                            mRootView.onNetResponseSuccess(zipData.getGroupDynamicList(), isLoadMore);
                            allready(zipData);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            super.onFailure(message, code);
                            mRootView.showSnackErrorMessage(message);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            super.onException(throwable);
                            mRootView.loadAllError();
                        }

                        @Override
                        public void onCompleted() {
                            super.onCompleted();
//                            mRootView.hideCenterLoading();
                        }
                    });
            addSubscrebe(subscription);
        } else {
            Subscription subscription = mRepository.getDynamicListFromGroup(group_id, maxId)
                    .map(listBaseJson -> {
                        if (!isLoadMore) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                            List<GroupDynamicListBean> data = getGroupDynamicBeenFromDB();
                            data.addAll(listBaseJson);
                        }
                        for (int i = 0; i < listBaseJson.size(); i++) { // 把自己发的评论加到评论列表的前面
                            List<GroupDynamicCommentListBean> dynamicCommentBeen = mGroupDynamicCommentBeanGreenDaoImpl.getMySendingComment(listBaseJson.get(i).getMaxId().intValue());
                            if (!dynamicCommentBeen.isEmpty()) {
                                dynamicCommentBeen.addAll(listBaseJson.get(i).getNew_comments());
                                listBaseJson.get(i).getNew_comments().clear();
                                listBaseJson.get(i).getNew_comments().addAll(dynamicCommentBeen);
                            }
                        }


                        return listBaseJson;
                    })
                    .subscribe(new BaseSubscribeForV2<List<GroupDynamicListBean>>() {
                        @Override
                        protected void onSuccess(List<GroupDynamicListBean> data) {
                            mRootView.onNetResponseSuccess(data, isLoadMore);
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
    }

    @Override
    public List<GroupDynamicListBean> requestCacheData(Long max_Id, boolean isLoadMore) {
        // 频道的动态不要从数据库拉取数据
        return null;
    }

    @Override
    public void getGroupDetails(long group_id) {
        mRepository.getGroupDetail(group_id).subscribe(new BaseSubscribeForV2<GroupInfoBean>() {
            @Override
            protected void onSuccess(GroupInfoBean data) {
                mGroupInfoBeanGreenDao.insertOrReplace(data);
            }
        });
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupDynamicListBean> data, boolean isLoadMore) {
        return false;
    }

    @NonNull
    private List<GroupDynamicListBean> getGroupDynamicBeenFromDB() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return new ArrayList<>();
        }
        List<GroupDynamicListBean> datas = mGroupDynamicListBeanGreenDaoimpl.getMySendingUnSuccessDynamic((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        return datas;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    private void allready(GroupZipBean groupZipBean) {
        mRootView.allDataReady(groupZipBean);
    }

    /**
     * handle like or cancle like in background
     *
     * @param isLiked    true,do like ,or  cancle like
     * @param dynamic_id dynamic id
     * @param group_id   group id
     * @param position   dynamic position
     */
    @Override
    public void handleLike(boolean isLiked, long group_id, long dynamic_id, int position) {
        if (dynamic_id == 0) {
            return;
        }
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getListDatas().get(position));
        mRepository.handleLike(isLiked, group_id, dynamic_id);
    }

    @Override
    public void handleCollect(GroupDynamicListBean dynamicBean) {
        // 修改数据-更新界面
        boolean is_collection = dynamicBean.getIs_collection() == GroupDynamicListBean.IS_COLLECT;// 旧状态
        // 更新数据库
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(dynamicBean);
        // 通知服务器
        mRepository.handelCollect(is_collection, dynamicBean.getGroup_id(), dynamicBean.getId());
    }

    @Override
    public void handleViewCount(Long feed_id, int position) {
        if (feed_id == null || feed_id == 0) {
            return;
        }
        mRootView.getListDatas().get(position).setViews(mRootView.getListDatas().get(position).getViews() + 1);
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getListDatas().get(position));
        mRepository.handleDynamicViewCount(feed_id);
        mRootView.refreshData();
    }

    @Override
    public void reSendDynamic(int position) {
        // 目前圈子动态失败也不重发
    }

    @Override
    public void deleteComment(GroupDynamicListBean dynamicBean, int dynamicPosition, long comment_id, int commentPositon) {
        mRootView.getListDatas().get(dynamicPosition).setComments(dynamicBean.getComments() - 1);
        mGroupDynamicListBeanGreenDaoimpl.insertOrReplace(mRootView.getListDatas().get(dynamicPosition));
        mGroupDynamicCommentBeanGreenDaoImpl.deleteSingleCache(dynamicBean.getNew_comments().get(commentPositon));
        mRootView.getListDatas().get(dynamicPosition).getNew_comments().remove(commentPositon);
        mRootView.refreshData(dynamicPosition);
        mRepository.deleteComment(dynamicBean.getId(), comment_id);
    }


    @Override
    public void reSendComment(GroupDynamicCommentListBean commentBean, long feed_id) {
        commentBean.setState(DynamicCommentBean.SEND_ING);
        mRepository.sendComment(commentBean.getContent(), feed_id, commentBean.getReply_to_user_id(), 1L);
        mRootView.refreshData();
    }

    @Override
    public void deleteDynamic(GroupDynamicListBean dynamicBean, int position) {

        mGroupDynamicListBeanGreenDaoimpl.deleteSingleCache(dynamicBean);
        mRootView.getListDatas().remove(position);
        if (mRootView.getListDatas().isEmpty()) {// 添加暂未图
            mRootView.getListDatas().add(new GroupDynamicListBean());
        }
        mRootView.refreshData();
        if (dynamicBean.getId() != null && dynamicBean.getId() != 0) {
            mRepository.deleteDynamic(dynamicBean.getId());
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
        GroupDynamicCommentListBean creatComment = new GroupDynamicCommentListBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setContent(commentContent);
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setFeed_id(mRootView.getListDatas().get(mCurrentPostion).getId().intValue());
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
        List<GroupDynamicCommentListBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getListDatas().get(mCurrentPostion).getNew_comments());
        mRootView.getListDatas().get(mCurrentPostion).getNew_comments().clear();
        mRootView.getListDatas().get(mCurrentPostion).getNew_comments().addAll(commentBeanList);
        mRootView.getListDatas().get(mCurrentPostion).setComments(mRootView.getListDatas().get(mCurrentPostion).getComments() + 1);
        mRootView.refreshData();

        mGroupDynamicCommentBeanGreenDaoImpl.insertOrReplace(creatComment);
        mRepository.sendGroupComment(commentContent,
                (long) mRootView.getListDatas().get(mCurrentPostion).getGroup_id(),
                mRootView.getListDatas().get(mCurrentPostion).getId(),
                replyToUserId,
                creatComment.getComment_mark());

    }

    @Override
    public void shareDynamic(GroupDynamicListBean dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mContext.getString(R.string.share));
        shareContent.setContent(TextUtils.isEmpty(dynamicBean.getContent()) ? mContext.getString(R.string
                .share_dynamic) : dynamicBean.getContent());
        if (bitmap != null) {
            shareContent.setBitmap(bitmap);
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_256)));
        }
        shareContent.setUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean.getId()
                == null ? "" : dynamicBean.getId()));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
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
     * 通过 feed_id 获取当前数据的位置
     *
     * @param feed_id
     * @return
     */
    @Override
    public int getCurrenPosiotnInDataList(long feed_id) {
        int position = -1;
        int size = mRootView.getListDatas().size();
        for (int i = 0; i < size; i++) {
            if (feed_id == mRootView.getListDatas().get(i).getId()) {
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
    public void handleSendComment(GroupDynamicCommentListBean dynamicCommentBean) {
        Observable.just(dynamicCommentBean)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(dynamicCommentBean1 -> {
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getId().equals(dynamicCommentBean1.getFeed_id())) {
                            dynamicPosition = i;
                            break;
                        }
                    }
                    if (dynamicPosition != -1) {// 如果列表有当前评论
                        int commentSize = mRootView.getListDatas().get(dynamicPosition).getNew_comments().size();
                        for (int i = 0; i < commentSize; i++) {
                            if (mRootView.getListDatas().get(dynamicPosition).getNew_comments().get(i).getFeed_id() == dynamicCommentBean1.getFeed_id()) {
                                mRootView.getListDatas().get(dynamicPosition).getNew_comments().get(i).setState(dynamicCommentBean1.getState());
                                mRootView.getListDatas().get(dynamicPosition).getNew_comments().get(i).setId(dynamicCommentBean1.getId());
                                mRootView.getListDatas().get(dynamicPosition).getNew_comments().get(i).setFeed_id(dynamicCommentBean1.getFeed_id());
                                break;
                            }
                        }
                    }
                    return dynamicPosition;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, throwable -> throwable.printStackTrace());

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
                .map(bundle -> {
                    int position = bundle.getInt(DYNAMIC_DETAIL_DATA_POSITION);
                    boolean isNeedRefresh = bundle.getBoolean(DYNAMIC_LIST_NEED_REFRESH);
                    GroupDynamicListBean dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                    int size = mRootView.getListDatas().size();
                    int dynamicPosition = -1;
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getId().equals(dynamicBean.getId())) {
                            dynamicPosition = i;
                            break;
                        }
                    }
                    if (dynamicPosition != -1) {// 如果列表有当前评论
                        mRootView.getListDatas().set(position, dynamicBean);
                    }

                    return isNeedRefresh ? dynamicPosition : -1;
                })
                .subscribe(integer -> {
                    if (integer != -1) {
                        mRootView.refreshData();
                    }

                }, throwable -> throwable.printStackTrace());


    }

    /**
     * 收到发送动态的通知
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_CHANNEL)
    public void getSendDynamic(DynamicBean dynamicBean) {
        mRootView.sendDynamic();
    }

    @Override
    public void handleGroupSubscrib(final GroupInfoBean groupSubscripBean) {
        Subscription subscription = mRepository.handleSubscribGroupByFragment(groupSubscripBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        // 发送到可能的地方，改变订阅状态
                        groupSubscripBean.setIs_member(1);
                        mRootView.subscribChannelState(true, groupSubscripBean, "");
                        EventBus.getDefault().post(groupSubscripBean, EventBusTagConfig.EVENT_CHANNEL_SUBSCRIB);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.subscribChannelState(false, groupSubscripBean, message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        String message = throwable.getMessage();
                        if (TextUtils.isEmpty(message)) {
                            message = mContext.getString(R.string.err_net_not_work);
                        }
                        mRootView.subscribChannelState(false, groupSubscripBean, message);
                    }
                });
        addSubscrebe(subscription);
    }
}
