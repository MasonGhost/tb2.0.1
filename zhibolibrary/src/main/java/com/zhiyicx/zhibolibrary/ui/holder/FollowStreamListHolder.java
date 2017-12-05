package com.zhiyicx.zhibolibrary.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class FollowStreamListHolder extends ZBLBaseHolder<SearchResult> implements View.OnClickListener {

    View rl_container;
    ImageView mIconIV;
    ImageView mVerifiedIV;
    TextView mTitleTV;
    TextView mSubheadTV;
    TextView mFansTV;
    ImageView mFollowBT;
    private SearchResult mData;


    private long mTime;

    private boolean isFollow;
    private Context mContext;

    public FollowStreamListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext().getApplicationContext();
        rl_container = itemView.findViewById(R.id.rl_container);
        rl_container.setOnClickListener(this);
        mIconIV = (ImageView) itemView.findViewById(R.id.iv_search_item_icon);
        mVerifiedIV = (ImageView) itemView.findViewById(R.id.iv_follow_item_verified);
        mTitleTV = (TextView) itemView.findViewById(R.id.tv_search_item_title);
        mSubheadTV = (TextView) itemView.findViewById(R.id.tv_search_item_detail_title);
        mFansTV = (TextView) itemView.findViewById(R.id.tv_search_item_detail_fans);
        mFollowBT = (ImageView) itemView.findViewById(R.id.tv_follow_item_stream);
        mFollowBT.setOnClickListener(this);

    }


    @Override
    public void setData(SearchResult data) {
        this.mData = data;
        if (data.user == null) {
            return;
        }
        mTitleTV.setText(data.user.uname);
        Drawable drawable = mContext.getResources().getDrawable(data.user.sex == null || data.user.sex == 1 ? R.mipmap.ico_sex_man : R.mipmap
                .ico_sex_woman);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mTitleTV.setCompoundDrawables(null, null, drawable, null);


        UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), mIconIV, new GlideCircleTrasform(UiUtils.getContext()));
        mSubheadTV.setText(TextUtils.isEmpty(data.user.intro) ? UiUtils.getString("str_default_intro") : data.user.intro
        );
        mFansTV.setText(data.user.fans_count + "");
        mVerifiedIV.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);
        mFollowBT.setVisibility(data.stream == null ? View.INVISIBLE : View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_follow_item_stream) {
            if (mOnViewClickListener != null) {
                mOnViewClickListener.onViewClick(v, this.getPosition());
            }
        } else if (
                v.getId() == R.id.rl_container) {
            if (mData != null) {
                watchUser(mData);
            }
        }

    }

    /**
     * 查看用户主页
     *
     * @param data
     */
    private void watchUser(SearchResult data) {
        //
        Intent intent = new Intent(ZhiboApplication.INTENT_ACTION_UESRINFO);
        Bundle bundle = new Bundle();
        bundle.putString("user_id", data.user.uid);
        intent.putExtras(bundle);
        UiUtils.startActivity(intent);
    }

}
