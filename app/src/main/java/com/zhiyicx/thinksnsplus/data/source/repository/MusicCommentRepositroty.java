package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.util.SparseArray;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentRepositroty implements MusicCommentContract.Repository {

    MusicClient mMusicClient;
    Context mContext;
    protected UserInfoRepository mUserInfoRepository;

    @Inject
    public MusicCommentRepositroty(Application application, ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
        mContext = application;
        mUserInfoRepository = new UserInfoRepository(serviceManager, application);
    }

    @Override
    public Observable<BaseJson<List<MusicCommentListBean>>> getMusicCommentList(String music_id,
                                                                                long max_id) {
        return mMusicClient.getMusicCommentList(music_id, max_id,
                Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                .flatMap(new Func1<BaseJson<List<MusicCommentListBean>>, Observable<BaseJson<List<MusicCommentListBean>>>>() {

                    @Override
                    public Observable<BaseJson<List<MusicCommentListBean>>> call
                            (final BaseJson<List<MusicCommentListBean>> listBaseJson) {

                        if (listBaseJson.getData().isEmpty()) {
                            return Observable.just(listBaseJson);
                        } else {
                            final List<Long> user_ids = new ArrayList<>();
                            for (MusicCommentListBean commentListBean : listBaseJson.getData()) {
                                user_ids.add((long) commentListBean.getUser_id());
                                user_ids.add((long) commentListBean.getReply_to_user_id());
                            }

                            return mUserInfoRepository.getUserInfo(user_ids).map(new Func1<BaseJson<List<UserInfoBean>>,
                                    BaseJson<List<MusicCommentListBean>>>() {

                                @Override
                                public BaseJson<List<MusicCommentListBean>> call(BaseJson<List
                                        <UserInfoBean>> userinfobeans) {
                                    if (userinfobeans.isStatus()) { //
                                        // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                                SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                                    .intValue(), userInfoBean);
                                        }
                                        for (MusicCommentListBean commentListBean : listBaseJson
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
                                                commentListBean.setToUserInfoBean(userInfoBeanSparseArray.get(commentListBean.getReply_to_user_id()));
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
                    }
                });
    }

    @Override
    public Observable<BaseJson<List<MusicCommentListBean>>> getAblumCommentList(String special_id, Long max_id) {
        return mMusicClient.getAblumCommentList(special_id, max_id,
                Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE))
                .flatMap(new Func1<BaseJson<List<MusicCommentListBean>>, Observable<BaseJson<List<MusicCommentListBean>>>>() {

                    @Override
                    public Observable<BaseJson<List<MusicCommentListBean>>> call
                            (final BaseJson<List<MusicCommentListBean>> listBaseJson) {

                        if (listBaseJson.getData().isEmpty()) {
                            return Observable.just(listBaseJson);
                        } else {
                            final List<Long> user_ids = new ArrayList<>();
                            for (MusicCommentListBean commentListBean : listBaseJson.getData()) {
                                user_ids.add((long) commentListBean.getUser_id());
                                user_ids.add((long) commentListBean.getReply_to_user_id());
                            }

                            return mUserInfoRepository.getUserInfo(user_ids).map(new Func1<BaseJson<List<UserInfoBean>>,
                                    BaseJson<List<MusicCommentListBean>>>() {

                                @Override
                                public BaseJson<List<MusicCommentListBean>> call(BaseJson<List
                                        <UserInfoBean>> userinfobeans) {
                                    if (userinfobeans.isStatus()) { //
                                        // 获取用户信息，并设置动态所有者的用户信息，已以评论和被评论者的用户信息
                                        SparseArray<UserInfoBean> userInfoBeanSparseArray = new
                                                SparseArray<>();
                                        for (UserInfoBean userInfoBean : userinfobeans.getData()) {
                                            userInfoBeanSparseArray.put(userInfoBean.getUser_id()
                                                    .intValue(), userInfoBean);
                                        }
                                        for (MusicCommentListBean commentListBean : listBaseJson
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
                                                commentListBean.setToUserInfoBean(userInfoBeanSparseArray.get(commentListBean.getReply_to_user_id()));
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
                    }
                });
    }

    @Override
    public void sendComment(int reply_id, String content,String path) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", content);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.POST, params);
        backgroundRequestTaskBean.setPath(String.format(path,
                reply_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }
}
