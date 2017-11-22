package com.zhiyicx.zhibolibrary.ui.components;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.zhiyicx.zhibolibrary.R;


/**
 * Created by jess on 2015/12/28.
 */
public class LoadingDialog extends AlertDialog {
    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zb_dialog_loading);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
