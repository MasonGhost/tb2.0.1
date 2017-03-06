package com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

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
        FilterImageView filterImageView = holder.getView(R.id.iv_headpic);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView iv_follow = holder.getView(R.id.iv_follow);

        UserInfoBean userInfoBean = followFansBean.getTargetUserInfo();
        if (userInfoBean != null) {
            tv_name.setText(userInfoBean.getName());
            tv_content.setText(userInfoBean.getIntro());
        }
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
