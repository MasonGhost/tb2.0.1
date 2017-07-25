package com.zhiyicx.thinksnsplus.modules.rank;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
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
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public class RankAdapter extends CommonAdapter<DigRankBean> {

    public RankAdapter(Context context, int layoutId, List<DigRankBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, DigRankBean digRankBean, int position) {
        setItemData(holder, digRankBean, position);
    }

    private void setItemData(final ViewHolder holder, final DigRankBean digRankBean, final int position) {
        // 设置关注状态   暂时没有关注操作
//        switch (digRankBean.getFollowState()) {
//            case FollowFansBean.IFOLLOWED_STATE:
//                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
//                break;
//            case FollowFansBean.UNFOLLOWED_STATE:
//                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
//                break;
//            case FollowFansBean.FOLLOWED_EACHOTHER_STATE:
//                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed_eachother);
//                break;
//            default:
//                holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
//        }
//        RxView.clicks(holder.getView(R.id.iv_user_follow))
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        // 添加关注，或者取消关注
//                        // 关注列表的逻辑操作：关注，互相关注 ---》未关注
//                        // 粉丝列表的逻辑操作：互相关注 ---》未关注
////                        LogUtils.i("old_state--》" + digRankBean.getFollowState());
//
//                    }
//                });

        // 设置用户信息
        final UserInfoBean userInfoBean = digRankBean.getDigUserInfo();
        if (userInfoBean == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            return;
        }
        /**
         * 如果关注粉丝列表中出现了自己，需要隐藏关注按钮
         */
//        holder.getView(R.id.iv_user_follow).setVisibility(
//                userInfoBean.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()? View.INVISIBLE:View.VISIBLE);
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean.getName());
        holder.setText(R.id.tv_user_signature, userInfoBean.getIntro());
        holder.setText(R.id.tv_rank, (position + 1) + "");
        // 修改点赞数量颜色
        String digCountString = digRankBean.getValue();
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
        ImageView headPic = holder.getView(R.id.iv_headpic);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, headPic);
        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(getContext(), userInfoBean));

    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

}
