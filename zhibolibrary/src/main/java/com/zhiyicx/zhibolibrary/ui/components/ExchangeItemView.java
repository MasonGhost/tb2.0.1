package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhiyicx.zhibolibrary.R;


/**
 * Created by jess on 2015/11/19.
 */
public class ExchangeItemView extends RelativeLayout {

    private View mRootview;
    private String mTitle;
    private String mSubtitle;
    private int mIcon1;
    private int mIcon2;
    private boolean mDivider;


    ImageView mIcon1IV;
    ImageView mIcon2IV;
    ImageView mDividerIV;
    TextView mTitleTV;
    TextView mSubTitleTV;
    public final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    public ExchangeItemView(Context context) {
        this(context, null);
    }

    public ExchangeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            mTitle = attrs.getAttributeValue(NAMESPACE, "exchange_title");
            mSubtitle = attrs.getAttributeValue(NAMESPACE, "exchange_subtitle");
            mIcon1 = attrs.getAttributeResourceValue(NAMESPACE, "exchange_icon1", 0);
            mIcon2 = attrs.getAttributeResourceValue(NAMESPACE, "exchange_icon2", 0);
            mDivider = attrs.getAttributeBooleanValue(NAMESPACE, "exchange_cleanDivider", false);
        }
        init();
    }

    public ExchangeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootview = View.inflate(this.getContext(), R.layout.zb_item_star_exchange, this);

         mIcon1IV= (ImageView) mRootview.findViewById(R.id.iv_exchange_icon1);
        mIcon2IV= (ImageView) mRootview.findViewById(R.id.iv_exchange_icon2);
        mDividerIV= (ImageView) mRootview.findViewById(R.id.iv_exchange_divider);
        mTitleTV= (TextView) mRootview.findViewById(R.id.tv_exchange_title);
        mSubTitleTV= (TextView) mRootview.findViewById(R.id.tv_exchange_subtitle);


        if (!TextUtils.isEmpty(mTitle)) {
            mTitleTV.setText(mTitle);
        }
        if (!TextUtils.isEmpty(mSubtitle)) {
            mSubTitleTV.setText(mSubtitle);
        }

        if (mIcon1 != 0) {
            mIcon1IV.setBackgroundResource(mIcon1);
        }

        if (mIcon2 != 0) {
            mIcon2IV.setBackgroundResource(mIcon2);
        }

        if (mDivider) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDividerIV.getLayoutParams();
            params.width = 0;
        }

        setBackgroundResource(R.drawable.selector_mine_press);
    }

    public void setIcon1Visible(boolean isVisible) {
        mIcon1IV.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setIcon2Visible(boolean isVisible) {
        mIcon2IV.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(String name) {
        mTitleTV.setText(name);
    }

    public void setSubtitle(String action) {
        mSubTitleTV.setText(action);
    }

    public void setIcon1(int res) {
        mIcon1IV.setBackgroundResource(res);
    }

    public void setIcon2(int res) {
        mIcon2IV.setBackgroundResource(res);
    }

    public void cleanDivider(boolean isClean) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDividerIV.getLayoutParams();
        if (isClean) {
            params.width = 0;
        } else {
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        }
    }


}
