package com.zhiyicx.thinksnsplus.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.zhiyicx.thinksnsplus.R;

public class CenterDialog extends Dialog{

    private ImageView mImgTip;
    private TextView mTvTip;
    private View mContentView;
    private Handler mHandler;
    private Runnable mRunnable;

    public CenterDialog(@NonNull Context context) {
        super(context, R.style.CenterDialog);
        mHandler = new Handler();
        mRunnable = () -> dismiss();
        setCanceledOnTouchOutside(false);
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_center, null);
        setContentView(mContentView);
        mImgTip = (ImageView) mContentView.findViewById(R.id.img_tip);
        mTvTip = (TextView) mContentView.findViewById(R.id.tv_tip);
    }

    @Override
    public void show() {
        super.show();
        mHandler.postDelayed(mRunnable, 2000);
    }

    public void setImgTipSrc(Drawable drawable){
        mImgTip.setImageDrawable(drawable);
    }

    public void setmTvTipTxt(String text){
        mTvTip.setText(text);
    }
}
