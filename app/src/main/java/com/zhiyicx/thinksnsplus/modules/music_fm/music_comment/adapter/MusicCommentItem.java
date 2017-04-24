package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoLongClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_ERROR;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class MusicCommentItem implements ItemViewDelegate<MusicCommentListBean> {
    private OnUserInfoClickListener mOnUserInfoClickListener;
    private OnUserInfoLongClickListener mOnUserInfoLongClickListener;
    protected OnReSendClickListener mOnReSendClickListener;

    public void setOnUserInfoClickListener(OnUserInfoClickListener onUserInfoClickListener) {
        mOnUserInfoClickListener = onUserInfoClickListener;
    }

    public void setOnUserInfoLongClickListener(OnUserInfoLongClickListener onUserInfoLongClickListener) {
        mOnUserInfoLongClickListener = onUserInfoLongClickListener;
    }

    public void setOnReSendClickListener(OnReSendClickListener onReSendClickListener) {
        mOnReSendClickListener = onReSendClickListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(MusicCommentListBean item, int position) {
        return !TextUtils.isEmpty(item.getComment_content());
    }

    @Override
    public void convert(ViewHolder holder,final MusicCommentListBean musicCommentListBean,
                        MusicCommentListBean lastT, final int position) {
        if (musicCommentListBean.getFromUserInfoBean() != null) {
            AppApplication.AppComponentHolder.getAppComponent()
                    .imageLoader()
                    .loadImage(holder.getConvertView().getContext(), GlideImageConfig.builder()
                            .url(ImageUtils.imagePathConvert(musicCommentListBean
                                    .getFromUserInfoBean()
                                    .getAvatar(), ImageZipConfig.IMAGE_26_ZIP))
                            .placeholder(R.drawable.shape_default_image_circle)
                            .transformation(new GlideCircleTransform(holder.getConvertView()
                                    .getContext()))
                            .errorPic(R.drawable.shape_default_image_circle)
                            .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                            .build()
                    );
            holder.setText(R.id.tv_name, musicCommentListBean.getFromUserInfoBean().getName());
            holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(musicCommentListBean
                    .getCreated_at()));
            holder.setText(R.id.tv_content, setShowText(musicCommentListBean, position));
            if (musicCommentListBean.getState() == SEND_ERROR) {
                holder.getView(R.id.fl_tip).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.fl_tip).setVisibility(View.GONE);
            }
            RxView.clicks(holder.getView(R.id.fl_tip))
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)  // 两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            if (mOnReSendClickListener != null) {
                                mOnReSendClickListener.onReSendClick(musicCommentListBean);
                            }
                        }
                    });
            List<Link> links = setLiknks(holder, musicCommentListBean, position);
            if (!links.isEmpty()) {
                LinkBuilder.on((TextView) holder.getView(R.id.tv_content))
                        .addLinks(links)
                        .build();
            }

            setUserInfoClick(holder.getView(R.id.tv_name), musicCommentListBean.getFromUserInfoBean());
            setUserInfoClick(holder.getView(R.id.iv_headpic), musicCommentListBean.getFromUserInfoBean());
        }

    }

    private void setUserInfoClick(View v, final UserInfoBean userInfoBean) {
        RxView.clicks(v).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (mOnUserInfoClickListener != null) {
                    mOnUserInfoClickListener.onUserInfoClick(userInfoBean);
                }
            }
        });
    }

    protected String setShowText(MusicCommentListBean musicCommentListBean, int position) {
        return handleName(musicCommentListBean);
    }

    protected List<Link> setLiknks(ViewHolder holder, final MusicCommentListBean musicCommentListBean, int position) {
        List<Link> links = new ArrayList<>();
        if (musicCommentListBean.getToUserInfoBean() != null && musicCommentListBean.getToUserInfoBean().getName() != null) {
            Link replyNameLink = new Link(musicCommentListBean.getToUserInfoBean().getName())
                    .setTextColor(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.important_for_content))                  // optional, defaults to holo blue
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(holder.getConvertView().getContext(), R.color.general_for_hint)) // optional, defaults to holo blue
                    .setHighlightAlpha(.5f)                                     // optional, defaults to .15f
                    .setUnderlined(false)                                       // optional, defaults to true
                    .setOnLongClickListener(new Link.OnLongClickListener() {
                        @Override
                        public void onLongClick(String clickedText) {
                            if (mOnUserInfoLongClickListener != null) {
                                mOnUserInfoLongClickListener.onUserInfoLongClick(musicCommentListBean.getToUserInfoBean());
                            }
                        }
                    })
                    .setOnClickListener(new Link.OnClickListener() {
                        @Override
                        public void onClick(String clickedText) {
                            // single clicked
                            if (mOnUserInfoClickListener != null) {
                                mOnUserInfoClickListener.onUserInfoClick(musicCommentListBean.getToUserInfoBean());
                            }
                        }
                    });
            links.add(replyNameLink);
        }


        return links;
    }

    /**
     * 处理名字的颜色与点击
     *
     * @param musicCommentListBean
     * @return
     */
    private String handleName(MusicCommentListBean musicCommentListBean) {
        String content = "";
        if (musicCommentListBean.getReply_to_user_id() != 0) { // 当没有回复者时，就是回复评论
            content += " 回复 " + musicCommentListBean.getToUserInfoBean().getName() + ": " +
                    musicCommentListBean.getComment_content();
        } else {
            content = musicCommentListBean.getComment_content();
        }
        return content;
    }

    /**
     * resend interface
     */
    public interface OnReSendClickListener {
        void onReSendClick(MusicCommentListBean musicCommentListBean);
    }
}
