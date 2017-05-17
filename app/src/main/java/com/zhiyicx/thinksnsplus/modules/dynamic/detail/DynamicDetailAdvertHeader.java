package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/05/17/17:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicDetailAdvertHeader {

    private View mRootView;
    private LinearLayout mAdvertContainer;
    private TextView mTitle;
    private Context mContext;

    public DynamicDetailAdvertHeader(Context context) {
        mContext = context;
        mRootView = LayoutInflater.from(context).inflate(R.layout.advert_details, null);
        mTitle = (TextView) mRootView.findViewById(R.id.tv_advert_title);
        mAdvertContainer = (LinearLayout) mRootView.findViewById(R.id.fl_advert_container);
    }

    public DynamicDetailAdvertHeader(Context context, View rootView) {
        mContext = context;
        mRootView = rootView;
        mTitle = (TextView) mRootView.findViewById(R.id.tv_advert_title);
        mAdvertContainer = (LinearLayout) mRootView.findViewById(R.id.fl_advert_container);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setAdverts(List<String> adverts) {

    }

    public void hideAdvert() {
        mRootView.setVisibility(View.GONE);
    }
}
