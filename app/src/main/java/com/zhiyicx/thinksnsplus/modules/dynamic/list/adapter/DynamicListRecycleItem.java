package com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.DynamicGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 动态列表 1、2、3、4、9张图片使用的 item 适配器
 * document : {@see https://github.com/zhiyicx/thinksns-plus-document/blob/master/document/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8%8C%83%202.0/TS%2B%E8%A7%86%E8%A7%89%E8%A7%84%E8%8C%83%202.0.pdf}
 * @Author Jungle68
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListRecycleItem extends DynamicListBaseItem {
    private static final int IMAGE_COUNTS_1 = 1;// 动态列表图片数量 ,一张图片
    private static final int IMAGE_COUNTS_2 = 2;// 动态列表图片数量 ,两张图片
    private static final int IMAGE_COUNTS_3 = 3;// 动态列表图片数量 ,三张图片
    private static final int IMAGE_COUNTS_4 = 4;// 动态列表图片数量 ,四张图片
    private static final int IMAGE_COUNTS_9 = 9;// 动态列表图片数量 ,九张图片
    private static final int IMAGE_ITEM_SPACING = 5;// 图片间距，单位 dp
    private int mImageItemSpacing;

    public DynamicListRecycleItem(Context context) {
        super(context);
        mImageItemSpacing = ConvertUtils.dp2px(context, IMAGE_ITEM_SPACING);

    }

    private CommonAdapter getadaper(final Context context, List<String> mImagUrls) {
        return new CommonAdapter<String>(context, R.layout.item_dynamic_list_image, mImagUrls) {
            @Override
            protected void convert(ViewHolder holder, String String, int position) {
                mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                        .url(String)
                        .imagerView((ImageView) holder.getView(R.id.iv_item_image))
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
//        return item.getFeed().getStorage().size() == IMAGE_COUNTS_1
//                || item.getFeed().getStorage().size() == IMAGE_COUNTS_2
//                || item.getFeed().getStorage().size() == IMAGE_COUNTS_3
//                || item.getFeed().getStorage().size() == IMAGE_COUNTS_4
//                || item.getFeed().getStorage().size() == IMAGE_COUNTS_9;
//        return item.getFeed().getStorage().size() != 5;
        return false;
    }

    @Override
    public void convert(ViewHolder holder, DynamicBean dynamicBean, DynamicBean lastT, int position) {
        super.convert(holder, dynamicBean, lastT, position);
        RecyclerView recyclerView = holder.getView(R.id.nrv_image);
        int colums;
        switch (dynamicBean.getFeed().getStorage().size()) { // 根据设计规范，计算出当前数量图片应该分成几列

            case IMAGE_COUNTS_1:
                colums = 1;
                break;

            case IMAGE_COUNTS_2:
                colums = 2;
                break;

            case IMAGE_COUNTS_3:
                colums = 3;
                break;
            case IMAGE_COUNTS_4:
                colums = 2;
                break;
            case IMAGE_COUNTS_9:
                colums = 3;
                break;
            default:
                colums = 3;
        }
        System.out.println("dynamicBean.getFeed().getStorage().size() = " + dynamicBean.getFeed().getStorage().size());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.getConvertView().getContext(), colums);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DynamicGridDecoration(mContext))
        ;//设置Item的间隔
        List<String> testdata = new ArrayList<>();
        for (int i = 0; i < dynamicBean.getFeed().getStorage().size(); i++) {
            testdata.add("http://wx4.sinaimg.cn/thumbnail/6c2fc79ely1fcss6ufxbaj20do0i8n4a.jpg");
        }
        recyclerView.setAdapter(getadaper(recyclerView.getContext(), testdata));
    }


}

