package com.zhiyicx.thinksnsplus.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;

/**
 * @author LiuChao
 * @describe 提示信息弹框，包括发送失败，发送成功，正在发送
 * @date 2017/2/5
 * @contact email:450127106@qq.com
 */

public class LoadingDialogUtils {
    private static AlertDialog sLoadingDialog;
    private static AnimationDrawable mAnimationDrawable;
    private static View layoutView;
    private static final int SUCCESS_ERROR_STATE_TIME = 1000;// 成功或者失败的停留时间
    private static final int HANDLE_DELAY = 0;
    private static ImageView iv_hint_img;
    private static TextView tv_hint_text;

    /**
     * 显示错误的状态
     *
     * @param context
     */
    public static void showStateError(Context context) {
        initDialog(R.mipmap.msg_box_remind, "发送失败", context, false);
        sendHideMessage(context);
    }

    /**
     * 显示成功的状态
     *
     * @param context
     */
    public static void showStateSuccess(Context context) {
        initDialog(R.mipmap.msg_box_succeed, "发送成功", context, false);
        sendHideMessage(context);
    }

    /**
     * 显示进行中的状态
     *
     * @param context
     */
    public static void showStateIng(Context context) {
        initDialog(R.drawable.frame_loading_grey, "发送中...", context, false);
        handleAnimation(true);
    }

    /**
     * 进行中的状态变为结束
     *
     * @param context
     */
    public static void showStateEnd(Context context) {
        handleAnimation(false);
        hideDialog(context);
    }

    /**
     * 处理动画
     *
     * @param status true 开启动画，false 关闭动画
     */
    private static void handleAnimation(boolean status) {
        mAnimationDrawable = (AnimationDrawable) iv_hint_img.getDrawable();
        if (mAnimationDrawable == null)
            throw new IllegalArgumentException("load animation not be null");
        if (status) {
            if (!mAnimationDrawable.isRunning()) {
                mAnimationDrawable.start();
            }
        } else {
            if (mAnimationDrawable.isRunning()) {
                mAnimationDrawable.stop();
            }
        }
    }


    private static void initDialog(Integer imgRsId, String hintContent, Context context, boolean outsideCancel) {
        if (sLoadingDialog == null) {
            layoutView = LayoutInflater.from(context).inflate(com.zhiyicx.baseproject.R.layout.view_hint_info1, null);
            iv_hint_img = (ImageView) layoutView.findViewById(com.zhiyicx.baseproject.R.id.iv_hint_img);
            tv_hint_text = (TextView) layoutView.findViewById(com.zhiyicx.baseproject.R.id.tv_hint_text);
            sLoadingDialog = new AlertDialog.Builder(context, R.style.loadingDialogStyle)
                    .setCancelable(outsideCancel)
                    .create();
            sLoadingDialog.setCanceledOnTouchOutside(outsideCancel);
        }
        tv_hint_text.setText(hintContent);
        iv_hint_img.setImageResource(imgRsId);
        showDialog(context);
        sLoadingDialog.setContentView(layoutView);// 必须放在show方法后面
    }

    /**
     * 发送关闭窗口的延迟消息
     *
     * @param context
     */
    private static void sendHideMessage(Context context) {
        Message message = Message.obtain();
        message.what = HANDLE_DELAY;
        message.obj = context;
        mHandler.sendMessageDelayed(message, SUCCESS_ERROR_STATE_TIME);
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_DELAY && sLoadingDialog != null && msg.obj != null) {
                Context context = (Context) msg.obj;
                hideDialog(context);
            }
        }
    };

    // Dialog有可能在activity销毁后，调用，这样会发生dialog找不到窗口的错误，所以需要先判断是否有activity
    private static void showDialog(Context context) {
        if (isValidContext((Activity) context)) {
            sLoadingDialog.show();
        }
    }

    private static void hideDialog(Context context) {
        if (isValidContext((Activity) context)) {
            sLoadingDialog.dismiss();
        }
    }

    /**
     * 判断一个界面是否还存在
     * 使用场景：比如  一个activity被销毁后，它的dialog还要执行某些操作，比如dismiss和show这样是不可以的
     * 因为 dialog是属于activity的
     *
     * @param c
     * @return
     */
    @TargetApi(17)
    private static boolean isValidContext(Activity c) {
        if (c == null) {
            return false;
        }

        if (c.isDestroyed() || c.isFinishing()) {
            return false;
        } else {
            return true;
        }
    }
}
