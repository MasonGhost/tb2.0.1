package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig;
import com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeItem implements ItemViewDelegate<UserInfoBean> {

    private String mRankType; // 排序的type
    private RankTypeListContract.Presenter mPresenter;
    private Context mContext;

    public RankTypeItem(Context context, String rankType, RankTypeListContract.Presenter presenter) {
        this.mContext = context;
        this.mRankType = rankType;
        this.mPresenter = presenter;
    }

    private void dealFollowState(ViewHolder holder, UserInfoBean userInfoBean) {
        if (userInfoBean.isFollowing() && userInfoBean.isFollower()) {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed_eachother);
        } else if (userInfoBean.isFollower()) {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
        } else {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
        }
    }

    private void dealRankType(UserInfoBean userInfoBean, TextView tv) {
        String format = "";
        int count = 0;
        // 粉丝
        switch (mRankType) {
            case RankTypeConfig.RANK_USER_FOLLOWER:
                format = mContext.getString(R.string.rank_type_fans);
                count=userInfoBean.getExtra().getFollowers_count();
                break;
            case RankTypeConfig.RANK_USER_CHECK_ID:
                format = mContext.getString(R.string.rank_type_check_in);
                count=userInfoBean.getExtra().getCheckin_count();

                break;
            case RankTypeConfig.RANK_USER_QUESTION_LIKE:
                format = mContext.getString(R.string.rank_type_answer_dig_count);
                count=userInfoBean.getExtra().getCount();

                break;
            case RankTypeConfig.RANK_QUESTION_DAY:
            case RankTypeConfig.RANK_QUESTION_WEEK:
            case RankTypeConfig.RANK_QUESTION_MONTH:
                format = mContext.getString(R.string.rank_type_answer_count);
                count=userInfoBean.getExtra().getCount();

                break;
            case RankTypeConfig.RANK_DYNAMIC_DAY:
            case RankTypeConfig.RANK_DYNAMIC_WEEK:
            case RankTypeConfig.RANK_DYNAMIC_MONTH:
                format = mContext.getString(R.string.rank_type_like_count);
                count=userInfoBean.getExtra().getCount();

                break;
            case RankTypeConfig.RANK_INFORMATION_DAY:
            case RankTypeConfig.RANK_INFORMATION_WEEK:
            case RankTypeConfig.RANK_INFORMATION_MONTH:
                format = mContext.getString(R.string.rank_type_view_count);
                count=userInfoBean.getExtra().getCount();

                break;
            default:
        }
        if (TextUtils.isEmpty(format)) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(String.format(format, count));
        }
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_rank_type_list;
    }

    @Override
    public boolean isForViewType(UserInfoBean item, int position) {
        return item.getUser_id() != 0L/* && position != 0*/;
    }

    @Override
    public void convert(ViewHolder holder, UserInfoBean userInfoBean, UserInfoBean lastT, int position, int itemCounts) {
        // 排名
        int rank = userInfoBean.getExtra().getRank() == 0 ? position + 1 : userInfoBean.getExtra().getRank();
        holder.setText(R.id.tv_rank, String.valueOf(rank));
        holder.setTextColor(R.id.tv_rank, rank > 3 ?
                ContextCompat.getColor(mContext, R.color.normal_for_assist_text) : ContextCompat.getColor(mContext, R.color.themeColor));
        // 用户信息
        ImageUtils.loadCircleUserHeadPic(userInfoBean, holder.getView(R.id.iv_user_portrait));
        holder.setText(R.id.tv_user_name, userInfoBean.getName());
        // 排行的信息
        if (mRankType.equals(RankTypeConfig.RANK_USER_CHECK_ID)) {
            dealRankType(userInfoBean, holder.getView(R.id.tv_rank_type));
        } else {
            dealRankType(userInfoBean, holder.getView(R.id.tv_rank_type));
        }
        // 关注按钮
        ImageView ivUserFollow = holder.getView(R.id.iv_user_follow);
        if (userInfoBean.getUser_id().equals(AppApplication.getMyUserIdWithdefault())) {
            ivUserFollow.setVisibility(View.GONE);
        } else {
            ivUserFollow.setVisibility(View.VISIBLE);
            dealFollowState(holder, userInfoBean);
            RxView.clicks(ivUserFollow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        if (mPresenter.handleTouristControl()) { // 游客勿入
                            return;
                        }
                        // 修改关注状态
                        userInfoBean.setFollower(!userInfoBean.getFollower());
                        dealFollowState(holder, userInfoBean);
                        // 网络请求
                        mPresenter.handleFollowState(userInfoBean);
                    });
        }

    }
}
