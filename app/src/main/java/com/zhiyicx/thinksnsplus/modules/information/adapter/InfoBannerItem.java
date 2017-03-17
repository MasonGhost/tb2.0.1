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
import com.zhiyicx.thinksnsplus.data.beans.InfoListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class InfoBannerItem implements ItemViewDelegate<BaseListBean> {


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_banner;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListBean.RecommendBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int
            position) {
        String url="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3498552962," +
                "2666166364&fm=21&gp=0.jpg";
        List<String> list=new ArrayList<>();
        list.add(url);
        list.add(url);
        list.add(url);
        list.add(url);
        InfoListBean.RecommendBean realData = (InfoListBean.RecommendBean) baseListBean;
        Banner banner = holder.getView(R.id.item_banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(list);
        banner.setDelayTime(5000);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setBannerAnimation(Transformer.CubeIn);
        banner.start();
    }

    private class GlideImageLoader extends ImageLoader {



        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            AppApplication.AppComponentHolder.getAppComponent()
                    .imageLoader()
                    .loadImage(context, GlideImageConfig.builder()
                            .imagerView(imageView)
                            .url((String)path)
                            .errorPic(R.mipmap.npc)
                            .build());
        }

    }
}