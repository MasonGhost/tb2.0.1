package com.zhiyicx.thinksnsplus.modules.circle.list;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/05/19/11:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ChannelAdvertHeader implements MultiItemTypeAdapter.OnItemClickListener {

    private View mRootView;
    private LinearLayout mAdvertContainer;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;

    public ChannelAdvertHeader(final Context context, final List<SystemConfigBean.Advert> adverts) {
        this.mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.advert_channel, null);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_advert_channel);

        mRootView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        GridLayoutManager layoutManager = new GridLayoutManager(context, adverts.size() <= 3 ? adverts.size() : 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new TGridDecoration(20, 20, false));
        CommonAdapter adapter = new CommonAdapter<SystemConfigBean.Advert>(context, R.layout.item_advert, adverts) {
            @Override
            protected void convert(ViewHolder holder, SystemConfigBean.Advert advert, int position) {
                final String url = advert.getImageAdvert().getImage();
                final String link = advert.getImageAdvert().getLink();
                AppApplication.AppComponentHolder.getAppComponent().imageLoader().loadImage(BaseApplication.getContext(), GlideImageConfig.builder()
                        .url(url)
                        .placeholder(R.drawable.shape_default_image)
                        .errorPic(R.drawable.shape_default_image)
                        .imagerView(holder.getImageViwe(R.id.im_item_advert))
                        .build());
            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClik(view, holder, position);
        }

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mOnItemClickListener != null) {
            return mOnItemClickListener.onItemLongClick(view, holder, position);
        }
        return false;
    }

    public View getRootView() {
        return mRootView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClik(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }
}
