package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */

public class InfoDigListAdapter extends CommonAdapter<InfoDigListBean> {

    private InfoDigListContract.Presenter mPresenter;

    public InfoDigListAdapter(Context context, List<InfoDigListBean> datas, InfoDigListContract.Presenter presenter) {
        super(context, R.layout.item_dig_list, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, InfoDigListBean infoDigListBean, int position) {
        final UserAvatarView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        if (infoDigListBean != null) {
            tv_name.setText(infoDigListBean.getDiggUserInfo().getName());
            tv_content.setText(infoDigListBean.getDiggUserInfo().getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic(infoDigListBean.getDiggUserInfo(), filterImageView);
            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), infoDigListBean.getDiggUserInfo()));
        }
        // 如果当前列表包含了自己，就隐藏该关注按钮
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (infoDigListBean != null && infoDigListBean.getUser_id() == authBean.getUser_id()) {
            iv_follow.setVisibility(View.GONE);
        } else {
            iv_follow.setVisibility(View.VISIBLE);
            if (infoDigListBean.getDiggUserInfo().isFollowing() && infoDigListBean.getDiggUserInfo().isFollower()) {
                iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
            } else if (infoDigListBean.getDiggUserInfo().isFollower()) {
                iv_follow.setImageResource(R.mipmap.detail_ico_followed);
            } else {
                iv_follow.setImageResource(R.mipmap.detail_ico_follow);
            }

            // 设置关注状态点击事件
            RxView.clicks(iv_follow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    //.compose(.<Void>bindToLifecycle())
                    .subscribe(aVoid -> mPresenter.handleFollowUser(position, infoDigListBean.getDiggUserInfo()));
        }
    }
}
