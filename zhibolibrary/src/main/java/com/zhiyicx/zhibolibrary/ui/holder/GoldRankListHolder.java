package com.zhiyicx.zhibolibrary.ui.holder;

import android.app.Activity;
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
import com.zhiyicx.zhibolibrary.di.component.DaggerGoldRankListHolderComponent;
import com.zhiyicx.zhibolibrary.di.module.UserHomeModule;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.presenter.UserHomePresenter;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.ui.components.FllowButtonView;
import com.zhiyicx.zhibolibrary.ui.view.UserHomeView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import javax.inject.Inject;

/**
 * Created by jungle on 2016/5/25.
 */
public class GoldRankListHolder extends ZBLBaseHolder<SearchResult> implements UserHomeView, View.OnClickListener {


    ImageView ivRankGoldItemIcon;
    ImageView mVerifiedIV;
    TextView tvRankGoldItemTitle;
    TextView tvRankGoldItemDetailTitle;
    TextView tvRankGoldItemGoldeSent;
    TextView tvRankGoldItemRank;
    FllowButtonView btRankGoldItemAttention;


    private SearchResult mData;
    @Inject
    UserHomePresenter mPresenter;

    private long mTime;

    private boolean isFollow;
    private Context mContext;

    public GoldRankListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();
        DaggerGoldRankListHolderComponent
                .builder()
                .userHomeModule(new UserHomeModule(this))
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .build()
                .inject(this);

        ivRankGoldItemIcon = (ImageView) itemView.findViewById(R.id.iv_rank_gold_item_icon);
        ivRankGoldItemIcon.setOnClickListener(this);
        mVerifiedIV = (ImageView) itemView.findViewById(R.id.iv_ranking_item_verified);
        tvRankGoldItemTitle = (TextView) itemView.findViewById(R.id.tv_rank_gold_item_title);
        tvRankGoldItemDetailTitle = (TextView) itemView.findViewById(R.id.tv_rank_gold_item_detail_title);
        tvRankGoldItemGoldeSent = (TextView) itemView.findViewById(R.id.tv_rank_gold_item_golde_sent);
        tvRankGoldItemRank = (TextView) itemView.findViewById(R.id.tv_rank_gold_item_rank);
        btRankGoldItemAttention = (FllowButtonView) itemView.findViewById(R.id.bt_rank_gold_item_attention);
        itemView.findViewById(R.id.fl_container).setOnClickListener(this);
        btRankGoldItemAttention.setOnClickListener(this);


    }


    @Override
    public void setData(SearchResult data) {
        this.mData = data;
        tvRankGoldItemTitle.setText(data.user.uname);
        Drawable drawable = mContext.getResources().getDrawable(data.user.sex == null || data.user.sex == 1 ? R.mipmap.ico_sex_man : R.mipmap.ico_sex_woman);
/// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvRankGoldItemTitle.setCompoundDrawables(null, null, drawable, null);


        UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), ivRankGoldItemIcon, new GlideCircleTrasform(UiUtils.getContext()));

        tvRankGoldItemDetailTitle.setText(TextUtils.isEmpty(data.user.intro) ? UiUtils.getString("str_default_intro") : data.user.intro
        );
        tvRankGoldItemGoldeSent.setText("x " + data.user.gold);
        tvRankGoldItemRank.setText(this.getPosition() + 1 + "");
        mVerifiedIV.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);
        if (this.getPosition() < 3) {
            tvRankGoldItemRank.setTextColor(UiUtils.getColor("color_font_red"));
        }
        else {
            tvRankGoldItemRank.setTextColor(UiUtils.getColor("color_font_black_light"));
        }
        if (data.user.uid.equals(ZhiboApplication.getUserInfo().uid))
            btRankGoldItemAttention.setVisibility(View.INVISIBLE);
        else {
            btRankGoldItemAttention.setVisibility(View.VISIBLE);
            isFollow = data.user.is_follow == 1 ? true : false;//是否关注
            setFollow(isFollow);
            btRankGoldItemAttention.setNameSize(13);
        }

    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        if (isFollow) {
            btRankGoldItemAttention.setName(UiUtils.getString("str_already_follow"));
            btRankGoldItemAttention.setNameColor(UiUtils.getColor(R.color.white));
            btRankGoldItemAttention.setBackgroundResource(R.drawable.shape_stroke_corner_disable);
            btRankGoldItemAttention.setNameLeftDrawable(R.mipmap.ico_added_gz);
        }
        else {
            btRankGoldItemAttention.setName(UiUtils.getString("str_follow"));
            btRankGoldItemAttention.setNameColor(UiUtils.getColor(R.color.color_blue_button));
            btRankGoldItemAttention.setBackgroundResource(R.drawable.shape_stroke_corner);
            btRankGoldItemAttention.setNameLeftDrawable(null);
        }
    }

    @Override
    public void setFollowEnable(boolean isEnable) {
        btRankGoldItemAttention.setEnabled(isEnable);
    }

    @Override
    public void setFollow(boolean isFollow) {
        this.isFollow = isFollow;
        setFollowStatus(isFollow);
    }

    @Override
    public boolean getFollow() {
        return isFollow;
    }


    @Override
    public void onClick(View v) {
        /**
         * 关注
         */
        if (v.getId() == R.id.bt_rank_gold_item_attention) {
            long currentTime = System.currentTimeMillis();//频繁请求提示
            if ((currentTime - mTime) < UserService.FOLLOW_SPACING_TIME) {
                showMessage(UiUtils.getString("str_frequently_follow_prompt"));
                return;
            }
            else {
                mTime = System.currentTimeMillis();
            }
            mPresenter.follow(UserHomePresenter.isFollow(!isFollow));
        }
        else if (v.getId() == R.id.fl_container) {
            /**
             * 点击头像
             */

            // TODO: 16/10/10
            //跳转到个人主页
            Intent intent = new Intent(ZhiboApplication.INTENT_ACTION_UESRINFO);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", mData.user.uid);
            intent.putExtras(bundle);
            UiUtils.startActivity(intent);
        }


    }


    @Override
    public SearchResult getUserInfo() {
        return mData;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }
}
