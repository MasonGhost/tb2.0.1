package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.util.SparseArray;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsConstract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsRepository implements InfoDetailsConstract.Repository {

    InfoMainClient mInfoMainClient;
    protected UserInfoRepository mUserInfoRepository;
    private Application context;

    @Inject
    public InfoDetailsRepository(ServiceManager serviceManager, Application context) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        mUserInfoRepository = new UserInfoRepository(serviceManager, context);
        this.context = context;
    }

    @Override
    public Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String feed_id,
                                                                              Long max_id, Long
                                                                                      limit) {
        return mInfoMainClient.getInfoCommentList(feed_id, max_id, null)
                .flatMap(new Func1<BaseJson<List<InfoCommentListBean>>,
                        Observable<BaseJson<List<InfoCommentListBean>>>>() {

                    @Override
                    public Observable<BaseJson<List<InfoCommentListBean>>> call
                            (final BaseJson<List<InfoCommentListBean>> listBaseJson) {
                        final List<Long> user_ids = new ArrayList<>();
                        for (InfoCommentListBean commentListBean : listBaseJson.getData()) {
                            user_ids.add(commentListBean.getUser_id());
                            user_ids.add(commentListBean.getReply_to_user_id());
                        }

                        return mUserInfoRepository.getUserInfo(user_ids).map(new Func1<BaseJson<List<UserInfoBean>>,
                                BaseJson<List<InfoCommentListBean>>>() {

                            @Override
                            public BaseJson<List<InfoCommentListBean>> call(BaseJson<List
                                    <UserInfoBean>> userinfobeans) {
                                if (userinfobeans.isStatus()) { //
                                    // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                            SparseArray<>();
                                    for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                                .intValue(), userInfoBean);
                                    }
                                    for (InfoCommentListBean commentListBean : listBaseJson
                                            .getData()) {
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

                                    }
                                    AppApplication.AppComponentHolder.getAppComponent()
                                            .userInfoBeanGreenDao().insertOrReplace(userinfobeans
                                            .getData());
                                } else {
                                    listBaseJson.setStatus(userinfobeans.isStatus());
                                    listBaseJson.setCode(userinfobeans.getCode());
                                    listBaseJson.setMessage(userinfobeans.getMessage());
                                }

                                return listBaseJson;
                            }
                        });
                    }
                });
    }

    @Override
    public void handleCollect(boolean isUnCollected, final String news_id) {
        Observable.just(isUnCollected)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        BackgroundRequestTaskBean backgroundRequestTaskBean;
                        HashMap<String, Object> params = new HashMap<>();
                        params.put("news_id", news_id);
                        // 后台处理
                        if (aBoolean) {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                    (BackgroundTaskRequestMethodConfig.POST, params);
                        } else {
                            backgroundRequestTaskBean = new BackgroundRequestTaskBean
                                    (BackgroundTaskRequestMethodConfig.DELETE, params);
                        }
                        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                                .APP_PATH_INFO_COLLECT_FORMAT, news_id));
                        BackgroundTaskManager.getInstance(context).addBackgroundRequestTask
                                (backgroundRequestTaskBean);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void sendComment(String comment_content, Long new_id, int reply_to_user_id,
                            Long comment_mark) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", comment_content);
        params.put("reply_to_user_id", reply_to_user_id);
        params.put("comment_mark", comment_mark);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.SEND_INFO_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_INFO_COMMENT_FORMAT,
                new_id));
        BackgroundTaskManager.getInstance(context).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public void deleteComment(int news_id, int comment_id) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("news_id", news_id);
        params.put("comment_id", comment_id);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig
                .APP_PATH_INFO_DELETE_COMMENT_FORMAT, news_id, comment_id));
        BackgroundTaskManager.getInstance(context).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }
}
