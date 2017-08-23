package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
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

    private void dealFollowState(CheckBox tvUserSubscrib, UserInfoBean userInfoBean) {
        tvUserSubscrib.setChecked(userInfoBean.getFollower());
        tvUserSubscrib.setText(userInfoBean.getFollower() ? mContext.getString(R.string.followed) : mContext.getString(R.string.follow));
    }

    private void dealRankType(int count, TextView tv) {
        String format = "";
        // 粉丝
        if (mRankType.equals(mContext.getString(R.string.rank_user_type_all))) {
            format = mContext.getString(R.string.rank_type_fans);
        }
        // 签到
        if (mRankType.equals(mContext.getString(R.string.rank_user_type_sign_in))) {
            format = mContext.getString(R.string.rank_type_check_in);
        }
        // 问答达人
        if (mRankType.equals(mContext.getString(R.string.rank_user_type_qa))) {
            format = mContext.getString(R.string.rank_type_answer_dig_count);
        }
        // 问答
        if (mRankType.equals(mContext.getString(R.string.rank_qa_type_day))
                || mRankType.equals(mContext.getString(R.string.rank_qa_type_week))
                || mRankType.equals(mContext.getString(R.string.rank_qa_type_month))) {
            format = mContext.getString(R.string.rank_type_answer_count);
        }
        // 动态
        if (mRankType.equals(mContext.getString(R.string.rank_dynamic_type_day))
                || mRankType.equals(mContext.getString(R.string.rank_dynamic_type_week))
                || mRankType.equals(mContext.getString(R.string.rank_dynamic_type_month))) {
            format = mContext.getString(R.string.rank_type_like_count);
        }
        // 资讯
        if (mRankType.equals(mContext.getString(R.string.rank_info_type_day))
                || mRankType.equals(mContext.getString(R.string.rank_info_type_week))
                || mRankType.equals(mContext.getString(R.string.rank_info_type_month))) {
            format = mContext.getString(R.string.rank_type_view_count);
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
        return item.getUser_id() != 0L && position != 0;
    }

    @Override
    public void convert(ViewHolder holder, UserInfoBean userInfoBean, UserInfoBean lastT, int position, int itemCounts) {
        // 排名
        int rank = userInfoBean.getExtra().getRank() == 0 ? position : userInfoBean.getExtra().getRank();
        holder.setText(R.id.tv_rank, String.valueOf(rank));
        holder.setTextColor(R.id.tv_rank, userInfoBean.getExtra().getRank() > 3 ?
                ContextCompat.getColor(mContext, R.color.normal_for_assist_text) : ContextCompat.getColor(mContext, R.color.themeColor));
        // 用户信息
        ImageUtils.loadCircleUserHeadPic(userInfoBean, holder.getView(R.id.iv_user_portrait));
        holder.setText(R.id.tv_user_name, userInfoBean.getName());
        // 排行的信息
        if (mRankType.equals(mContext.getString(R.string.rank_user_type_sign_in))){
            dealRankType(userInfoBean.getExtra().getCheckin_count(), holder.getView(R.id.tv_rank_type));
        } else {
            dealRankType(userInfoBean.getExtra().getCount(), holder.getView(R.id.tv_rank_type));
        }
        // 关注按钮
        CheckBox tvUserSubscrib = holder.getView(R.id.tv_user_subscrib);
        if (userInfoBean.getUser_id().equals(AppApplication.getmCurrentLoginAuth().getUser_id())){
            tvUserSubscrib.setVisibility(View.GONE);
        } else {
            tvUserSubscrib.setVisibility(View.VISIBLE);
            dealFollowState(tvUserSubscrib, userInfoBean);
            RxView.clicks(tvUserSubscrib)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        // 修改关注状态
                        userInfoBean.setFollower(!userInfoBean.getFollower());
                        dealFollowState(tvUserSubscrib, userInfoBean);
                        // 网络请求
                        mPresenter.handleFollowState(userInfoBean);
                    });
        }

    }
}
