package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.DigListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/12/11/15:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicDigListItem extends BaseDigItem {

    public DynamicDigListItem(Context context, DigListContract.Presenter presenter) {
        super(context, presenter);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof DynamicDigListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {
        final UserAvatarView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);
        DynamicDigListBean dynamicDigListBean = (DynamicDigListBean) baseListBean;
        if (dynamicDigListBean != null) {
            tv_name.setText(dynamicDigListBean.getDiggUserInfo().getName());
            tv_content.setText(dynamicDigListBean.getDiggUserInfo().getIntro());
            // 显示用户头像
            ImageUtils.loadCircleUserHeadPic(dynamicDigListBean.getDiggUserInfo(), filterImageView);
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
            if (dynamicDigListBean.getDiggUserInfo().isFollowing() && dynamicDigListBean.getDiggUserInfo().isFollower()) {
                iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
            } else if (dynamicDigListBean.getDiggUserInfo().isFollower()) {
                iv_follow.setImageResource(R.mipmap.detail_ico_followed);
            } else {
                iv_follow.setImageResource(R.mipmap.detail_ico_follow);
            }

            // 设置关注状态点击事件
            RxView.clicks(iv_follow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    //.compose(.<Void>bindToLifecycle())
                    .subscribe(aVoid -> handleFollowUser(position, dynamicDigListBean.getDiggUserInfo()));
        }
    }
}
