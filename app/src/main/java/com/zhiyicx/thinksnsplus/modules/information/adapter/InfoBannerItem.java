package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class InfoBannerItem implements ItemViewDelegate<BaseListBean> {


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_banner;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return (item instanceof InfoRecommendBean) && position == 0;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                        int position,int itemCounts) {
        InfoRecommendBean realData = (InfoRecommendBean) baseListBean;
        String url = String.format(ApiConfig.IMAGE_PATH, realData.getCover().getId(), 50);
        Banner banner = holder.getView(R.id.item_banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.addImages(url);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setBannerAnimation(Transformer.CubeIn);
        banner.start();
    }

    private class GlideImageLoader extends com.youth.banner.loader.ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            AppApplication.AppComponentHolder.getAppComponent()
                    .imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .imagerView(imageView)
                            .url((String) path)
                            .errorPic(R.mipmap.npc)
                            .build());
        }

    }

}