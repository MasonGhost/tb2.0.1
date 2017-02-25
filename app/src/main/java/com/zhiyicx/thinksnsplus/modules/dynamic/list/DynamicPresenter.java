package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicToolBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action0;
import rx.functions.Func1;

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
        mRepository.getDynamicList(mRootView.getDynamicType(), maxId, mRootView.getPage())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mRootView.hideLoading();
                    }
                })
                .map(new Func1<BaseJson<List<DynamicBean>>, BaseJson<List<DynamicBean>>>() {
                    @Override
                    public BaseJson<List<DynamicBean>> call(BaseJson<List<DynamicBean>> listBaseJson) {
                        if (!isLoadMore && listBaseJson.isStatus() && mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) { // 如果是刷新，并且获取到了数据，更新发布的动态 ,把发布的动态信息放到请求数据的前面
                            insertOrUpdateDynamicDB(listBaseJson.getData());
                            List<DynamicBean> data = getDynamicBeenFromDB();
                            data.addAll(listBaseJson.getData());
                            listBaseJson.setData(data);
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
        List<DynamicDetailBean> dynamicDetailBeen = new ArrayList<>();
        List<DynamicCommentBean> dynamicCommentBeen = new ArrayList<>();
        List<DynamicToolBean> dynamicToolBeen = new ArrayList<>();
        for (DynamicBean dynamicBean : data) {
            dynamicBean.setFeed_id(dynamicBean.getFeed().getFeed_id());
            dynamicBean.getFeed().setFeed_mark(dynamicBean.getFeed_mark());
            dynamicBean.getTool().setFeed_mark(dynamicBean.getFeed_mark());
            for (DynamicCommentBean dynamicCommentBean : dynamicBean.getComments()) {
                dynamicCommentBean.setFeed_mark(dynamicBean.getFeed_mark());
            }
            dynamicDetailBeen.add(dynamicBean.getFeed());
            dynamicCommentBeen.addAll(dynamicBean.getComments());
            dynamicToolBeen.add(dynamicBean.getTool());

        }
        mDynamicBeanGreenDao.insertOrReplace(data);
        mDynamicDetailBeanGreenDao.insertOrReplace(dynamicDetailBeen);
        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBeen);
        mDynamicToolBeanGreenDao.insertOrReplace(dynamicToolBeen);
    }

    /**
     * 处理发送动态数据
     *
     * @param dynamicBean
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST)
    public void handleSendDynamic(DynamicBean dynamicBean) {
        LogUtils.d(TAG,"handleSendDynamic = " +dynamicBean);
        if (mRootView.getDynamicType().equals(ApiConfig.DYNAMIC_TYPE_NEW)) {
            List<DynamicBean> datas = new ArrayList<>();
            int position = msendingStatus.indexOfValue(dynamicBean.getFeed_mark());
            if (position == -1) {
                datas.add(mDynamicBeanGreenDao.getDynamicByFeedMark(dynamicBean.getFeed_mark()));
                datas.addAll(mRootView.getDatas());
                mRootView.setDatas(datas);
            } else {
                mRootView.getDatas().get(position).setState(dynamicBean.getState());
            }
        }
    }

    @NonNull
    private List<DynamicBean> getDynamicBeenFromDB() {
        List<DynamicBean> datas = mDynamicBeanGreenDao.getMySendingDynamic((long) AppApplication.getmCurrentLoginAuth().getUser_id());
        msendingStatus.clear();
        for (int i = 0; i < datas.size(); i++) {
            msendingStatus.put(i, datas.get(i).getFeed_mark());
        }
        return datas;
    }
}