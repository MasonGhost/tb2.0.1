package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

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
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageCommentFragment extends TSListFragment<MessageCommentContract.Presenter, CommentedBean> implements MessageCommentContract.View {


    private ImageLoader mImageLoader;

    public MessageCommentFragment() {
    }

    public static MessageCommentFragment newInstance() {
        MessageCommentFragment fragment = new MessageCommentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected String setCenterTitle() {
        return getString(R.string.comment);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected CommonAdapter<CommentedBean> getAdapter() {
        return new CommonAdapter<CommentedBean>(getActivity(), R.layout.item_message_comment_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CommentedBean CommentedBean, int position) {
                setItemData(holder, CommentedBean, position);
            }
        };
    }


    @Override
    protected void initData() {
        super.initData();
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
    }

    /**
     * 设置item 数据
     *
     * @param holder      控件管理器
     * @param commentedBean 当前数据
     * @param position    当前数据位置
     */

    private void setItemData(ViewHolder holder, final CommentedBean commentedBean, int position) {

        if (position == mListDatas.size() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(ImageUtils.imagePathConvert(commentedBean.getCommentUserInfo().getAvatar(), ImageZipConfig.IMAGE_38_ZIP))
                .transformation(new GlideCircleTransform(getContext()))
                .errorPic(R.mipmap.pic_default_portrait1)
                .placeholder(R.mipmap.pic_default_portrait1)
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        if (commentedBean.getSource_cover() != 0) {
            holder.setVisible(R.id.tv_deatil, View.GONE);
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(ImageUtils.imagePathConvert(ApiConfig.IMAGE_PATH,commentedBean.getSource_cover()))
                    .imagerView((ImageView) holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
            holder.setVisible(R.id.tv_deatil, View.VISIBLE);
            holder.setText(R.id.tv_deatil, commentedBean.getSource_content());
        }

        holder.setText(R.id.tv_name, commentedBean.getCommentUserInfo().getName());
        holder.setText(R.id.tv_content, commentedBean.getComment_content());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(commentedBean.getUpdated_at()));

        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter( commentedBean.getCommentUserInfo());
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter( commentedBean.getCommentUserInfo());
                    }
                });
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        doComment(commentedBean);
                    }
                });
        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toDetail(commentedBean);
                    }
                });

    }

    /**
     * 根据不同的type 进入不同的 详情
     * @param commentedBean
     */
    private void toDetail(CommentedBean commentedBean) {
    }

    /**
     * 进入聊天页
     *
     * @param CommentedBean
     */
    private void doComment(CommentedBean CommentedBean) {

    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getActivity(),userInfoBean);
    }

}
