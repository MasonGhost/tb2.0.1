package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class FollowFansListFragment extends TSListFragment<FollowFansListContract.Presenter, FollowFansItemBean> implements FollowFansListContract.View {

    @Inject
    FollowFansListPresenter mFollowFansListPresenter;
    private List<FollowFansItemBean> datas = new ArrayList<>();

    @Override
    protected CommonAdapter<FollowFansItemBean> getAdapter() {
        return new CommonAdapter<FollowFansItemBean>(getContext(), R.layout.item_follow_fans_list, datas) {
            @Override
            protected void convert(ViewHolder holder, FollowFansItemBean followFansItemBean, int position) {
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
        for (int i = 0; i < 10; i++) {
            FollowFansItemBean followFansItemBean = new FollowFansItemBean();
            followFansItemBean.setFollowState(0);
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUserIcon("http://image.xinmin.cn/2017/01/11/bedca80cdaa44849a813e7820fff8a26.jpg");
            userInfoBean.setName("魂行道");
            userInfoBean.setIntro("走在风中今天阳光突然好温柔，天的温柔地的温柔像你抱着我");
            followFansItemBean.setUserInfoBean(userInfoBean);
            datas.add(followFansItemBean);
        }
        refreshData();
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
    protected boolean insertOrUpdateData(@NotNull List<FollowFansItemBean> data) {
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

    public static FollowFansListFragment initFragment(Bundle bundle) {
        FollowFansListFragment followFansListFragment = new FollowFansListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    private void setItemData(ViewHolder holder, FollowFansItemBean followFansItemBean, int position) {
        UserInfoBean userInfoBean = followFansItemBean.getUserInfoBean();

        holder.setText(R.id.tv_name, userInfoBean.getName());
        holder.setText(R.id.tv_user_signature, userInfoBean.getIntro());

        String digContent = "点赞 " + "<" + 56 + ">";
        CharSequence charSequence = ColorPhrase.from(digContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(),R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_dig_count);
        digCount.setText(charSequence);

        ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        imageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(userInfoBean.getUserIcon())
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build()
        );
        holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);

    }
}
