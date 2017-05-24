package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/05/17/17:32
 * @Email Jliuer@aliyun.com
 * @Description 详情广告
 */
public class DynamicDetailAdvertHeader {

    private View mRootView;
    private LinearLayout mAdvertContainer;
    private TextView mTitle;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public DynamicDetailAdvertHeader(Context context) {
        this(context, LayoutInflater.from(context).inflate(R.layout.advert_details, null));
    }

    public DynamicDetailAdvertHeader(Context context, View rootView) {
        mContext = context;
        mRootView = rootView;
        mTitle = (TextView) mRootView.findViewById(R.id.tv_advert_title);
        mAdvertContainer = (LinearLayout) mRootView.findViewById(R.id.fl_advert_container);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setAdverts(List<String> adverts) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, mContext.getResources().getDimensionPixelSize(R.dimen.channel_advert_height));
        params.weight = 1;
        adverts = adverts.subList(0, adverts.size() >= 3 ? 3 : adverts.size());
        for (int i = 0; i < adverts.size(); i++) {
            FilterImageView imageView = new FilterImageView(mContext);
            imageView.setImageResource(R.mipmap.icon_256);
            imageView.setLayoutParams(params);
            mAdvertContainer.addView(imageView);
            final int position = i;
            final String url = adverts.get(position);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemClik(v, position, url);
                }
            });
        }
    }

    public void hideAdvert() {
        mRootView.setVisibility(View.GONE);
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, height);
        params.setMargins(20, 20, 20, 20);
        mAdvertContainer.setLayoutParams(params);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClik(View v, int position, String url);
    }
}
