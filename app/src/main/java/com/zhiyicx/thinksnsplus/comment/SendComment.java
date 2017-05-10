package com.zhiyicx.thinksnsplus.comment;

import android.text.TextUtils;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskHandler;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:24
 * @Email Jliuer@aliyun.com
 * @Description 发送评论处理类
 */
public class SendComment implements ICommentEvent<ICommentBean> {

    private BackgroundTaskHandler.OnNetResponseCallBack mCallBack;

    @Inject
    ServiceManager serviceManager;

    CommonCommentClient mCommonCommentClient;

    public SendComment() {
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
        commentBean.newBuilder().putState(CommonMetadata.METADATA_KEY_COMMENT_STATE, CommonMetadata.SEND_ING);
        sendComment(commentBean);
        BackgroundRequestTaskBean backgroundRequestTaskBean;
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", commentBean.getString(CommonMetadata.METADATA_KEY_COMMENT_CONTENT));
        params.put("comment_mark", commentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
        params.put("reply_to_user_id", commentBean.getInteger(CommonMetadata.METADATA_KEY_TARGET_ID));

        params.put(BackgroundTaskHandler.NET_CALLBACK, mCallBack);
        // 后台处理
        backgroundRequestTaskBean = new BackgroundRequestTaskBean
                (BackgroundTaskRequestMethodConfig.POST, params);
        backgroundRequestTaskBean.setPath(commentBean.getString(CommonMetadata.METADATA_KEY_COMMENT_URL));
        BackgroundTaskManager.getInstance(BaseApplication.getContext()).addBackgroundRequestTask
                (backgroundRequestTaskBean);
    }

    @Override
    public void handleComment(ICommentBean comment) {
        CommonMetadata commentBean = comment.get$$Comment();
        HashMap<String, Object> params = new HashMap<>();
        params.put("comment_content", commentBean.getString(CommonMetadata.METADATA_KEY_COMMENT_CONTENT));
        params.put("reply_to_user_id", commentBean.getInteger(CommonMetadata.METADATA_KEY_TO_USER_ID));
        params.put("comment_mark", commentBean.getLong(CommonMetadata.METADATA_KEY_COMMENT_MARK));
        mCommonCommentClient.sendComment(commentBean.getString(CommonMetadata.METADATA_KEY_COMMENT_URL),
                upLoadFileAndParams(null, params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
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

    protected void sendComment(CommonMetadata commentBean) {

    }

    public List<MultipartBody.Part> upLoadFileAndParams(Map<String, String> filePathList, HashMap<String, Object> params) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().toString());//ParamKey.TOKEN 自定义参数key常量类，即参数名
            }
        }
        if (filePathList != null) {
            Set<String> filePathKey = filePathList.keySet();
            for (String fileParam : filePathKey) {
                try {
                    File file = new File(filePathList.get(fileParam));//filePath 图片地址
                    String mimeType = FileUtils.getMimeTypeByFile(file);
                    RequestBody imageBody = RequestBody.create(
                            MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
                    builder.addFormDataPart(fileParam, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果没有任何参数传入，又调用了该方法，需要传一个缺省参数， Multipart body must have at least one part.
        if ((params == null || params.isEmpty()) && (filePathList == null || filePathList.isEmpty())) {
            builder.addFormDataPart("hehe", "hehe");
        }
        return  builder.build().parts();
    }
}
