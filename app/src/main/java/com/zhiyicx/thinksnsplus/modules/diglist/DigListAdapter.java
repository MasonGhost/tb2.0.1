package com.zhiyicx.thinksnsplus.modules.diglist;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListAdapter extends CommonAdapter<BaseListBean> {
    private DigListContract.Presenter mPresenter;

    public DigListAdapter(Context context, int layoutId, List<BaseListBean> datas, DigListContract.Presenter mPresenter) {
        super(context, layoutId, datas);
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(ViewHolder holder, final BaseListBean digListBeans, final int position) {
        final UserAvatarView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        if (digListBeans instanceof DynamicDigListBean) {
            DynamicDigListBean digListBean = (DynamicDigListBean) digListBeans;

            tv_name.setText(digListBean.getDiggUserInfo().getName());
            tv_content.setText(digListBean.getDiggUserInfo().getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic(digListBean.getDiggUserInfo(), filterImageView);
            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), digListBean.getDiggUserInfo()));
            // 如果当前列表包含了自己，就隐藏该关注按钮
            AuthBean authBean = AppApplication.getmCurrentLoginAuth();
            if (digListBean.getUser_id() == authBean.getUser_id()) {
                iv_follow.setVisibility(View.GONE);
            } else {
                iv_follow.setVisibility(View.VISIBLE);
                if (digListBean.getDiggUserInfo().isFollowing() && digListBean.getDiggUserInfo().isFollower()) {
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
                } else if (digListBean.getDiggUserInfo().isFollower()) {
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed);
                } else {
                    iv_follow.setImageResource(R.mipmap.detail_ico_follow);
                }

                // 设置关注状态点击事件
                RxView.clicks(iv_follow)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> mPresenter.handleFollowUser(position, digListBean.getDiggUserInfo()));
            }
        } else if (digListBeans instanceof AnswerDigListBean) {
            AnswerDigListBean digListBean = (AnswerDigListBean) digListBeans;

            tv_name.setText(digListBean.getDiggUserInfo().getName());
            tv_content.setText(digListBean.getDiggUserInfo().getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic(digListBean.getDiggUserInfo(), filterImageView);
            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), digListBean.getDiggUserInfo()));
            // 如果当前列表包含了自己，就隐藏该关注按钮
            AuthBean authBean = AppApplication.getmCurrentLoginAuth();
            if (digListBean.getUser_id() == authBean.getUser_id()) {
                iv_follow.setVisibility(View.GONE);
            } else {
                iv_follow.setVisibility(View.VISIBLE);
                if (digListBean.getDiggUserInfo().isFollowing() && digListBean.getDiggUserInfo().isFollower()) {
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
                } else if (digListBean.getDiggUserInfo().isFollower()) {
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed);
                } else {
                    iv_follow.setImageResource(R.mipmap.detail_ico_follow);
                }

                // 设置关注状态点击事件
                RxView.clicks(iv_follow)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> mPresenter.handleFollowUser(position, digListBean.getDiggUserInfo()));
            }


        }
    }
}
