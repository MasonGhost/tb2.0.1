package com.zhiyicx.baseproject.widget.infohint;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.base.TSApplication;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ToastUtils;

/**
 * @author LiuChao
 * @describe 每次进行网络请求或者类似的操作，需要进行提示，比如发送失败，已发送之类的
 * @date 2017/1/7
 * @contact email:450127106@qq.com
 */

public class ShowHintInfo {
    /**
     * 通过toast自定义布局，发送失败提示
     */
    public static void showSendError(Integer imgRsId, String hintContent) {
        View view = LayoutInflater.from(BaseApplication.getContext()).inflate(R.layout.view_hint_info1, null);
        if (imgRsId == null || TextUtils.isEmpty(hintContent)) {
            // 没有图片或者内容，就不要使用该方法，直接去ToastUtils里面找吧
            throw new IllegalArgumentException(BaseApplication.getContext().getString(R.string.erro_argument_in_method));
        }
        ((ImageView) (view.findViewById(R.id.iv_hint_img))).setImageResource(imgRsId);
        ((TextView) (view.findViewById(R.id.tv_hint_text))).setText(hintContent);
        ToastUtils.showToast(view, BaseApplication.getContext());
    }

    /**
     * 通过toast自定义布局，发送成功提示
     */
    public static void showSendSuccess() {

    }
}
