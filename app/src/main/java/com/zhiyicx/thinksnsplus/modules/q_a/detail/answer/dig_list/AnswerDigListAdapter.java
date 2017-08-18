package com.zhiyicx.thinksnsplus.modules.q_a.detail.answer.dig_list;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @Author Jliuer
 * @Date 2017/8/18 9:54
 * @Email Jliuer@aliyun.com
 * @Description 
 */
public class AnswerDigListAdapter extends CommonAdapter<AnswerDigListBean> {
    private AnswerDigListContract.Presenter mPresenter;

    public AnswerDigListAdapter(Context context, int layoutId, List<AnswerDigListBean> datas, AnswerDigListContract.Presenter mPresenter) {
        super(context, layoutId, datas);
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(ViewHolder holder, final AnswerDigListBean dynamicDigListBean, final int position) {
        final UserAvatarView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        if (dynamicDigListBean != null) {
            tv_name.setText(dynamicDigListBean.getDiggUserInfo().getName());
            tv_content.setText(dynamicDigListBean.getDiggUserInfo().getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic( dynamicDigListBean.getDiggUserInfo(), filterImageView);
            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), dynamicDigListBean.getDiggUserInfo()));
        }
        // 如果当前列表包含了自己，就隐藏该关注按钮
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (dynamicDigListBean != null && dynamicDigListBean.getUser_id() == authBean.getUser_id()) {
            iv_follow.setVisibility(View.GONE);
        } else {
            iv_follow.setVisibility(View.VISIBLE);
            if(dynamicDigListBean.getDiggUserInfo().isFollowing()&&dynamicDigListBean.getDiggUserInfo().isFollower()){
                iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
            }else if(dynamicDigListBean.getDiggUserInfo().isFollower()){
                iv_follow.setImageResource(R.mipmap.detail_ico_followed);
            }else {
                iv_follow.setImageResource(R.mipmap.detail_ico_follow);
            }

            // 设置关注状态点击事件
            RxView.clicks(iv_follow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    //.compose(.<Void>bindToLifecycle())
                    .subscribe(aVoid -> mPresenter.handleFollowUser(position, dynamicDigListBean.getDiggUserInfo()));
        }

    }
}
