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
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
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

public class ChannelListFragmentAdapter extends CommonAdapter<ChannelSubscripBean> {
    private static final String TAG = "ChannelListFragmentAdap";
    private ChannelListContract.Presenter mPresenter;

    public ChannelListFragmentAdapter(Context context, int layoutId, List<ChannelSubscripBean> datas, ChannelListContract.Presenter presenter) {
        super(context, layoutId, datas);
        mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, final ChannelSubscripBean channelSubscripBean, final int position) {
        ImageView iv_channel_cover = holder.getView(R.id.iv_channel_cover);
        TextView tv_channel_name = holder.getView(R.id.tv_channel_name);
        TextView tv_channel_feed_count = holder.getView(R.id.tv_channel_feed_count);
        TextView tv_channel_follow_count = holder.getView(R.id.tv_channel_follow_count);
        CheckBox tv_channel_subscrib = holder.getView(R.id.tv_channel_subscrib);

        ChannelInfoBean channelInfoBean = channelSubscripBean.getChannelInfoBean();
        // 设置封面
        ChannelInfoBean.ChannelCoverBean channelCoverBean = channelInfoBean.getCover();
        // 计算图片压缩比
        int imageViewWidth = getContext().getResources().getDimensionPixelSize(R.dimen.rec_image_for_list_normal);// 获取图片控件宽高
        int port = (int) (imageViewWidth * 100.0f / channelCoverBean.getImage_width());
        if (port > 100) {
            port = 100;
        }
        LogUtils.i(TAG + "channelCoverBean  " + channelCoverBean);
        String imgUrl = String.format(ApiConfig.IMAGE_PATH, channelCoverBean.getId(), port);
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .placeholder(R.drawable.shape_default_image)
                .errorPic(R.drawable.shape_default_image)
                .url(imgUrl)
                .imagerView(iv_channel_cover)
                .build()
        );
        // 设置频道名称
        tv_channel_name.setText(channelInfoBean.getTitle());
        // 设置分享人数
        String feedCountNumber = ConvertUtils.numberConvert(channelInfoBean.getFeed_count());
        String feedContent = getContext().getString(R.string.channel_share) + " " + "<" + feedCountNumber + ">";
        CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        tv_channel_feed_count.setText(feedString);
        // 设置订阅人数
        String followCountNumber = ConvertUtils.numberConvert(channelInfoBean.getFollow_count());
        String followContent = getContext().getString(R.string.channel_follow) + " " + "<" + followCountNumber + ">";
        CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        tv_channel_follow_count.setText(followString);

        // 设置订阅状态
        tv_channel_subscrib.setChecked(channelSubscripBean.getChannelSubscriped());
        RxView.clicks(tv_channel_subscrib)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mPresenter.handleChannelSubscrib(position, channelSubscripBean);
                    }
                });

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChannelDetailPage(getContext(), channelSubscripBean);
                    }
                });
    }

    private void toChannelDetailPage(Context context, ChannelSubscripBean channelSubscripBean) {
        Intent intent = new Intent(context, ChannelDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChannelDetailFragment.CHANNEL_HEADER_INFO_DATA, channelSubscripBean);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
