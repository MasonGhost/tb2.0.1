package com.zhiyicx.zhibolibrary.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleBoundTrasform;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class VideoListHolder extends ZBLBaseHolder<SearchResult> {
    TextView mName;
    TextView mUserCount;
    TextView mTitle;
    ImageView mCover;
    ImageView mVerified;
    ImageView mIcon;
    TextView mLocation;
    ImageView mLocationIV;
    ImageView mShapeIV;
    ImageView mBlackCoverIV;

    public VideoListHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.tv_live_item_user_name);
        mUserCount = (TextView) itemView.findViewById(R.id.tv_live_item_user_count);
        mTitle = (TextView) itemView.findViewById(R.id.tv_live_item_title);
        mCover = (ImageView) itemView.findViewById(R.id.iv_live_item_cover);
        mVerified = (ImageView) itemView.findViewById(R.id.iv_live_item_verified);
        mIcon = (ImageView) itemView.findViewById(R.id.iv_live_item_user_icon);
        mLocation = (TextView) itemView.findViewById(R.id.tv_live_item_location);
        mLocationIV = (ImageView) itemView.findViewById(R.id.iv_live_item_location);
        mShapeIV = (ImageView) itemView.findViewById(R.id.iv_live_item_shape);
        mBlackCoverIV = (ImageView) itemView.findViewById(R.id.iv_live_item_black_cover);
    }

    @Override
    public void setData(final SearchResult data) {
        mBlackCoverIV.setVisibility(View.GONE);
        mName.setText(data.user.uname);
        mShapeIV.setImageResource(R.mipmap.ico_people);
        mLocationIV.setVisibility(TextUtils.isEmpty(data.video.video_location) ? View.GONE : View.VISIBLE);
        mLocation.setText(data.video.video_location);
        mUserCount.setText(data.video.replay_count + "");
        mTitle.setText(data.video.video_title);

//        mVerified.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);
//
//        if(data.user.avatar.getOrigin()!=null) {
//            UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), mIcon, new GlideCircleBoundTrasform(UiUtils.getContext()));
//        } else {
//            mIcon.setImageResource(R.mipmap.pic_default_woman);
//        }
        if (data.video.video_icon.getOrigin() == null) {
            showBlackCover();//如果地址位空显示黑色遮罩
        }

        UiUtils.glideWrap(data.video.video_icon.getOrigin())
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
