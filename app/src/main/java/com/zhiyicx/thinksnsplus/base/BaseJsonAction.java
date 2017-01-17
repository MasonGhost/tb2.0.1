package com.zhiyicx.thinksnsplus.base;

import android.text.TextUtils;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;

import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe 集中处理服务器返回的 code 和 message 消息
 * @date 2017/1/14
 * @contact email:450127106@qq.com
 */

public class BaseJsonAction<T> implements Action1<BaseJson<T>> {

    @Override
    public void call(BaseJson<T> tBaseJson) {
        // 数据发射成功，该数据为 BaseJson 的泛型类
        if (tBaseJson != null) {
            boolean status = tBaseJson.isStatus();
            int code = tBaseJson.getCode();
            String message = tBaseJson.getMessage();

            // 状态值为 false 或者 code 不为 0，并且此时的消息为空，需要尝试从映射表中查找消息
            if (code != 0 && TextUtils.isEmpty(message)) {
                String codeName = String.format(BaseApplication.getContext().getString(R.string.code_identify), code);
                int id = UIUtils.getResourceByName(codeName, "string", BaseApplication.getContext());
                if (id != 0) { // 没找到，返回 0
                    message = BaseApplication.getContext().getString(id);
                } else {
                    message = BaseApplication.getContext().getString(R.string.code_not_define);
                }
            }

            if (status) {
                onSuccess(tBaseJson.getData());
            } else {
                if (TextUtils.isEmpty(tBaseJson.getMessage())) {
                    // 重新设置 message
                    tBaseJson.setMessage(message);
                }
                onFailure(tBaseJson.getMessage());
            }
        }
    }

    protected void onSuccess(T data) {

    }

    protected void onFailure(String message) {

    }
}
