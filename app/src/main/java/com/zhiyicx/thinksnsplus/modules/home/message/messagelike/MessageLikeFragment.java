package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
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
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageLikeFragment extends TSListFragment<MessageLikeContract.Presenter, DigedBean> implements MessageLikeContract.View {

    private ImageLoader mImageLoader;

    public MessageLikeFragment() {
    }

    public static MessageLikeFragment newInstance() {
        MessageLikeFragment fragment = new MessageLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.like);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
    }

    @Override
    protected CommonAdapter<DigedBean> getAdapter() {
        return new CommonAdapter<DigedBean>(getActivity(), R.layout.item_message_like_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, DigedBean DigedBean, int position) {
                setItemData(holder, DigedBean, position);
            }

        };
    }

    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param digedBean 当前数据
     * @param position        当前数据位置
     */

    private void setItemData(ViewHolder holder, final DigedBean digedBean, int position) {
        if (position == mListDatas.size() - 1) {
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
                    .url(ImageUtils.imagePathConvert(ApiConfig.IMAGE_PATH,digedBean.getSource_cover()))
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
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(digedBean);
                    }
                });

        RxView.clicks(holder.getView(R.id.tv_deatil))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(digedBean);
                    }
                });
    }

    /**
     * 进入聊天页
     *
     * @param DigedBean
     */
    private void toChat(DigedBean DigedBean) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, DigedBean);
        to.putExtras(bundle);
        startActivity(to);
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(to);
    }

}
