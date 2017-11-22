package com.zhiyicx.zhibolibrary.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by jungle on 2016/5/25.
 */
public class GiftRankHeadpicListHolder extends ZBLBaseHolder<SearchResult> {

    ImageView ivRankGoldItemIcon;
    ImageView ivRankItemVerified;


    private SearchResult mData;

    public GiftRankHeadpicListHolder(View itemView) {
        super(itemView);
        ivRankGoldItemIcon = (ImageView) itemView.findViewById(R.id.iv_rank_gold_item_icon);
        ivRankItemVerified = (ImageView) itemView.findViewById(R.id.iv_rank_gold_item_verified);

    }


    @Override
    public void setData(SearchResult data) {
        this.mData = data;

        UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), ivRankGoldItemIcon, new GlideCircleTrasform(UiUtils.getContext()));

        ivRankItemVerified.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);

    }

}
