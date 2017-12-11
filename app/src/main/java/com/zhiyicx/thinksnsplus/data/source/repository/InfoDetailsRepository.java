package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoWebBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsConstract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsRepository extends BaseRewardRepository implements InfoDetailsConstract.Repository {

    InfoMainClient mInfoMainClient;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    Application mContext;

    @Inject
    public InfoDetailsRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mInfoMainClient = serviceManager.getInfoMainClient();
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
        if (reply_to_user_id>0){
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


}
