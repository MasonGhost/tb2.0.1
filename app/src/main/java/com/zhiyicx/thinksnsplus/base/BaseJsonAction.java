package com.zhiyicx.thinksnsplus.base;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import rx.functions.Action1;

/**
 * @author LiuChao
 * @describe 集中处理服务器返回的code和message消息
 * @date 2017/1/14
 * @contact email:450127106@qq.com
 */

public class BaseJsonAction<T> implements Action1<BaseJson<T>> {
    private static final int MESSAGE_CODE_COUNT = 17;

    @Override
    public void call(BaseJson<T> tBaseJson) {
        // 数据发射成功，该数据为BaseJson的泛型类
        if (tBaseJson != null) {
            int code = tBaseJson.getCode();
            String message = tBaseJson.getMessage();
            boolean status = tBaseJson.isStatus();
            // 状态值为false或者code不为0，并且此时的消息为空，需要尝试从映射表中查找消息
            if (code != 0 && TextUtils.isEmpty(message)) {
                for (int i = 0; i < MESSAGE_CODE_COUNT; i++) {
                    String codeName = "code_" + 1000 + i;
                    int id = UIUtils.getResourceByName(codeName, "string", BaseApplication.getContext());
                    if (id != 0) { // 没找到，返回0
                        message = BaseApplication.getContext().getString(id);
                        break;
                    } else {
                        continue;
                    }
                }
            }
            // 测试用的code
            if (code == 9999) {
                message = BaseApplication.getContext().getString(R.string.code_9999);
            }
            // 重新设置message
            tBaseJson.setMessage(message);
        }
    }
}
