package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/13
 * @Contact master.jungle68@gmail.com
 */

public class MessageLikeAdapter extends CommonAdapter<DigedBean> {
    private ImageLoader mImageLoader;

    public MessageLikeAdapter(Context context, int layoutId, List<DigedBean> datas) {
        super(context, layoutId, datas);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();

    }

    @Override
    protected void convert(final ViewHolder holder, final DigedBean digedBean, final int position) {

        if (position == getItemCount() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(ImageUtils.imagePathConvert(digedBean.getDigUserInfo().getAvatar(), ImageZipConfig.IMAGE_38_ZIP))
                .transformation(new GlideCircleTransform(getContext()))
                .errorPic(R.mipmap.pic_default_portrait1)
                .placeholder(R.mipmap.pic_default_portrait1)
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        if (digedBean.getSource_cover() != 0) {
            holder.setVisible(R.id.tv_deatil, View.GONE);
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(ImageUtils.imagePathConvert(ApiConfig.IMAGE_PATH, digedBean.getSource_cover()))
                    .imagerView((ImageView) holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
            holder.setVisible(R.id.tv_deatil, View.VISIBLE);
            holder.setText(R.id.tv_deatil, digedBean.getSource_content());
        }

        holder.setText(R.id.tv_name, digedBean.getDigUserInfo().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(digedBean.getUpdated_at()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(digedBean.getDigUserInfo());
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(digedBean.getDigUserInfo());
                    }
                });
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toDetail(digedBean);
                    }
                });
    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(mContext, userInfoBean);
    }

    /**
     * 根据不同的type 进入不同的 详情
     *
     * @param digedBean
     */
    private void toDetail(DigedBean digedBean) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, digedBean.getSource_id());
        switch (digedBean.getComponent()) {

            case ApiConfig.APP_COMPONENT_FEED:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case ApiConfig.APP_COMPONENT_MUSIC:
                intent = new Intent(mContext, MusicDetailActivity.class);
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case ApiConfig.APP_COMPONENT_NEWS:
                intent = new Intent(mContext, InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;
            default:
                intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;

        }
        mContext.startActivity(intent);
    }

}
