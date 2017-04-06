package com.zhiyicx.thinksnsplus.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.ZipHelper;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * @Describe 处理服务器数据 适用 ＲＥＳＥＴＦＵＬ　ＡＰＩ
 * @Author Jungle68
 * @Date 2017/1/20
 * @Contact master.jungle68@gmail.com
 */

public abstract class BaseSubscribe<T> extends Subscriber<BaseJson<T>> {
    private static final String TAG = "BaseSubscribe";

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            Response response = ((HttpException) e).response();
            try {
                if (response != null && response.errorBody() != null) {
                    //读取服务器返回的结果
                    ResponseBody responseBody = response.errorBody();
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    //获取content的压缩类型
                    String encoding = response
                            .headers()
                            .get("Content-Encoding");
                    Buffer clone = buffer.clone();
                    String bodyString;

                    //解析response content
                    if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
                        bodyString = ZipHelper.decompressForGzip(clone.readByteArray());//解压
                    } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
                        bodyString = ZipHelper.decompressToStringForZlib(clone.readByteArray());//解压
                    } else {//content没有被压缩
                        Charset charset = Charset.forName("UTF-8");
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(charset);
                        }
                        bodyString = clone.readString(charset);
                    }
                    // 打印返回的json结果
                    LogUtils.json(TAG, bodyString);
                    BaseJson tBaseJson = new Gson().fromJson(bodyString, BaseJson.class);
                    // 数据发射成功，该数据为 BaseJson 的泛型类
                    handleStatus(tBaseJson);

                } else {
                    handleError(e);
                }
            } catch (IOException e1) {
                onException(e1);
            } catch (JsonSyntaxException e1) {
                onException(e1);
            }
        } else {
            handleError(e);
        }

    }

    private void handleError(Throwable e) {
        e.printStackTrace();
        onException(e);
    }

    @Override
    public void onNext(BaseJson<T> tBaseJson) {
        // 数据发射成功，该数据为 BaseJson 的泛型类
        handleStatus(tBaseJson);
    }

    /**
     * 处理数据，按照 resutful api 要求，204 不反回数据
     *
     * @param tBaseJson
     */
    private void handleStatus(BaseJson<T> tBaseJson) {
        if (tBaseJson != null) {
            boolean status = tBaseJson.isStatus();
            int code = tBaseJson.getCode();
            String message;

            if (status) {
                onSuccess(tBaseJson.getData());
            } else {
                // 状态值为 false 或者 code 不为 0 需要尝试从映射表中查找消息
                if (code != 0) {
                    String codeName = String.format(BaseApplication.getContext().getString(R.string.code_identify), code);
                    int id = UIUtils.getResourceByName(codeName, "string", BaseApplication.getContext());
                    if (id != 0) { // 没找到，返回 0
                        message = BaseApplication.getContext().getString(id);
                    } else {
                        message = BaseApplication.getContext().getString(R.string.code_not_define);
                    }
                } else {
                    message = tBaseJson.getMessage();
                }
                if (TextUtils.isEmpty(message)) {
                    // 重新设置 message
                    tBaseJson.setMessage(BaseApplication.getContext().getString(R.string.code_not_define));
                } else {
                    tBaseJson.setMessage(message);
                }
                onFailure(tBaseJson.getMessage(), tBaseJson.getCode());
            }
        } else { // 204 等没有结构体的数据
            onSuccess(null);
        }
    }

    /**
     * 服务器正确处理返回正确数据
     *
     * @param data 正确的数据
     */
    protected abstract void onSuccess(T data);

    /**
     * 服务器正确接收到请求，主动返回错误状态以及数据
     *
     * @param message 错误信息
     * @param code
     */
    protected abstract void onFailure(String message, int code);

    /**
     * 系统级错误，网络错误，系统内核错误等
     *
     * @param throwable
     */
    protected abstract void onException(Throwable throwable);

}
