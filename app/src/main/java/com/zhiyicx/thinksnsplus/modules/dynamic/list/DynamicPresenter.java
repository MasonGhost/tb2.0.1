package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
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
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .map(new Func1<BaseJson<List<DynamicBean>>, BaseJson<List<DynamicBean>>>() {
                    @Override
                    public BaseJson<List<DynamicBean>> call(BaseJson<List<DynamicBean>> listBaseJson) {
                        if (listBaseJson.isStatus()) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                            insertOrUpdateDynamicDB(listBaseJson.getData());
                            if (!isLoadMore) {
                                if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) {
                                    List<DynamicBean> data = getDynamicBeenFromDB();
                                    data.addAll(listBaseJson.getData());
                                    listBaseJson.setData(data);
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

        Observable.just(data)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<List<DynamicBean>>() {
                    @Override
                    public void call(List<DynamicBean> datas) {

                        List<DynamicDetailBean> dynamicDetailBeen = new ArrayList<>();
                        List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
                        List<DynamicToolBean> dynamicToolBeen = new ArrayList<>();
                        for (DynamicBean dynamicBeanTmp : datas) {
                            // 处理关注和热门数据
                            dealLocalTypeData(dynamicBeanTmp);
                            // 把详情的 feed_id 放到 dynamicbean 中
                            dynamicBeanTmp.setFeed_id(dynamicBeanTmp.getFeed().getFeed_id());
                            // 把 feed_mark 设置到详情中去
                            dynamicBeanTmp.getFeed().setFeed_mark(dynamicBeanTmp.getFeed_mark());
                            dynamicBeanTmp.getTool().setFeed_mark(dynamicBeanTmp.getFeed_mark());
                            for (DynamicCommentBean dynamicCommentBean : dynamicBeanTmp.getComments()) {
                                dynamicCommentBean.setFeed_mark(dynamicBeanTmp.getFeed_mark());
                            }
                            dynamicDetailBeen.add(dynamicBeanTmp.getFeed());
                            dynamicCommentBeen.addAll(dynamicBeanTmp.getComments());
                            dynamicToolBeen.add(dynamicBeanTmp.getTool());

                        }
                        mDynamicBeanGreenDao.insertOrReplace(datas);
                        mDynamicDetailBeanGreenDao.insertOrReplace(dynamicDetailBeen);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBeen);
                        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBeen);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    private void dealLocalTypeData(DynamicBean dynamicBeanTmp) {
        DynamicBean localDynamicBean = mDynamicBeanGreenDao.getDynamicByFeedMark(dynamicBeanTmp.getFeed_mark());
        if (localDynamicBean != null) {
            switch (mRootView.getDynamicType()) {
                case ApiConfig.DYNAMIC_TYPE_FOLLOWS:
                    if (localDynamicBean.getHot_creat_time() != null && dynamicBeanTmp.getHot_creat_time() != 0) {
                        dynamicBeanTmp.setHot_creat_time(localDynamicBean.getHot_creat_time());
                    }
                    break;

                case ApiConfig.DYNAMIC_TYPE_HOTS:
                    if (localDynamicBean.getIsFollowed()) {
                        dynamicBeanTmp.setIsFollowed(localDynamicBean.getIsFollowed());
                    }
                    break;
                case ApiConfig.DYNAMIC_TYPE_NEW:
                    dynamicBeanTmp.setIsFollowed(localDynamicBean.getIsFollowed());
                    dynamicBeanTmp.setHot_creat_time(localDynamicBean.getHot_creat_time());
                    break;

                default:

            }
        }
    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST)
    public void handleSendDynamic(DynamicBean dynamicBean) {
        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) {
            int position = hasContanied(dynamicBean);
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

    private int hasContanied(DynamicBean dynamicBean) {
        int size = mRootView.getDatas().size();
        for (int i = 0; i < size; i++) {
            if (mRootView.getDatas().get(i).getFeed_mark().equals(dynamicBean.getFeed_mark())) {
                mRootView.getDatas().get(i).setState(dynamicBean.getState());
                return i;
            }
        }
        return -1;
    }

    @NonNull
    private List<DynamicBean> getDynamicBeenFromDB() {
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
        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mDynamicToolBeanGreenDao.insertOrReplace(mRootView.getDatas().get(postion).getTool());
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("feed_id", feed_id);
                        // 后台处理
                        if (aBoolean) {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.POST, params);
                        } else {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig.DELETE, params);
                        }
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_DYNAMIC_HANDLE_LIKE_FORMAT, feed_id));
                        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

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


}