package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.model.GlideUrl;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detail.ChannelDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.list.ChannelListContract;
import com.zhiyicx.thinksnsplus.modules.circle.mine.MyGroupContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListItem implements ItemViewDelegate<GroupInfoBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_channel_list;
    }

    @Override
    public boolean isForViewType(GroupInfoBean item, int position) {
        return item.getId() > 0;
    }

    @Override
    public void convert(ViewHolder holder, GroupInfoBean groupInfoBean, GroupInfoBean lastT, int position, int itemCounts) {

        // 封面
        ImageView circleCover = holder.getView(R.id.iv_channel_cover);

        // 名字
        TextView circleName = holder.getView(R.id.tv_channel_name);

        // 帖子数量
        TextView circleFeedCount = holder.getView(R.id.tv_channel_feed_count);

        // 成员数量
        TextView circleMemberCount = holder.getView(R.id.tv_channel_follow_count);

        CheckBox circleSubscribe = holder.getView(R.id.tv_channel_subscrib);

        Context context = circleSubscribe.getContext();

        // 设置封面
        GroupInfoBean.GroupCoverBean groupCoverBean = groupInfoBean.getAvatar();
        if (groupCoverBean == null) {
            groupCoverBean = new GroupInfoBean.GroupCoverBean();
        }
        String[] size = groupCoverBean.getSize().split("x");
        int width = 0;
        int height = 0;
        if (size.length > 0) {
            try {
                width = Integer.parseInt(size[0]);
                height = Integer.parseInt(size[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        // 计算图片压缩比
        int imageViewWidth = context.getResources().getDimensionPixelSize(R.dimen.rec_image_for_list_normal);// 获取图片控件宽高
        if (width == 0) {
            width = (int) (imageViewWidth * 100.0f);
            height = (int) (imageViewWidth * 100.0f);
        }
        int port = (int) (imageViewWidth * 100.0f / width);
        if (port > 100) {
            port = 100;
        }
        GlideUrl glideUrl = ImageUtils.imagePathConvertV2((int) groupCoverBean.getFile_id(), width, height
                , port, AppApplication.getTOKEN());
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(context, GlideImageConfig.builder()
                .placeholder(R.drawable.shape_default_image)
                .errorPic(R.drawable.shape_default_image)
                .url(glideUrl.toStringUrl())
                .imagerView(circleCover)
                .build()
        );
        // 设置频道名称
        circleName.setText(groupInfoBean.getTitle());
        // 设置分享人数
        String feedCountNumber = ConvertUtils.numberConvert(groupInfoBean.getPosts_count());
        String feedContent = context.getString(R.string.channel_share) + " " + "<" + feedCountNumber + ">";
        CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                .format();
        circleFeedCount.setText(feedString);
        // 设置订阅人数
        String followCountNumber = ConvertUtils.numberConvert(groupInfoBean.getMembers_count());
        String followContent = context.getString(R.string.channel_follow) + " " + "<" + followCountNumber + ">";
        CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                .format();
        circleMemberCount.setText(followString);

        // 设置订阅状态
        boolean isJoined = groupInfoBean.getIs_member() == 1;
        circleSubscribe.setChecked(isJoined);
        circleSubscribe.setText(isJoined ? context.getString(R.string.group_joined) : context.getString(R.string.join_group));
        circleSubscribe.setPadding(isJoined ? context.getResources().getDimensionPixelSize(R.dimen.spacing_small) : context.getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
        RxView.clicks(circleSubscribe)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {

                });

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {

                });
        RxView.clicks(holder.getView(R.id.iv_channel_cover))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {

                });
    }

    private void toChannelDetailPage(Context context, GroupInfoBean groupInfoBean) {
        Intent intent = new Intent(context, ChannelDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChannelDetailFragment.CHANNEL_HEADER_INFO_DATA, groupInfoBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
