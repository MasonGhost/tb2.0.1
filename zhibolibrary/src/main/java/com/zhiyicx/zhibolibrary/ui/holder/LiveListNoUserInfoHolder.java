package com.zhiyicx.zhibolibrary.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class LiveListNoUserInfoHolder extends ZBLBaseHolder<SearchResult> {
    TextView mUserCount;
    TextView mTitle;
    TextView mLocation;
    ImageView mCover;
    ImageView mLocationIV;
    ImageView mBlackCoverIV;

    private SearchResult mData;

    public LiveListNoUserInfoHolder(View itemView) {
        super(itemView);
        mUserCount = (TextView) itemView.findViewById(R.id.tv_live_item_user_count);
        mTitle = (TextView) itemView.findViewById(R.id.tv_live_item_title);
        mLocation = (TextView) itemView.findViewById(R.id.tv_live_item_location);
        mCover = (ImageView) itemView.findViewById(R.id.iv_live_item_cover);
        mLocationIV = (ImageView) itemView.findViewById(R.id.iv_live_item_location);
        mBlackCoverIV = (ImageView) itemView.findViewById(R.id.iv_live_item_black_cover);
    }

    @Override
    public void setData(SearchResult data) {
        this.mData = data;
        mBlackCoverIV.setVisibility(View.GONE);//默认不显示黑色遮罩
        if (data.stream == null) {

            mTitle.setText("主播离线");
            mBlackCoverIV.setVisibility(View.VISIBLE);
            mCover.setImageResource(R.mipmap.pic_photo_340);
            return;

        }
        mTitle.setText(data.stream.title);
        mUserCount.setText(data.stream.online_count + "");
        mLocationIV.setVisibility(TextUtils.isEmpty(data.stream.location) ? View.GONE : View.VISIBLE);
        mLocation.setText(data.stream.location);
        if (data.stream.icon.getOrigin() == null) showBlackCover();//如果地址位空显示黑色遮罩
        mUserCount.setText(data.stream.online_count + "");
        UiUtils.glideWrap(data.stream.icon.getOrigin())
                .placeholder(R.mipmap.pic_photo_340)
                .listener(new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        LogUtils.warnInfo(TAG, "display.......");
                        showBlackCover();//如果图片加载成功则显示黑色遮罩
                        return false;
                    }
                })
                .into(mCover);

    }

    private void showBlackCover() {
        mBlackCoverIV.setVisibility(View.VISIBLE);
    }


}
