package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by jungle on 16/6/13.
 * com.zhiyicx.zhibo.ui.view
 * zhibo_android
 * email:335891510@qq.com
 */
public class RankHeadView extends AutoRelativeLayout {
    private ImageView ivHeadpic,ivVertify;
    private Context mContext;
    public RankHeadView(Context context) {
        super(context);
        init(context);
    }


    public RankHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RankHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext=context;
        View newslayout = LayoutInflater.from(context).inflate(R.layout.zb_recycle_item_rank_headpic, this, true);
        ivHeadpic = (ImageView) newslayout.findViewById(R.id.iv_rank_gold_item_icon);
        ivVertify = (ImageView) newslayout.findViewById(R.id.iv_rank_gold_item_verified);
    }
    public void setData(SearchResult data){

        UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), ivHeadpic, new GlideCircleTrasform(mContext));

        ivVertify.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);


    }
}
