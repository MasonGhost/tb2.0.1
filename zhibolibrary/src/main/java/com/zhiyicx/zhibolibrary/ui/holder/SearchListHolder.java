package com.zhiyicx.zhibolibrary.ui.holder;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerSearchListComponent;
import com.zhiyicx.zhibolibrary.di.module.UserHomeModule;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.presenter.SearchTabPresenter;
import com.zhiyicx.zhibolibrary.presenter.UserHomePresenter;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.ui.components.FllowButtonView;
import com.zhiyicx.zhibolibrary.ui.view.UserHomeView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class SearchListHolder extends ZBLBaseHolder<SearchResult> implements UserHomeView {
    public static final String DISPATCH_EVENT_VIDEO = "dispatch_video";

    ImageView mIconIV;
    TextView mTitleTV;
    TextView mSubheadTV;
    TextView mFansTV;
    ImageView mVerifiedIV;
    FllowButtonView mButton;
    ImageView mGenderIV;
    ImageView mCountIV;

    private int type;

    private SearchResult mData;
    @Inject
    UserHomePresenter mPresenter;

    private long mTime;

    private boolean isFollow;

    public SearchListHolder(View itemView) {
        super(itemView);
        DaggerSearchListComponent
                .builder()
                .userHomeModule(new UserHomeModule(this))
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .build()
                .inject(this);
        mIconIV = (ImageView) itemView.findViewById(R.id.iv_search_item_icon);
        mTitleTV = (TextView) itemView.findViewById(R.id.tv_search_item_title);
        mSubheadTV = (TextView) itemView.findViewById(R.id.tv_search_item_detail_title);
        mFansTV = (TextView) itemView.findViewById(R.id.tv_search_item_detail_fans);
        mVerifiedIV = (ImageView) itemView.findViewById(R.id.iv_search_item_verified);
        mButton = (FllowButtonView) itemView.findViewById(R.id.bt_item_attention);
        mGenderIV = (ImageView) itemView.findViewById(R.id.iv_search_item_gender);
        mCountIV = (ImageView) itemView.findViewById(R.id.iv_search_item_count);

    }

    @Override
    public void setData(final SearchResult data) {
        mData = data;
        if (data.stream != null) {
            type = SearchTabPresenter.SEARCH_STREAM;
        }
        else if (data.video != null) {
            type = SearchTabPresenter.SEARCH_VIDEO;
        }
        else {
            type = SearchTabPresenter.SEARCH_USER;
        }
        setUserInfo(data);

        switch (type) {
            case SearchTabPresenter.SEARCH_USER:
                if (data.user.uid.equals(ZhiboApplication.getUserInfo().uid))
                    mButton.setVisibility(View.INVISIBLE);
                else {
                    mButton.setVisibility(View.VISIBLE);
                    isFollow = data.is_follow == 1 ? true : false;//是否关注
                    setFollow(isFollow);
                    setFllowBtLayout();
                }
                setUserLayout();

                break;
            case SearchTabPresenter.SEARCH_STREAM:
                setFllowBtStatus("  直播  ");
                setStreamLayout();
                break;
            case SearchTabPresenter.SEARCH_VIDEO:
                setFllowBtStatus("  回放  ");
                setVideoLayout();
                break;
        }
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case SearchTabPresenter.SEARCH_USER:
                        long currentTime = System.currentTimeMillis();//频繁请求提示
                        if ((currentTime - mTime) < UserService.FOLLOW_SPACING_TIME) {
                            showMessage(UiUtils.getString("str_frequently_follow_prompt"));
                            break;
                        }
                        else {
                            mTime = System.currentTimeMillis();
                        }
                        mPresenter.follow(UserHomePresenter.isFollow(!isFollow));
                        break;
                    case SearchTabPresenter.SEARCH_STREAM:
                    case SearchTabPresenter.SEARCH_VIDEO:
                        EventBus.getDefault().post(mData, DISPATCH_EVENT_VIDEO);
                        break;


                }


            }
        });
        mButton.setNameSize(13);
    }

    private void setFllowBtStatus(String name) {
        mButton.setBackgroundResource(R.drawable.selector_follow_button);
        mButton.setNameColor(UiUtils.getColor(R.color.color_blue_button));
        mButton.setName(name);
    }

    private void setFllowBtLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(130)
                , AutoUtils.getPercentWidthSize(56));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.rightMargin = AutoUtils.getPercentWidthSize(35);
        mButton.setLayoutParams(layoutParams);
    }

    /**
     * 设置搜索回放item的布局
     */
    private void setVideoLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(140)
                , AutoUtils.getPercentWidthSize(140));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParams.leftMargin = AutoUtils.getPercentWidthSize(25);
        mIconIV.setLayoutParams(layoutParams);
        mCountIV.setBackgroundResource(R.mipmap.ico_people_online);
    }

    /**
     * 设置搜索直播item的布局
     */
    private void setStreamLayout() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(140)
                , AutoUtils.getPercentWidthSize(140));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        layoutParams.leftMargin = AutoUtils.getPercentWidthSize(25);
        mIconIV.setLayoutParams(layoutParams);
        mCountIV.setBackgroundResource(R.mipmap.ico_people_online);
    }

    /**
     * 设置搜索用户item的布局
     */
    private void setUserLayout() {
        mGenderIV.setVisibility(View.VISIBLE);
        mGenderIV.setImageResource(mData.user.sex == 1 ? R.mipmap.ico_sex_man : R.mipmap.ico_sex_woman);
    }

    /**
     * 用户
     *
     * @param data
     */
    private void setUserInfo(SearchResult data) {
        switch (type) {

            case SearchTabPresenter.SEARCH_USER:
                mTitleTV.setText(data.user.uname);
                UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), mIconIV, new GlideCircleTrasform(UiUtils.getContext()));
                mVerifiedIV.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);
                mSubheadTV.setText(TextUtils.isEmpty(data.user.intro) ? UiUtils.getString("str_default_intro") : data.user.intro
                );
                mFansTV.setText("" + data.user.fans_count);
                break;

            case SearchTabPresenter.SEARCH_STREAM:
                mTitleTV.setText(data.stream.title);
                UiUtils.glideWrap(data.stream.icon.getOrigin())
                        .placeholder(R.mipmap.pic_photo_140)
                        .into(mIconIV);
                mSubheadTV.setText(TextUtils.isEmpty(data.stream.location) ? UiUtils.getString("str_default_location") : data.stream.location
                );
                mFansTV.setText("" + data.stream.online_count);
                break;

            case SearchTabPresenter.SEARCH_VIDEO:
                mTitleTV.setText(data.video.video_title);
                UiUtils.glideWrap(data.video.video_icon.getOrigin())
                        .placeholder(R.mipmap.pic_photo_140)
                        .into(mIconIV);
                mSubheadTV.setText(TextUtils.isEmpty(data.video.video_location) ? UiUtils.getString("str_default_location") : data.video.video_location
                );
                mFansTV.setText("" + data.video.replay_count);
                break;

        }


    }

    @Override
    public SearchResult getUserInfo() {
        return mData;
    }

    @Override
    public void setFollowStatus(boolean isFollow) {
        if (isFollow) {
            mButton.setName(UiUtils.getString("str_already_follow"));
            mButton.setNameColor(UiUtils.getColor(R.color.white));
            mButton.setBackgroundResource(R.drawable.shape_stroke_corner_disable);
            mButton.setNameLeftDrawable(R.mipmap.ico_added_gz);
        }
        else {
            mButton.setName(UiUtils.getString("str_follow"));
            mButton.setBackgroundResource(R.drawable.shape_stroke_corner);
            mButton.setNameColor(UiUtils.getColor(R.color.color_blue_button));
            mButton.setNameLeftDrawable(null);
        }
    }

    @Override
    public void setFollowEnable(boolean isEnable) {
        mButton.setEnabled(isEnable);
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
