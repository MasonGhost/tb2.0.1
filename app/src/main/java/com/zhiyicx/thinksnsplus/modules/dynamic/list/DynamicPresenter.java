package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import org.jetbrains.annotations.NotNull;
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
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_POSITION;
import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA_TYPE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/13
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class DynamicPresenter extends BasePresenter<DynamicContract.Repository, DynamicContract.View> implements DynamicContract.Presenter {

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
        Subscription dynamicLisSub = mRepository.getDynamicList(mRootView.getDynamicType(), maxId, mRootView.getPage())
                .map(new Func1<BaseJson<List<DynamicBean>>, BaseJson<List<DynamicBean>>>() {
                    @Override
                    public BaseJson<List<DynamicBean>> call(BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus()) {
                            insertOrUpdateDynamicDB(listBaseJson.getData()); // 更新数据库
                            if (!isLoadMore) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                                if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) {
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
                    protected void onFailure(String message) {
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
                datas = mDynamicBeanGreenDao.getFollowedDynamicList(maxId);
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
            default:
        }
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getFeed_mark() != null) {
                datas.get(i).setComments(mDynamicCommentBeanGreenDao.getLocalComments(datas.get(i).getFeed_mark()));
            }
        }
        System.out.println("datas.toString() = " + datas.toString());
        return datas;
    }

    /**
     * 此处需要先存入数据库，方便处理动态的状态，故此处不需要再次更新数据库
     *
     * @param data
     * @return
     */
    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicBean> data) {
        return true;
    }

    /**
     * 动态数据库更新
     *
     * @param data
     */
    private void insertOrUpdateDynamicDB(@NotNull List<DynamicBean> data) {
        mRepository.updateOrInsertDynamic(data);

    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST)
    public void handleSendDynamic(DynamicBean dynamicBean) {
        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) {
            int position = hasDynamicContanied(dynamicBean);
            if (position != -1) {// 如果列表有当前数据
                mRootView.refresh(position);
            } else {
                List<DynamicBean> temps = new ArrayList<>();
                temps.add(dynamicBean);
                temps.addAll(mRootView.getDatas());
                mRootView.getDatas().clear();
                mRootView.getDatas().addAll(temps);
                temps.clear();
                mRootView.refresh();
            }

        }
    }

    /**
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
                        String type = bundle.getString(DYNAMIC_DETAIL_DATA_TYPE);
                        int position = bundle.getInt(DYNAMIC_DETAIL_DATA_POSITION);
                        DynamicBean dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
                        if (mRootView.getDynamicType().equals(type)) { // 先刷新当前页面，再刷新其他页面
                            mRootView.getDatas().set(position, dynamicBean);
                            return position;
                        }
                        int size = mRootView.getDatas().size();
                        int dynamicPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                                dynamicPosition = i;
                                break;
                            }
                        }
                        if (dynamicPosition != -1) {// 如果列表有当前评论
                            mRootView.getDatas().set(position, dynamicBean);
                        }
                        return dynamicPosition;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer != -1) {
                            mRootView.refresh(integer);
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
     * 列表中是否有了
     *
     * @param dynamicBean
     * @return
     */
    private int hasDynamicContanied(DynamicBean dynamicBean) {
        int size = mRootView.getDatas().size();
        for (int i = 0; i < size; i++) {
            if (mRootView.getDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                mRootView.getDatas().get(i).setState(dynamicBean.getState());
                mRootView.getDatas().get(i).setFeed_id(dynamicBean.getFeed_id());
                mRootView.getDatas().get(i).getFeed().setFeed_id(dynamicBean.getFeed_id());
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
            msendingStatus.put(i, datas.get(i).getFeed_mark());
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
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getDatas().get(postion).getTool());
        mRepository.handleLike(isLiked, feed_id);

    }

    @Override
    public void reSendDynamic(int position) {
        // 将动态信息存入数据库
        mDynamicBeanGreenDao.insertOrReplace(mRootView.getDatas().get(position));
        mDynamicDetailBeanGreenDao.insertOrReplace(mRootView.getDatas().get(position).getFeed());
        // 发送动态
        BackgroundRequestTaskBean backgroundRequestTaskBean = new BackgroundRequestTaskBean();
        backgroundRequestTaskBean.setMethodType(BackgroundTaskRequestMethodConfig.SEND_DYNAMIC);
        HashMap<String, Object> params = new HashMap<>();
        // feed_mark作为参数
        params.put("params", mRootView.getDatas().get(position).getFeed_mark());
        backgroundRequestTaskBean.setParams(params);
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(DynamicBean dynamicBean, int dynamicPosition, long comment_id, int commentPositon) {
        mRootView.getDatas().get(dynamicPosition).getTool().setFeed_comment_count(dynamicBean.getTool().getFeed_comment_count() - 1);
        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getDatas().get(dynamicPosition).getTool());
        mDynamicCommentBeanGreenDao.deleteSingleCache(dynamicBean.getComments().get(commentPositon));
        mRootView.getDatas().get(dynamicPosition).getComments().remove(commentPositon);
        mRootView.refresh(dynamicPosition);
        mRepository.deleteComment(dynamicBean.getFeed_id(), comment_id);
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
        creatComment.setFeed_mark(mRootView.getDatas().get(mCurrentPostion).getFeed_mark());
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        System.out.println("creatComment ---------------> = " + creatComment.getReply_to_user_id());
        if (replyToUserId == 0) { //当回复动态的时候
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {

            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getmCurrentLoginAuth().getUser_id());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache((long) AppApplication.getmCurrentLoginAuth().getUser_id()));
        creatComment.setCreated_at(TimeUtils.millis2String(System.currentTimeMillis()));
        List<DynamicCommentBean> commentBeanList = new ArrayList<>();
        commentBeanList.add(creatComment);
        commentBeanList.addAll(mRootView.getDatas().get(mCurrentPostion).getComments());
        mRootView.getDatas().get(mCurrentPostion).getComments().clear();
        mRootView.getDatas().get(mCurrentPostion).getComments().addAll(commentBeanList);
        mRootView.getDatas().get(mCurrentPostion).getTool().setFeed_comment_count(mRootView.getDatas().get(mCurrentPostion).getTool().getFeed_comment_count() + 1);
        mRootView.refresh(mCurrentPostion);

        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getDatas().get(mCurrentPostion).getTool());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        mRepository.sendComment(commentContent, mRootView.getDatas().get(mCurrentPostion).getFeed_id(), replyToUserId, creatComment.getComment_mark());

    }

    /**
     * 处理发送动态数据
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
                        int size = mRootView.getDatas().size();
                        int dynamicPosition = -1;
                        for (int i = 0; i < size; i++) {
                            if (mRootView.getDatas().get(i).getFeed_mark().equals(dynamicCommentBean.getFeed_mark())) {
                                dynamicPosition = i;
                                break;
                            }
                        }
                        if (dynamicPosition != -1) {// 如果列表有当前评论
                            int commentSize = mRootView.getDatas().get(dynamicPosition).getComments().size();
                            for (int i = 0; i < commentSize; i++) {
                                if (mRootView.getDatas().get(dynamicPosition).getComments().get(i).getFeed_mark().equals(dynamicCommentBean.getFeed_mark())) {
                                    mRootView.getDatas().get(dynamicPosition).getComments().get(i).setState(dynamicCommentBean.getState());
                                    mRootView.getDatas().get(dynamicPosition).getComments().get(i).setComment_mark(dynamicCommentBean.getComment_mark());
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
                            mRootView.refresh(integer);
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

}