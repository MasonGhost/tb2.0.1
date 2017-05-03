package com.zhiyicx.thinksnsplus.comment;

import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.util.HashMap;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * @Author Jliuer
 * @Date 2017/04/12/9:44
 * @Email Jliuer@aliyun.com
 * @Description 删除评论处理类
 */
public class DeleteComment implements ICommentEvent<ICommentBean> {

    private BackgroundTaskHandler.OnNetResponseCallBack mCallBack;

    @Inject
    ServiceManager serviceManager;

    CommonCommentClient mCommonCommentClient;

    public DeleteComment() {
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        mCommonCommentClient = serviceManager.getCommonCommentClient();
    }

    @Override
    public void setListener(BackgroundTaskHandler.OnNetResponseCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void handleCommentInBackGroud(ICommentBean comment) {
        CommonMetadata commentBean = comment.get$$Comment();
        deleteComment(commentBean);
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put(BackgroundTaskHandler.NET_CALLBACK, mCallBack);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.DELETE, params);
        backgroundRequestTaskBean.setPath(commentBean.getString(CommonMetadata.METADATA_KEY_DELETE_URL));
        BackgroundTaskManager.getInstance(BaseApplication.getContext()).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public void handleComment(ICommentBean comment) {
        CommonMetadata commentBean = comment.get$$Comment();
        mCommonCommentClient.deleteComment(commentBean.getString(CommonMetadata.METADATA_KEY_DELETE_URL))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        if (mCallBack!=null){
                            mCallBack.onSuccess(data);
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (mCallBack!=null){
                            mCallBack.onFailure(message, code);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        if (mCallBack!=null){
                            mCallBack.onException(throwable);
                        }
                    }
                });

    }

    protected void deleteComment(CommonMetadata commentBean) {

    }
}
