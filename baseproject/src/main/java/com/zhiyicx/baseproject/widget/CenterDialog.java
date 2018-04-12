package com.zhiyicx.baseproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import com.zhiyicx.baseproject.R;

public class CenterDialog extends Dialog{

    private LinearLayout mLayWordSuccess;
    private ImageView mImgTip;
    private TextView mTvTip;
    private LinearLayout mLayWordFinish;
    private TextView mTvTitle;
    private TextView mTvNotice;
    private TextView mTvConfirm;
    private TextView mTvCancel;
    private View mContentView;
    private Handler mHandler;
    private Runnable mRunnable;
    private OnConfirmClickListener mOnConfirmClickListener;
    public final static int SUCCESS = 0;
    public final static int FINISH = 1;
    private int mCurrentView;//0 success; 1 finish

    public CenterDialog(@NonNull Context context) {
        super(context, R.style.CenterDialog);
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        };
        setCanceledOnTouchOutside(false);
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_center, null);
        setContentView(mContentView);
        mLayWordSuccess = (LinearLayout) mContentView.findViewById(R.id.ll_word_success);
        mImgTip = (ImageView) mContentView.findViewById(R.id.img_tip);
        mTvTip = (TextView) mContentView.findViewById(R.id.tv_tip);
        mLayWordFinish = (LinearLayout) mContentView.findViewById(R.id.ll_word_finish);
        mTvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        mTvNotice = (TextView) mContentView.findViewById(R.id.tv_notice);
        mTvConfirm = (TextView) mContentView.findViewById(R.id.tv_confirm);
        mTvCancel = (TextView) mContentView.findViewById(R.id.tv_cancel);
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnConfirmClickListener != null){
                    mOnConfirmClickListener.onConfirmClick();
                }
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public interface OnConfirmClickListener{
        void onConfirmClick();
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener){
        this.mOnConfirmClickListener = onConfirmClickListener;
    }

    @Override
    public void show() {
        switch (mCurrentView){
            case 0: {
                mLayWordSuccess.setVisibility(View.VISIBLE);
                mLayWordFinish.setVisibility(View.GONE);
                mHandler.postDelayed(mRunnable, 2000);
                break;
            }
            case 1: {
                mLayWordFinish.setVisibility(View.VISIBLE);
                mLayWordSuccess.setVisibility(View.GONE);
                mHandler.removeCallbacks(mRunnable);
                break;
            }
            default:
                break;
        }
        super.show();
    }

    public void setMtitle(String title){
        mTvTitle.setText(title);
    }

    public void setNotice(String notice){
        mTvNotice.setText(notice);
    }

    public void setConfirm(String confirm){
        mTvConfirm.setText(confirm);
    }

    public void setCancel(String cancel){
        mTvCancel.setText(cancel);
    }

    public void setCurrentView(int currentView){
        this.mCurrentView = currentView;
    }

    public void setImgTipSrc(Drawable drawable){
        mImgTip.setImageDrawable(drawable);
    }

    public void setTvTip(String text){
        mTvTip.setText(text);
    }
}
