package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */

public class FindSomeOneNearbyListAdapter extends CommonAdapter<NearbyBean> {
    private FindSomeOneNearbyListContract.Presenter mPresenter;

    public FindSomeOneNearbyListAdapter(Context context, int layoutId, List<NearbyBean> datas, FindSomeOneNearbyListContract.Presenter presenter) {
        super(context, layoutId, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, NearbyBean userInfoBean, int position) {
        setItemData(holder, userInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final NearbyBean nearbyBean, final int position) {
        UserInfoBean userInfoBean1 = nearbyBean.getUser();
        if (userInfoBean1 == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            return;
        }
        if (userInfoBean1.isFollowing() && userInfoBean1.isFollower()) {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed_eachother);
        } else if (userInfoBean1.isFollower()) {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
        } else {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
        }

        RxView.clicks(holder.getView(R.id.iv_user_follow))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (mPresenter.handleTouristControl() || userInfoBean1.getHas_deleted()) {
                        return;
                    }
                    // 添加关注，或者取消关注
                    // 关注列表的逻辑操作：关注，互相关注 ---》未关注
                    // 粉丝列表的逻辑操作：互相关注 ---》未关注

                    if (userInfoBean1.isFollowing() && userInfoBean1.isFollower()) {
                        mPresenter.cancleFollowUser(position, userInfoBean1);
                    } else if (userInfoBean1.isFollower()) {
                        mPresenter.cancleFollowUser(position, userInfoBean1);
                    } else {
                        mPresenter.followUser(position, userInfoBean1);
                    }

                });


        /**
         * 如果关注粉丝列表中出现了自己，需要隐藏关注按钮
         */
        holder.getView(R.id.iv_user_follow).setVisibility(
                userInfoBean1.getUser_id() == AppApplication.getMyUserIdWithdefault() ? View.GONE : View.VISIBLE);
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean1.getName());

        holder.setText(R.id.tv_user_signature, TextUtils.isEmpty(userInfoBean1.getIntro()) ? getContext().getString(R.string.intro_default) : userInfoBean1.getIntro());
        // 修改点赞数量颜色
        String digCountString = userInfoBean1.getExtra().getLikes_count() + "";
        // 当前没有获取到点赞数量，设置为0，否则ColorPhrase会抛出异常
        if (TextUtils.isEmpty(digCountString)) {
            digCountString = 0 + "";
        }
        String digContent = "点赞 " + "<" + digCountString + ">";
        CharSequence charSequence = ColorPhrase.from(digContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_dig_count);
        digCount.setText(charSequence);
        // 头像加载
        ImageUtils.loadCircleUserHeadPic(userInfoBean1, holder.getView(R.id.iv_headpic));
        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(getContext(), userInfoBean1));
    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        if (mPresenter.handleTouristControl()) {
            return;
        }
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

}
