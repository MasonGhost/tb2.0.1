package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailCommentItem implements ItemViewDelegate<InfoCommentListBean> {

    private OnCommentItemListener mOnCommentItemListener;

    public InfoDetailCommentItem(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return !TextUtils.isEmpty(item.getComment_content());
    }

    @Override
    public void convert(final ViewHolder holder, InfoCommentListBean infoCommentListBean,
                        InfoCommentListBean lastT, final int position) {
        AppApplication.AppComponentHolder.getAppComponent()
                .imageLoader()
                .loadImage(holder.getConvertView().getContext(), GlideImageConfig.builder()
                        .url(ImageUtils.imagePathConvert(infoCommentListBean
                                .getFromUserInfoBean()
                                .getAvatar(), ImageZipConfig.IMAGE_26_ZIP))
                        .placeholder(R.drawable.shape_default_image_circle)
                        .transformation(new GlideCircleTransform(holder.getConvertView()
                                .getContext()))
                        .errorPic(R.drawable.shape_default_image_circle)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                        .build()
                );
        holder.setText(R.id.tv_name, infoCommentListBean.getFromUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(infoCommentListBean
                .getCreated_at()));
        holder.setText(R.id.tv_content, setShowText(infoCommentListBean, position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnCommentItemListener.onItemClick(v,holder,position);
            }
        });
    }

    protected String setShowText(InfoCommentListBean infoCommentListBean, int position) {
        return handleName(infoCommentListBean);
    }

    private String handleName(InfoCommentListBean infoCommentListBean) {
        String content = "";
        if (infoCommentListBean.getReply_to_user_id() != 0) { // 当没有回复者时，就是回复评论
            content += " 回复 " + infoCommentListBean.getToUserInfoBean().getName() + " " +
                    infoCommentListBean.getComment_content();
        } else {
            content = infoCommentListBean.getComment_content();
        }
        return content;
    }

    public void setOnCommentItemListener(OnCommentItemListener onCommentItemListener) {
        mOnCommentItemListener = onCommentItemListener;
    }

    public interface OnCommentItemListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }
}
