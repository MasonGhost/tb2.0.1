package com.zhiyicx.zhibolibrary.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class RankListHolder extends ZBLBaseHolder<SearchResult> {
    ImageView mIconIV;
    //    ImageView mVerifiedIV;
    TextView mTitleTV;
    //    TextView mSubheadTV;
    TextView mStarTV;
    TextView mGoldTV;
    TextView mRankTV;

    private Context mContext;
    private SearchResult mData;


    public RankListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext().getApplicationContext();
        mIconIV = (ImageView) itemView.findViewById(R.id.iv_ranking_item_icon);
//        mVerifiedIV = (ImageView) itemView.findViewById(R.id.iv_ranking_item_verified);
        mTitleTV = (TextView) itemView.findViewById(R.id.tv_ranking_item_title);
//        mSubheadTV = (TextView) itemView.findViewById(R.id.tv_ranking_item_detail_title);
        mStarTV = (TextView) itemView.findViewById(R.id.tv_ranking_item_star);
        mGoldTV = (TextView) itemView.findViewById(R.id.tv_ranking_item_gold);
        mRankTV = (TextView) itemView.findViewById(R.id.tv_ranking_item_rank);


    }


    @Override
    public void setData(SearchResult data) {
        this.mData = data;
        if (data.user == null) return;

        mTitleTV.setText(data.user.uname);
//        Drawable drawable = mContext.getResources().getDrawable(data.user.sex == null || data.user.sex == 1 ? R.mipmap.ico_sex_man : R.mipmap.ico_sex_woman);
///// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        mTitleTV.setCompoundDrawables(null, null, drawable, null);


        if (data.user.avatar != null)
            UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), mIconIV, new GlideCircleTrasform(UiUtils.getContext()));

//        mSubheadTV.setText(TextUtils.isEmpty(data.user.intro) ? UiUtils.getString("str_default_intro") : data.user.intro
//        );

        mStarTV.setText("获赞：" + data.user.fans_count);
        mGoldTV.setText("积分：" + data.user.getGold());
        mRankTV.setText(this.getPosition() + 1 + "");
//        mVerifiedIV.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);
        if (this.getPosition() < 3) {
            mRankTV.setTextColor(UiUtils.getColor("title_blue"));
        }
        else {
            mRankTV.setTextColor(UiUtils.getColor("bbblack"));
        }
    }


}
