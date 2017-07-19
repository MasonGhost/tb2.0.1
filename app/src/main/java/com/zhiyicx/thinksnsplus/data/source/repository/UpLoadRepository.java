package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;

import java.io.File;
import java.util.HashMap;

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
    private Context mContext;

    // 这个用于服务器校检 hash
    private static final int RETRY_MAX_COUNT = 2; // 最大重试次
    private static final int RETRY_INTERVAL_TIME = 2; // 循环间隔时间 单位 s

    @Inject
    public UpLoadRepository(ServiceManager serviceManager, Application context) {
        mContext = context;
        mCommonClient = serviceManager.getCommonClient();
        mUserInfoClient = serviceManager.getUserInfoClient();
    }

    @Override
    public Observable<BaseJson<Integer>> upLoadSingleFile(final String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight) {
        File file = new File(filePath);
        // 封装上传文件的参数
        HashMap<String, String> paramMap = new HashMap<>();
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
       /* LogUtils.i("file_options-->" + "mime_type-->" + FileUtils.getMimeType(filePath)
                + "width-->" + options.outWidth
                + "height-->" + options.outHeight
                + "mime-->" + options.outMimeType);*/
        return mCommonClient.createStorageTask(paramMap, null)
                // 处理创建存储任务到上传文件的过程
                .flatMap(new Func1<BaseJson<StorageTaskBean>, Observable<String[]>>() {
                    @Override
                    public Observable<String[]> call(BaseJson<StorageTaskBean> storageTaskBeanBaseJson) {
                        // 服务器获取成功
                        if (storageTaskBeanBaseJson.isStatus()) {
                            StorageTaskBean storageTaskBean = storageTaskBeanBaseJson.getData();
                            int storageId = storageTaskBean.getStorage_id();
                            final int storageTaskId = storageTaskBean.getStorage_task_id();
                            if (storageId > 0) {
                                // 表示服务器已经存在该附件,已经成功上传，不需要做其他事情了
                                return Observable.just(new String[]{"success", storageTaskId + ""});
                            } else {
                                // 创建上传任务成功，开始上传
                                String method = storageTaskBean.getMethod();
                                String uri = storageTaskBean.getUri();
                                Gson gson = new Gson();
                                // 处理 headers
                                HashMap<String, String> headerMap;
                                try {
                                    headerMap = gson.fromJson(gson.toJson(storageTaskBean.getHeaders()),
                                            new TypeToken<HashMap<String, String>>() {
                                            }.getType());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    headerMap = new HashMap<>();
                                }

                                // 处理 options
                                HashMap<String, Object> optionsMap;
                                try {
                                    optionsMap = gson.fromJson(gson.toJson(storageTaskBean.getOptions()),
                                            new TypeToken<HashMap<String, Object>>() {
                                            }.getType());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    optionsMap = new HashMap<>();
                                }
                                //TODO optionsMap在此处添加图片收费参数

                                // 封装图片File
                                HashMap<String, String> fileMap = new HashMap<>();
                                fileMap.put(storageTaskBean.getInput(), filePath);
                                if (method.equalsIgnoreCase("put")) {
                                    // 使用map操作符携带任务id，继续向下传递
                                    return mCommonClient.upLoadFileByPut(uri, headerMap, UpLoadFile.upLoadFileAndParams(fileMap, optionsMap))
                                            .map(new Func1<String, String[]>() {
                                                @Override
                                                public String[] call(String s) {
                                                    return new String[]{s, storageTaskId + ""};
                                                }
                                            });
                                } else if (method.equalsIgnoreCase("post")) {
                                    // 使用map操作符携带任务id，继续向下传递
                                    return mCommonClient.upLoadFileByPost(uri, headerMap, UpLoadFile.upLoadFileAndParams(fileMap, optionsMap))
                                            .map(new Func1<String, String[]>() {
                                                @Override
                                                public String[] call(String s) {
                                                    return new String[]{s, storageTaskId + ""};
                                                }
                                            });
                                } else {
                                    return Observable.just(new String[]{"failure", ""});// 没有合适的方法上传文件，这一般是不会发生的
                                }
                            }
                        } else {
                            // 表示服务器创建存储任务失败
                            return Observable.just(new String[]{"failure", ""});
                        }
                    }
                })
                // 处理上传文件到获取任务通知的过程
                .flatMap(new Func1<String[], Observable<BaseJson<Integer>>>() {
                    @Override
                    public Observable<BaseJson<Integer>> call(final String[] s) {
                        switch (s[0]) {
                            case "success":// 直接成功
                                BaseJson<Integer> success = new BaseJson<Integer>();
                                success.setData(Integer.parseInt(s[1]));
                                success.setStatus(true);
                                return Observable.just(success);
                            case "failure":// 失败
                                BaseJson<Integer> failure = new BaseJson<Integer>();
                                failure.setData(Integer.parseInt(s[1]));
                                failure.setStatus(false);
                                return Observable.just(failure);
                            default:// 调用通知任务
                                return mCommonClient.notifyStorageTask(s[1], s[0], null)
                                        .map(new Func1<BaseJson, BaseJson<Integer>>() {
                                            @Override
                                            public BaseJson<Integer> call(BaseJson baseJson) {
                                                BaseJson<Integer> newBaseJson = new BaseJson<Integer>();
                                                newBaseJson.setCode(baseJson.getCode());
                                                newBaseJson.setStatus(baseJson.isStatus());
                                                newBaseJson.setMessage(baseJson.getMessage());
                                                newBaseJson.setData(Integer.parseInt(s[1]));
                                                return newBaseJson;
                                            }
                                        });
                        }
                    }
                });
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
                .flatMap(new Func1<BaseJsonV2, Observable<BaseJson<Integer>>>() {
                    @Override
                    public Observable<BaseJson<Integer>> call(BaseJsonV2 baseJson) {
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
                                    .flatMap(new Func1<BaseJsonV2, Observable<BaseJson<Integer>>>() {
                                        @Override
                                        public Observable<BaseJson<Integer>> call(BaseJsonV2 uploadFileResultV2) {
                                            if (uploadFileResultV2.getMessage().get(0).equals("上传成功")) {
                                                BaseJson<Integer> success = new BaseJson<>();
                                                success.setData(uploadFileResultV2.getId());
                                                success.setStatus(true);
                                                return Observable.just(success);
                                            } else {
                                                BaseJson<Integer> failure = new BaseJson<>();
                                                failure.setData(uploadFileResultV2.getId());
                                                failure.setStatus(false);
                                                return Observable.just(failure);
                                            }
                                        }
                                    });

                        }
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
        MultipartBody.Builder builder = new MultipartBody.Builder();
        File file = new File(filePath);//filePath 图片地址
        String mimeType = FileUtils.getMimeTypeByFile(file);
        RequestBody imageBody = RequestBody.create(
                MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
        builder.addFormDataPart("avatar", file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        builder.setType(MultipartBody.FORM);//设置类型
        MultipartBody multipartBody = builder.build();
        return mUserInfoClient.updateAvatar(multipartBody)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

}
