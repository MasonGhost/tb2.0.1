package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public class FollowFansListAdapter extends CommonAdapter<FollowFansBean> {
    private int pageType;
    private FollowFansListContract.Presenter mPresenter;

    public FollowFansListAdapter(Context context, int layoutId, List<FollowFansBean> datas, int pageType, FollowFansListContract.Presenter presenter) {
        super(context, layoutId, datas);
        this.pageType = pageType;
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, FollowFansBean followFansBean, int position) {
        setItemData(holder, followFansBean, position);
    }

    private void setItemData(final ViewHolder holder, final FollowFansBean followFansItemBean, final int position) {
        // 设置关注状态
        switch (followFansItemBean.getFollowState()) {
            case FollowFansBean.IFOLLOWED_STATE:
                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
                break;
            case FollowFansBean.UNFOLLOWED_STATE:
                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
                break;
            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed_eachother);
                break;
            default:
                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
        }
        RxView.clicks(holder.getView(R.id.iv_user_follow))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // 添加关注，或者取消关注
                        // 关注列表的逻辑操作：关注，互相关注 ---》未关注
                        // 粉丝列表的逻辑操作：互相关注 ---》未关注
                        LogUtils.i("old_state--》" + followFansItemBean.getFollowState());
                        switch (followFansItemBean.getFollowState()) {
                            // 当前已经关注状态
                            case FollowFansBean.IFOLLOWED_STATE:
                                mPresenter.cancleFollowUser(position, followFansItemBean);
                                break;
                            case FollowFansBean.UNFOLLOWED_STATE:
                                mPresenter.followUser(position, followFansItemBean);
                                break;
                            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                                mPresenter.cancleFollowUser(position, followFansItemBean);
                                break;
                            default:

                        }
                    }
                });


        /*UserInfoBean userInfoBean = null;
        if (pageType == FollowFansListFragment.FOLLOW_FRAGMENT_PAGE) {
            userInfoBean = followFansItemBean.getFllowedUser();
        } else if (pageType == FollowFansListFragment.FANS_FRAGMENT_PAGE) {
            userInfoBean = followFansItemBean.getUser();
        }*/
        // 设置用户信息
        UserInfoBean userInfoBean = followFansItemBean.getTargetUserInfo();
        if (userInfoBean == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            userInfoBean = new UserInfoBean();
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
                .errorPic(R.drawable.shape_default_image_circle)
                .placeholder(R.drawable.shape_default_image_circle)
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView(headPic)
                .build()
        );

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

    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getContext(), UserInfoActivity.class);
        getContext().startActivity(to);
    }

}
