package com.zhiyicx.thinksnsplus.modules.mechanism.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
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

public class SearchMechainsimUserListAdapter extends CommonAdapter<UserInfoBean> {
    private FindSomeOneListContract.Presenter mPresenter;

    public SearchMechainsimUserListAdapter(Context context, int layoutId, List<UserInfoBean> datas, FindSomeOneListContract.Presenter presenter) {
        super(context, layoutId, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        setItemData(holder, userInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final UserInfoBean userInfoBean1, final int position) {
        holder.setVisible(R.id.tv_follow, userInfoBean1.getUser_id() == AppApplication.getMyUserIdWithdefault() || userInfoBean1.getFollower() ?
                View.GONE : View.VISIBLE);

        RxView.clicks(holder.getView(R.id.tv_follow))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (mPresenter.handleTouristControl()) { // 游客无入
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
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean1.getName());
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
        if (mPresenter.handleTouristControl()) { // 游客无入
            return;
        }
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

}
