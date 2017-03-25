package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsConstract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailsRepository implements InfoDetailsConstract.Repository {

    InfoMainClient mInfoMainClient;
    private Context context;

    @Inject
    public InfoDetailsRepository(ServiceManager serviceManager, Application context) {
        mInfoMainClient = serviceManager.getInfoMainClient();
        this.context = context;
    }

    @Override
    public Observable<BaseJson<List<InfoCommentListBean>>> getInfoCommentList(String feed_id,
                                                                              Long max_id, Long
                                                                                      limit) {
        return mInfoMainClient.getInfoCommentList(feed_id, max_id, null);
    }

    @Override
    public void handleCollect(boolean isCollected, final String news_id) {
        Observable.just(isCollected)
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
                (BackgroundTaskRequestMethodConfig.SEND_COMMENT, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_INFO_COMMENT_FORMAT,
                new_id));
        BackgroundTaskManager.getInstance(context).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }
}
