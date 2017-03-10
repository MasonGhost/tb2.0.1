package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoBannerBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class InfoBannerItem implements ItemViewDelegate<BaseListBean> {


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_banner;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoBannerBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int
            position) {

        InfoBannerBean realData = (InfoBannerBean) baseListBean;
        Banner banner = holder.getView(R.id.item_banner);
        banner.setImageLoader(new GlideImageLoader(realData.getIamges().get(position)));
        banner.setImages(realData.getIamges());
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setBannerAnimation(Transformer.CubeIn);
        banner.start();
    }

    private class GlideImageLoader extends ImageLoader {

        String url;

        GlideImageLoader(String url) {
            this.url = url;
        }

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            AppApplication.AppComponentHolder.getAppComponent()
                    .imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .imagerView(imageView)
                            .url(url)
                            .errorPic(R.mipmap.npc)
                            .build());
        }

    }
}