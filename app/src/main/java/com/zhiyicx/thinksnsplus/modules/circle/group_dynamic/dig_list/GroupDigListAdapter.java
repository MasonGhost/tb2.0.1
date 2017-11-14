package com.zhiyicx.thinksnsplus.modules.circle.group_dynamic.dig_list;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/20
 * @contact email:648129313@qq.com
 */

public class GroupDigListAdapter extends CommonAdapter<DynamicDigListBean> {

    private GroupDigListContract.Presenter mPresenter;

    public GroupDigListAdapter(Context context, List<DynamicDigListBean> datas, GroupDigListContract.Presenter presenter) {
        super(context, R.layout.item_dig_list, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, DynamicDigListBean dynamicDigListBean, int position) {
        final UserAvatarView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        final UserInfoBean userInfoBean = dynamicDigListBean.getDiggUserInfo();
        if (userInfoBean != null) {
            tv_name.setText(userInfoBean.getName());
            tv_content.setText(userInfoBean.getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic(userInfoBean, filterImageView);

            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), userInfoBean));
        }
        // 如果当前列表包含了自己，就隐藏该关注按钮
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (dynamicDigListBean.getUser_id() == authBean.getUser_id()) {
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
                    .subscribe(aVoid -> mPresenter.handleFollowUser(position, dynamicDigListBean.getDiggUserInfo()));
        }
    }
}
