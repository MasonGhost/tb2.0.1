package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.baseproject.utils.ImageUtils.DEFAULT_IMAGE_ID;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/6
 * @contact email:450127106@qq.com
 */

public class DigListAdapter extends CommonAdapter<FollowFansBean> {
    private DigListContract.Presenter mPresenter;

    public DigListAdapter(Context context, int layoutId, List<FollowFansBean> datas, DigListContract.Presenter mPresenter) {
        super(context, layoutId, datas);
        this.mPresenter = mPresenter;
    }

    @Override
    protected void convert(ViewHolder holder, final FollowFansBean followFansBean, final int position) {
        final FilterImageView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        final UserInfoBean userInfoBean = followFansBean.getTargetUserInfo();
        if (userInfoBean != null) {
            tv_name.setText(userInfoBean.getName());
            tv_content.setText(userInfoBean.getIntro());
            // 显示用户头像
            ImageLoader imageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
            int storegeId;
            String userIconUrl;
            try {
                storegeId = Integer.parseInt(userInfoBean.getAvatar());
                userIconUrl = ImageUtils.imagePathConvertV2(storegeId
                        , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                        , getContext().getResources().getDimensionPixelOffset(R.dimen.headpic_for_list)
                        , ImageZipConfig.IMAGE_38_ZIP);
            } catch (Exception e) {
                userIconUrl = userInfoBean.getAvatar();
            }
            imageLoader.loadImage(filterImageView.getContext(), GlideImageConfig.builder()
                    .imagerView(filterImageView)
                    .transformation(new GlideCircleTransform(filterImageView.getContext()))
                    .url(userIconUrl)
                    .placeholder(R.mipmap.pic_default_portrait1)
                    .errorPic(R.mipmap.pic_default_portrait1)
                    .build());
            RxView.clicks(holder.getConvertView())
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(filterImageView.getContext(), userInfoBean));
        }
        // 如果当前列表包含了自己，就隐藏该关注按钮
        AuthBean authBean = AppApplication.getmCurrentLoginAuth();
        if (userInfoBean != null && userInfoBean.getUser_id() == authBean.getUser_id()) {
            iv_follow.setVisibility(View.GONE);
        } else {
            iv_follow.setVisibility(View.VISIBLE);
            // 设置关注状态
            int state = followFansBean.getFollowState();
            switch (state) {
                case FollowFansBean.UNFOLLOWED_STATE:
                    iv_follow.setImageResource(R.mipmap.detail_ico_follow);
                    break;
                case FollowFansBean.IFOLLOWED_STATE:
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed);
                    break;
                case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
                    iv_follow.setImageResource(R.mipmap.detail_ico_followed_eachother);
                    break;
                default:
            }

            // 设置关注状态点击事件
            RxView.clicks(iv_follow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    //.compose(.<Void>bindToLifecycle())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            mPresenter.handleFollowUser(position, followFansBean);
                        }
                    });
        }

    }
}
