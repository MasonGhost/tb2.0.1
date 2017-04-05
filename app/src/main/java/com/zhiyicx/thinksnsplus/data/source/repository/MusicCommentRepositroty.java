package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MusicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentContract;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentRepositroty implements MusicCommentContract.Repository {

    MusicClient mMusicClient;
    Context mContext;

    @Inject
    public MusicCommentRepositroty(Application application,ServiceManager serviceManager) {
        mMusicClient = serviceManager.getMusicClient();
        mContext=application;
    }

    @Override
    public Observable<BaseJson<List<MusicCommentListBean>>> getMusicCommentList(String music_id,
                                                                                long max_id) {
        return mMusicClient.getMusicCommentList(music_id,max_id, Long.valueOf(TSListFragment.DEFAULT_PAGE_SIZE));
    }

    @Override
    public void sendComment(String musci_id, String content) {
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", content);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.POST, params);
        backgroundRequestTaskBean.setPath(String.format(ApiConfig.APP_PATH_MUSIC_COMMENT_FORMAT,
                musci_id));
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }
}
