package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, FollowFansBean> implements FollowFansListContract.View {

    @Inject
    FollowFansListPresenter mFollowFansListPresenter;
    private List<FollowFansBean> datas = new ArrayList<>();

    @Override
    protected CommonAdapter<FollowFansBean> getAdapter() {
        return new CommonAdapter<FollowFansBean>(getContext(), R.layout.item_follow_fans_list, datas) {
            @Override
            protected void convert(ViewHolder holder, FollowFansBean followFansItemBean, int position) {
                setItemData(holder, followFansItemBean, position);
            }
        };
    }

    @Override
    protected void initData() {
        DaggerFollowFansListPresenterComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .followFansListPresenterModule(new FollowFansListPresenterModule(FollowFansListFragment.this))
                .build().inject(this);
        super.initData();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public void setPresenter(FollowFansListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    protected void requestNetData(int maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore, 20000, true);
    }

    @Override
    protected List<FollowFansBean> requestCacheData(int maxId, boolean isLoadMore) {
        return mPresenter.requestCacheData(maxId, isLoadMore, 20000, true);
    }

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    private void setItemData(ViewHolder holder, FollowFansBean followFansItemBean, int position) {
        UserInfoBean userInfoBean = followFansItemBean.getFllowedUser();
        if (userInfoBean == null) {
            return;
        }
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean.getName());
        holder.setText(R.id.tv_user_signature, userInfoBean.getIntro());
        // 修改点赞数量颜色
        String digContent = "点赞 " + "<" + 56 + ">";
        CharSequence charSequence = ColorPhrase.from(digContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_dig_count);
        digCount.setText(charSequence);
        // 头像加载
        ImageView headPic = holder.getView(R.id.iv_headpic);
        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(userInfoBean.getUserIcon())
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView(headPic)
                .build()
        );
        holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
        // 添加点击事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_user_follow))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 添加关注，或者取消关注
                    }
                });
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(to);
    }
}
