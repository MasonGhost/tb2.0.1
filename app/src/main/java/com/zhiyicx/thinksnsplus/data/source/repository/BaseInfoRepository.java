package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/7
 * @contact email:648129313@qq.com
 */

public class BaseInfoRepository implements IBaseInfoRepository {

    protected InfoMainClient mInfoMainClient;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    Application mContext;

    @Inject
    public BaseInfoRepository(ServiceManager manager) {
        mInfoMainClient = manager.getInfoMainClient();
    }

    @Override
    public Observable<List<InfoListDataBean>> getInfoListV2(String cate_id, String key, long max_id, long page, int isRecommend) {

        switch (cate_id) {
            case ApiConfig.INFO_TYPE_COLLECTIONS:
                return getCollectionListV2(max_id);
            default:
        }

        if (!TextUtils.isEmpty(key)) {
            return mInfoMainClient.getInfoListV2(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, key, 0);
        } else if (max_id == 0 && isRecommend != 1) {
            // 只有下拉才获取置顶,推荐这个栏目页不要置顶资讯
            return mInfoMainClient.getInfoTopList(cate_id)
                    .observeOn(Schedulers.io())
                    .flatMap(new Func1<List<InfoListDataBean>, Observable<List<InfoListDataBean>>>() {
                        @Override
                        public Observable<List<InfoListDataBean>> call(List<InfoListDataBean> infoListDataBeenTopList) {
                            if (infoListDataBeenTopList != null) {
                                for (InfoListDataBean infoListDataBean : infoListDataBeenTopList) {
                                    infoListDataBean.setIsTop(true);
                                }
                            }
                            return mInfoMainClient.getInfoListV2(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, "",
                                    isRecommend)
                                    .map(infoListDataBeenList -> {
                                        if (infoListDataBeenTopList != null) {
                                            infoListDataBeenList.addAll(0, infoListDataBeenTopList);
                                        }
                                        return infoListDataBeenList;
                                    });
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    ;
        } else {
            return mInfoMainClient.getInfoListV2(cate_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), page, "", isRecommend);
        }
    }

    @Override
    public Observable<List<InfoListDataBean>> getCollectionListV2(long max_id) {
        return mInfoMainClient.getInfoCollectListV2(max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<InfoListDataBean>> getMyInfoList(String type, long max_id) {
        return mInfoMainClient.getMyInfoList(max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE), type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<InfoTypeBean> getInfoType() {
        return mInfoMainClient.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> publishInfo(InfoPublishBean infoPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(infoPublishBean));
        return mInfoMainClient.publishInfo(infoPublishBean.getCategoryId(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<Object>> updateInfo(InfoPublishBean infoPublishBean) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(infoPublishBean));
        return mInfoMainClient.updateInfo(infoPublishBean.getCategoryId(), infoPublishBean.getNews_id(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<InfoCommentBean> getInfoCommentListV2(String news_id, Long max_id, Long limit) {
        return mInfoMainClient.getInfoCommentListV2(news_id, max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                .observeOn(Schedulers.io())
                .flatMap(infoCommentBean -> {
                    final List<Object> user_ids = new ArrayList<>();
                    if (infoCommentBean.getPinneds() != null) {
                        for (InfoCommentListBean commentListBean : infoCommentBean.getPinneds()) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTarget_user());
                        }
                    }
                    if (infoCommentBean.getComments() != null) {
                        for (InfoCommentListBean commentListBean : infoCommentBean.getComments()) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_to_user_id());
                            user_ids.add(commentListBean.getTarget_user());
                        }
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(infoCommentBean);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(userInfoBeanList -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                        SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeanList) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                            .intValue(), userInfoBean);
                                }
                                dealCommentData(infoCommentBean.getPinneds(), userInfoBeanSparseArray);
                                dealCommentData(infoCommentBean.getComments(), userInfoBeanSparseArray);
                                mUserInfoBeanGreenDao.insertOrReplace(userInfoBeanList);
                                return infoCommentBean;
                            });
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<InfoDigListBean>> getInfoDigListV2(String news_id, Long max_id) {
        return mInfoMainClient.getInfoDigList(news_id, max_id, TSListFragment.DEFAULT_PAGE_SIZE)
                .flatMap(infoDigListBeen -> {
                    List<Object> user_ids = new ArrayList<>();
                    for (InfoDigListBean digListBean : infoDigListBeen) {
                        user_ids.add(digListBean.getUser_id());
                        user_ids.add(digListBean.getTarget_user());
                    }
                    if (user_ids.isEmpty()) {
                        return Observable.just(infoDigListBeen);
                    }
                    return mUserInfoRepository.getUserInfo(user_ids)
                            .map(listBaseJson -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : listBaseJson) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                mUserInfoBeanGreenDao.insertOrReplace(listBaseJson);
                                for (InfoDigListBean digListBean : infoDigListBeen) {
                                    digListBean.setDiggUserInfo(userInfoBeanSparseArray.get(digListBean.getUser_id().intValue()));
                                    digListBean.setTargetUserInfo(userInfoBeanSparseArray.get(digListBean.getTarget_user().intValue()));
                                }
                                return infoDigListBeen;
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<InfoListDataBean>> getRelateInfoList(String news_id) {
        return mInfoMainClient.getRelateInfoList(news_id);
    }

    @Override
    public Observable<InfoListDataBean> getInfoDetail(String news_id) {
        return mInfoMainClient.getInfoDetail(news_id);
    }

    private void dealCommentData(List<InfoCommentListBean> list, SparseArray<UserInfoBean> userInfoBeanSparseArray) {
        if (list != null) {
            for (InfoCommentListBean commentListBean : list) {
                commentListBean.setFromUserInfoBean
                        (userInfoBeanSparseArray.get((int) commentListBean
                                .getUser_id()));
                if (commentListBean.getReply_to_user_id() == 0) { // 如果
                    // reply_user_id = 0 回复动态
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(0L);
                    commentListBean.setToUserInfoBean(userInfoBean);
                } else {
                    commentListBean.setToUserInfoBean
                            (userInfoBeanSparseArray.get(
                                    (int) commentListBean
                                            .getReply_to_user_id()));
                }
                if (commentListBean.getTarget_user() == 0) { // 如果
                    // reply_user_id = 0 回复动态
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(0L);
                    commentListBean.setPublishUserInfoBean(userInfoBean);
                } else {
                    commentListBean.setPublishUserInfoBean
                            (userInfoBeanSparseArray.get(
                                    (int) commentListBean
                                            .getTarget_user()));
                }
            }
        }
    }

    @Override
    public void handleCollect(boolean isUnCollected, final String news_id) {
        Observable.just(isUnCollected)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("news_id", news_id);
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.POST_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_INFO_COLLECTION_S, news_id));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void handleLike(boolean isLiked, final String news_id) {
        Observable.just(isLiked)
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    BackgroundRequestTaskBean backgroundRequestTaskBean;
                    HashMap<String, Object> params = new HashMap<>();
                    params.put("news_id", news_id);
                    // 后台处理
                    if (aBoolean) {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.POST_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    } else {
                        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
                        LogUtils.d(backgroundRequestTaskBean.getMethodType());
                    }
                    backgroundRequestTaskBean.setPath(String.format(ApiConfig
                            .APP_PATH_INFO_DIG_V2_S, news_id));
                    BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                            (backgroundRequestTaskBean);
                }, throwable -> throwable.printStackTrace());
    }

    @Override
    public void sendComment(String comment_content, Long new_id, int reply_to_user_id,
                            Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("body", comment_content);
        if (reply_to_user_id > 0) {
            params.put("reply_user", reply_to_user_id);
        }
        params.put("comment_mark", comment_mark);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.SEND_INFO_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_INFO_COMMENT_V2_S,
                new_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(int news_id, int comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.DELETE_V2, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                .APP_PATH_INFO_DELETE_COMMENT_V2_S, news_id, comment_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public Observable<BaseJsonV2<Object>> deleteInfo(String category, String news_id) {
        return mInfoMainClient.deleteInfo(category, news_id);
    }


/*******************************************  TB  *********************************************/

    /**
     * 获取资讯列表
     *
     * @param key  搜索用的关键字
     * @param type top-头条资讯 follow-关注机构资讯 默认top
     */
    @Override
    public Observable<List<InfoListDataBean>> getInfoListTB(String cate_id,
                                                            Long max_id,
                                                            Long limit,
                                                            Long page,
                                                            String key,
                                                            String type) {

        return mInfoMainClient.getInfoListTB(cate_id, max_id, limit, page, key, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
