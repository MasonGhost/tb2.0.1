package com.zhiyicx.thinksnsplus.modules.channel.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailActivity;
import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/10
 * @contact email:450127106@qq.com
 */

public class ChannelListFragmentAdapter extends CommonAdapter<GroupInfoBean> {
    private static final String TAG = "ChannelListFragmentAdap";
    private ChannelListContract.Presenter mPresenter;

    public ChannelListFragmentAdapter(Context context, int layoutId, List<GroupInfoBean> datas, ChannelListContract.Presenter presenter) {
        super(context, layoutId, datas);
        mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, final GroupInfoBean groupInfoBean, final int position) {
        ImageView iv_channel_cover = holder.getView(R.id.iv_channel_cover);
        TextView tv_channel_name = holder.getView(R.id.tv_channel_name);
        TextView tv_channel_feed_count = holder.getView(R.id.tv_channel_feed_count);
        TextView tv_channel_follow_count = holder.getView(R.id.tv_channel_follow_count);
        CheckBox tv_channel_subscrib = holder.getView(R.id.tv_channel_subscrib);

//        ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
        // 设置封面
        GroupInfoBean.GroupCoverBean groupCoverBean = groupInfoBean.getAvatar();
        String[] size = groupCoverBean.getSize().split("x");
        int width = 0;
        if (size.length > 0){
            width = Integer.parseInt(size[0]);
        }
        // 计算图片压缩比
        int imageViewWidth = getContext().getResources().getDimensionPixelSize(R.dimen.rec_image_for_list_normal);// 获取图片控件宽高
        if (width == 0){
            width = (int) (imageViewWidth * 100.0f);
        }
        int port = (int) (imageViewWidth * 100.0f / width);
        if (port > 100) {
            port = 100;
        }
        LogUtils.i(TAG + "channelCoverBean  " + groupCoverBean);
        String imgUrl = String.format(ApiConfig.IMAGE_PATH, groupCoverBean.getFile_id(), port);
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .placeholder(R.drawable.shape_default_image)
                .errorPic(R.drawable.shape_default_image)
                .url(imgUrl)
                .imagerView(iv_channel_cover)
                .build()
        );
        // 设置频道名称
        tv_channel_name.setText(groupInfoBean.getTitle());
        // 设置分享人数
        String feedCountNumber = ConvertUtils.numberConvert(groupInfoBean.getPosts_count());
        String feedContent = getContext().getString(R.string.channel_share) + " " + "<" + feedCountNumber + ">";
        CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        tv_channel_feed_count.setText(feedString);
        // 设置订阅人数
        String followCountNumber = ConvertUtils.numberConvert(groupInfoBean.getMembers_count());
        String followContent = getContext().getString(R.string.channel_follow) + " " + "<" + followCountNumber + ">";
        CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        tv_channel_follow_count.setText(followString);

        // 设置订阅状态
        boolean isSubscribe = groupInfoBean.getIs_audit() == 1;
        tv_channel_subscrib.setChecked(isSubscribe);
        tv_channel_subscrib.setText(isSubscribe ? getContext().getString(R.string.channel_followed) : getContext().getString(R.string.channel_follow));
        tv_channel_subscrib.setPadding(isSubscribe ? getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small) : getContext().getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
        RxView.clicks(tv_channel_subscrib)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (TouristConfig.CHEENAL_CAN_SUBSCRIB || !mPresenter.handleTouristControl()) {
//                        mPresenter.handleChannelSubscrib(position, groupInfoBean);
                    }
                });

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (TouristConfig.CHENNEL_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                        toChannelDetailPage(getContext(), groupInfoBean);
                    }
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
