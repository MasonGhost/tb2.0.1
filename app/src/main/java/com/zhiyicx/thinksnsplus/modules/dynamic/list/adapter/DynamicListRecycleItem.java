package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * @Describe 我发送的文本消息
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListRecycleItem extends DynamicListBaseItem {


    public DynamicListRecycleItem(Context context) {
        super(context);

    }

    private MultiItemTypeAdapter getadaper(final Context context, List<String> mImagUrls) {
        return new CommonAdapter<String>(context, R.layout.item_dynamic_list_image, mImagUrls) {
            @Override
            protected void convert(ViewHolder holder, String String, int position) {
                mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                        .url(String)
                        .transformation(new GlideCircleTransform(mContext))
                        .errorPic(R.drawable.shape_default_image_circle)
                        .imagerView((ImageView) holder.getView(R.id.iv_image))
                        .build());
            }
        };
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list;
    }

    @Override
    public boolean isForViewType(DynamicBean item, int position) {
        return true;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        RecyclerView recyclerView = holder.getView(R.id.nrv_image);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.getConvertView().getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridDecoration(10, 10));//设置Item的间隔
        List<String> testdata = new ArrayList<>();
        testdata.add("http://wx4.sinaimg.cn/thumbnail/6c2fc79ely1fcss6ufxbaj20do0i8n4a.jpg");
        testdata.add("http://img.blog.csdn.net/20160330224550866");
        testdata.add("http://tva2.sinaimg.cn/crop.0.0.1002.1002.50/d710166ajw8fbw38t1do7j20ru0ru0v4.jpg");
        recyclerView.setAdapter(getadaper(context, testdata));
    }


}

