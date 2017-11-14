package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;

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
    private LinearLayout mLLAdvert;
    private LinearLayout mLLAdvertTag;
    private TextView mTitle;
    private Context mContext;
    private List<RealAdvertListBean> mAdvertListBeans;

    private OnItemClickListener mOnItemClickListener;

    public DynamicDetailAdvertHeader(Context context) {
        this(context, LayoutInflater.from(context).inflate(R.layout.advert_details, null));
    }

    public DynamicDetailAdvertHeader(Context context, View rootView) {
        mContext = context;
        mRootView = rootView;
        mTitle = (TextView) mRootView.findViewById(R.id.tv_advert_title);
        mAdvertContainer = (LinearLayout) mRootView.findViewById(R.id.fl_advert_container);
        mLLAdvertTag = (LinearLayout) mRootView.findViewById(R.id.ll_advert_tag);
        mLLAdvert = (LinearLayout) mRootView.findViewById(R.id.ll_advert);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setAdverts(List<RealAdvertListBean> adverts) {
        mAdvertListBeans = adverts;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, mContext.getResources().getDimensionPixelSize(R.dimen.channel_advert_height));
        params.weight = 1;
        adverts = adverts.subList(0, adverts.size() >= 3 ? 3 : adverts.size());
        for (int i = 0; i < adverts.size(); i++) {
            FilterImageView imageView = new FilterImageView(mContext);
            imageView.setImageResource(R.mipmap.icon);
            imageView.setLayoutParams(params);
            mAdvertContainer.addView(imageView);
            final int position = i;
            final String url = adverts.get(i).getAdvertFormat().getImage().getImage();
            final String link = adverts.get(i).getAdvertFormat().getImage().getLink();
            AppApplication.AppComponentHolder.getAppComponent().imageLoader().loadImage(BaseApplication.getContext(), GlideImageConfig.builder()
                    .url(url)
                    .placeholder(R.drawable.shape_default_image)
                    .errorPic(R.drawable.shape_default_image)
                    .imagerView(imageView)
                    .build());
            imageView.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClik(v, position, link);
                }
            });
        }
    }

    public List<RealAdvertListBean> getAdvertListBeans() {
        return mAdvertListBeans;
    }

    public void hideAdvert() {
        mRootView.setVisibility(View.GONE);
    }

    public void showAdvert() {
        mRootView.setVisibility(View.VISIBLE);
    }

    public void setHeight(int height) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, height);
        params.setMargins(20, 20, 20, 20);
        mAdvertContainer.setLayoutParams(params);
    }

    public LinearLayout getAdvertContainer() {
        return mAdvertContainer;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setAdvertTagVisible(int visible) {
        mLLAdvertTag.setVisibility(visible);
    }

    public interface OnItemClickListener {
        void onItemClik(View v, int position, String url);
    }
}
