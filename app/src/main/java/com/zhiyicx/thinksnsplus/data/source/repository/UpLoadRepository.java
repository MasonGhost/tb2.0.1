package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.common.net.listener.ProgressRequestBody;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IUploadRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe 上传文件的实现类, 通过dagger注入
 * @date 2017/1/22
 * @contact email:450127106@qq.com
 */

public class UpLoadRepository implements IUploadRepository {
    private CommonClient mCommonClient;
    private UserInfoClient mUserInfoClient;

    // 这个用于服务器校检 hash
    private static final int RETRY_MAX_COUNT = 2; // 最大重试次
    private static final int RETRY_INTERVAL_TIME = 2; // 循环间隔时间 单位 s

    @Inject
    public UpLoadRepository(ServiceManager serviceManager) {
        mCommonClient = serviceManager.getCommonClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
    }

    @Override
    public Observable<BaseJson<Integer>> upLoadSingleFileV2(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight) {
        File file = new File(filePath);
        // 封装上传文件的参数
        final HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("hash", FileUtils.getFileMD5ToString(file));
        paramMap.put("origin_filename", file.getName());
        // 如果是图片就处理图片
        if (isPic) {
            paramMap.put("mime_type", mimeType);
            paramMap.put("width", photoWidth + "");// 如果是图片就选择宽高
            paramMap.put("height", photoHeight + "");// 如果是图片就选择宽高
        } else {
            paramMap.put("mime_type", FileUtils.getMimeType(filePath));
        }
        return checkStorageHash(paramMap.get("hash"))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME) {
                    @Override
                    protected boolean extraReTryCondition(Throwable throwable) {
                        return !throwable.toString().contains("404"); // 文件不存在 服务器返回404.
                    }
                })
                .onErrorReturn(throwable -> {
                    BaseJsonV2 baseJson = new BaseJsonV2();
                    baseJson.setId(-1);
                    return baseJson;
                })
                .flatMap(baseJson -> {
                    if (baseJson.getId() != -1) {
                        BaseJson<Integer> success = new BaseJson<>();
                        success.setData(baseJson.getId());
                        success.setStatus(true);
                        return Observable.just(success);
                    } else {
                        // 封装图片File
                        HashMap<String, String> fileMap = new HashMap<>();
                        fileMap.put("file", filePath);
                        return mCommonClient.upLoadFileByPostV2(UpLoadFile.upLoadFileAndParams(fileMap))
                                .flatMap(uploadFileResultV2 -> {
                                    BaseJson<Integer> success = new BaseJson<>();
                                    success.setData(uploadFileResultV2.getId());
                                    baseJson.setId(1);
                                    success.setStatus(true);
                                    return Observable.just(success);
                                }, Observable::error, () -> null);

                    }
                });
    }

    public Observable<BaseJson<Integer>> upLoadFileWithProgress(final String filePath, String mimeType,
                                                                boolean isPic, int photoWidth, int photoHeight, ProgressRequestBody.ProgressRequestListener listener) {
        File file = new File(filePath);
        // 封装上传文件的参数
        final HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("hash", FileUtils.getFileMD5ToString(file));
        paramMap.put("origin_filename", file.getName());
        // 如果是图片就处理图片
        if (isPic) {
            paramMap.put("mime_type", mimeType);
            paramMap.put("width", photoWidth + "");// 如果是图片就选择宽高
            paramMap.put("height", photoHeight + "");// 如果是图片就选择宽高
        } else {
            paramMap.put("mime_type", FileUtils.getMimeType(filePath));
        }
        return checkStorageHash(paramMap.get("hash"))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME) {
                    @Override
                    protected boolean extraReTryCondition(Throwable throwable) {
                        return !throwable.toString().contains("404"); // 文件不存在 服务器返回404.
                    }
                })
                .onErrorReturn(throwable -> {
                    BaseJsonV2 baseJson = new BaseJsonV2();
                    baseJson.setId(-1);
                    return baseJson;
                })
                .flatMap(baseJson -> {
                    if (baseJson.getId() != -1) {
                        BaseJson<Integer> success = new BaseJson<>();
                        success.setData(baseJson.getId());
                        success.setStatus(true);
                        return Observable.just(success);
                    } else {
                        // 封装图片File
                        HashMap<String, String> fileMap = new HashMap<>();
                        fileMap.put("file", filePath);
                        return mCommonClient.upLoadFileByPostV2(UpLoadFile.upLoadFileAndProgress(fileMap,listener))
                                .flatMap(uploadFileResultV2 -> {
                                    BaseJson<Integer> success = new BaseJson<>();
                                    success.setData(uploadFileResultV2.getId());
                                    baseJson.setId(1);
                                    success.setStatus(true);
                                    return Observable.just(success);
                                }, Observable::error, () -> null);

                    }
                });
    }

    @Override
    public Observable<BaseJsonV2> checkStorageHash(String hash) {
        return mCommonClient.checkStorageHash(hash);
    }

    /**
     * 更新用户头像
     *
     * @param filePath
     * @return
     */
    @Override
    public Observable<Object> uploadAvatar(String filePath) {
        return mUserInfoClient.updateAvatar(getMultipartBody(filePath, "avatar"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Object> uploadBg(String filePath) {
        return mUserInfoClient.updateBg(getMultipartBody(filePath, "image"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    @NonNull
    private MultipartBody getMultipartBody(String filePath, String key) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File(filePath);//filePath 图片地址
        String mimeType = FileUtils.getMimeTypeByFile(file);
        RequestBody imageBody = RequestBody.create(
                MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
        builder.addFormDataPart(key, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        builder.setType(MultipartBody.FORM);//设置类型
        return builder.build();
    }

}
