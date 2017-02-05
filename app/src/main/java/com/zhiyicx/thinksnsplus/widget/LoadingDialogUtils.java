package com.zhiyicx.thinksnsplus.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private static LoadingDialog sLoadingDialog;
    private static final int SUCCESS_ERROR_STATE_TIME = 1000;// 成功或者失败的停留时间
    private static final int HANDLE_DELAY = 0;

    public static void showStateError(Context context) {
        showState(R.mipmap.msg_box_succeed, "发送成功", context, false);
        mHandler.sendEmptyMessageDelayed(HANDLE_DELAY, SUCCESS_ERROR_STATE_TIME);
    }

    public static void showStateSuccess(Context context) {
        showState(R.mipmap.msg_box_remind, "发送成功", context, false);
        mHandler.sendEmptyMessageDelayed(HANDLE_DELAY, SUCCESS_ERROR_STATE_TIME);
    }

    public static void showStateIng(Context context) {

    }

    public static void hideDialog() {
        sLoadingDialog.hideDialog();
    }

    private static void showState(Integer imgRsId, String hintContent, Context context, boolean outsideCancel) {
        if (sLoadingDialog == null) {
            sLoadingDialog = LoadingDialog.initLoadingDialog(context, outsideCancel);
        }
        sLoadingDialog.setMessage(hintContent);
        sLoadingDialog.setImgRes(imgRsId);
        sLoadingDialog.showDialog();
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLE_DELAY && sLoadingDialog != null) {
                sLoadingDialog.hideDialog();
            }
        }
    };

    private static class LoadingDialog extends AlertDialog {
        private TextView tips_loading_msg;
        private String message = "";
        private int imgRes;

        private Context context;

        //修改message
        public void setMessage(String message) {
            this.message = message;
        }

        public void setImgRes(int imgRes) {
            this.imgRes = imgRes;
        }


        /**
         * 构造方法
         *
         * @param context 上下文
         */
        public LoadingDialog(Context context, String message, int imgRes) {
            super(context,R.style.loadingDialogStyle);
            this.message = message;
            this.imgRes = imgRes;
            this.context = context;
        }

        public LoadingDialog(Context context) {
            super(context,R.style.loadingDialogStyle);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setContentView(com.zhiyicx.baseproject.R.layout.view_hint_info1);
            ImageView iv_hint_img = (ImageView) findViewById(com.zhiyicx.baseproject.R.id.iv_hint_img);
            TextView tv_hint_text = (TextView) findViewById(com.zhiyicx.baseproject.R.id.tv_hint_text);
            iv_hint_img.setImageResource(imgRes);
            tv_hint_text.setText(message);

        }

        // Dialog有可能在activity销毁后，调用，这样会发生dialog找不到窗口的错误，所以需要先判断是否有activity
        public void showDialog() {
            if (isValidContext((Activity) context)) {
                show();
            }
        }

        public void hideDialog() {
            if (isValidContext((Activity) context)) {
                dismiss();
            }
        }

        /**
         * 创建LoadingDialog对象
         *
         * @param context
         * @param outCancelable 点击空白处是否可以取消dialog
         * @return
         */
        public static LoadingDialog initLoadingDialog(Context context, boolean outCancelable) {
            LoadingDialog dialog = new LoadingDialog(context);
            dialog.setCancelable(outCancelable);
            dialog.setCanceledOnTouchOutside(outCancelable);//点击可取消
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    //dissmiss后可能需要做些事情
                }
            });
            return dialog;
        }

        /**
         * 默认点击空白处取消dialog
         *
         * @param context
         * @return
         */
        public static LoadingDialog initLoadingDialog(Context context) {
            return initLoadingDialog(context, true);
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
}
