package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe 上传文件的实现类, 通过dagger注入
 * @date 2017/1/22
 * @contact email:450127106@qq.com
 */

public class UpLoadRepository implements IUploadRepository {
    private CommonClient mCommonClient;
    private Context mContext;

    @Inject
    public UpLoadRepository(ServiceManager serviceManager, Application context) {
        mContext = context;
        mCommonClient = serviceManager.getCommonClient();
    }

    @Override
    public Observable<BaseJson<Integer>> upLoadSingleFile(final String params, final String filePath, boolean isPic) {
        File file = new File(filePath);
        // 封装上传文件的参数
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("hash", FileUtils.getFileMD5ToString(file));
        paramMap.put("origin_filename", file.getName());
        // 如果是图片就处理图片
        if (isPic) {
            BitmapFactory.Options options = DrawableProvider.getPicsWHByFile(filePath);
            paramMap.put("mime_type", options.outMimeType);
            paramMap.put("width", options.outWidth + "");// 如果是图片就选择宽高
            paramMap.put("height", options.outHeight + "");// 如果是图片就选择宽高
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
                                // 处理headers
                                Object headers = storageTaskBean.getHeaders();
                                HashMap<String, String> headerMap = parseJSONObject(headers);
                                // 封装图片File
                                HashMap<String, String> fileMap = new HashMap<String, String>();
                                fileMap.put(params, filePath);
                                if (method.equalsIgnoreCase("put")) {
                                    // 使用map操作符携带任务id，继续向下传递
                                    return mCommonClient.upLoadFileByPut(uri, headerMap, UpLoadFile.upLoadMultiFile(fileMap))
                                            .map(new Func1<String, String[]>() {
                                                @Override
                                                public String[] call(String s) {
                                                    return new String[]{s.toString(), storageTaskId + ""};
                                                }
                                            });
                                } else if (method.equalsIgnoreCase("post")) {
                                    // 使用map操作符携带任务id，继续向下传递
                                    return mCommonClient.upLoadFileByPost(uri, headerMap, UpLoadFile.upLoadMultiFile(fileMap))
                                            .map(new Func1<String, String[]>() {
                                                @Override
                                                public String[] call(String s) {
                                                    return new String[]{s.toString(), storageTaskId + ""};
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

    /**
     * 处理header，传入retrofit中
     *
     * @param object
     * @return
     */
    private HashMap<String, String> parseJSONObject(Object object) {

        if (object == null) {
            return null;
        }
        String jsonString = object.toString();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            HashMap<String, String> jsonMap = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                try {
                    String key = iterator.next();
                    jsonMap.put(key, jsonObject.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
