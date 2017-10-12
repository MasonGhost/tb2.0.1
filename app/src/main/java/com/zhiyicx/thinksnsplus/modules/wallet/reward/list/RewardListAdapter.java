package com.zhiyicx.thinksnsplus.modules.wallet.reward.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
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

public class RewardListAdapter extends CommonAdapter<RewardsListBean> {
    RewardType mRewardType;

    public RewardListAdapter(Context context, int layoutId, List<RewardsListBean> datas, RewardType rewardType) {
        super(context, layoutId, datas);
        mRewardType = rewardType;
    }

    @Override
    protected void convert(ViewHolder holder, RewardsListBean data, int position) {
        setItemData(holder, data, position);
    }

    private void setItemData(final ViewHolder holder, final RewardsListBean rewardsListBean, final int position) {

        // 设置用户信息
        final UserInfoBean userInfoBean = rewardsListBean.getUser();
        if (userInfoBean == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            return;
        }
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(rewardsListBean.getCreated_at()));
        /**
         *     INFO(10001), // 咨询 // requst code must lower 16 bit ,so id must be < 65000
         DYNAMIC(10002),// 动态
         USER(10003), // 用户
         QA_ANSWER(10004); // 问答回答
         */
        String result;
        switch (mRewardType) {
            case INFO:
                result = getContext().getResources().getString(R.string.reward_list_tip, "<" + userInfoBean.getName() + ">");
                break;
            case DYNAMIC:
                result = getContext().getResources().getString(R.string.reward_list_tip_dynamic, "<" + userInfoBean.getName() + ">");
                break;
            case USER:
                result = getContext().getResources().getString(R.string.reward_list_tip_user, "<" + userInfoBean.getName() + ">");
                break;
            case QA_ANSWER:
                result = getContext().getResources().getString(R.string.reward_list_tip_answer, "<" + userInfoBean.getName() + ">");
                break;
            default:
                result = getContext().getResources().getString(R.string.reward_list_tip, "<" + userInfoBean.getName() + ">");

        }

        CharSequence charSequence = ColorPhrase.from(result).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.important_for_content))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_content);
        digCount.setText(charSequence);
        // 头像加载
        ImageUtils.loadCircleUserHeadPic(userInfoBean, holder.getView(R.id.iv_headpic));
        // 添加点击事件
        RxView.clicks(holder.getView(R.id.iv_headpic))
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
