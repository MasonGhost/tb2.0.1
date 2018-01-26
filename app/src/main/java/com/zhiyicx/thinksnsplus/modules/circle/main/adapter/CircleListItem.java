package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/11/14/13:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleListItem extends BaseCircleItem {

    private Drawable mPayDrawable;
    /**
     * 是否是我加入了的
     */
    private boolean mIsMineJoined;


    private Activity mContext;

    public CircleListItem(boolean isMineJoined, Context context, CircleItemItemEvent
            circleItemItemEvent) {
        super(circleItemItemEvent);
        mIsMineJoined = isMineJoined;
        mPayDrawable = UIUtils.getCompoundDrawables(context, R.mipmap.musici_pic_pay02);
    }

    public CircleListItem(boolean isMineJoined, Activity context, CircleItemItemEvent
            circleItemItemEvent, IBaseTouristPresenter presenter) {
        super(circleItemItemEvent);
        mContext = context;
        mIsMineJoined = isMineJoined;
        mPresenter = presenter;
        mPayDrawable = UIUtils.getCompoundDrawables(context, R.mipmap.musici_pic_pay02);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_circle_list;
    }

    @Override
    public boolean isForViewType(CircleInfo item, int position) {
        return item.getId() > 0;
    }

    @Override
    public void convert(ViewHolder holder, CircleInfo circleInfo, CircleInfo lastT, int position,
                        int itemCounts) {

        // 封面
        ImageView circleCover = holder.getView(R.id.iv_circle_cover);

        // 名字
        TextView circleName = holder.getView(R.id.tv_circle_name);
        circleName.setCompoundDrawables(null, null, CircleInfo.CirclePayMode.PAID.value.equals
                (circleInfo.getMode()) ? mPayDrawable : null, null);

        // 帖子数量
        TextView circleFeedCount = holder.getView(R.id.tv_circle_feed_count);

        // 成员数量
        TextView circleMemberCount = holder.getView(R.id.tv_circle_follow_count);

        Context context = holder.getConvertView().getContext();


        // 设置封面
        Glide.with(context)
                .load(circleInfo.getAvatar())
                .error(R.drawable.shape_default_image)
                .placeholder(R.drawable.shape_default_image)
                .into(circleCover);

        circleName.setText(circleInfo.getName());

        String feedCountNumber = ConvertUtils.numberConvert(circleInfo.getPosts_count());
        String feedContent = context.getString(R.string.circle_post) + " " + "<" +
                feedCountNumber + ">";
        CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                .format();
        circleFeedCount.setText(feedString);
        // 设置订阅人数
        String followCountNumber = ConvertUtils.numberConvert(circleInfo.getUsers_count());
        String followContent = context.getString(R.string.circle_member) + " " + "<" +
                followCountNumber + ">";
        CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                .format();
        circleMemberCount.setText(followString);

        // 界面标记---我加入的圈子，不需要加入操作
        if (mIsMineJoined) {
            TextView tvRole = holder.getView(R.id.tv_role);
            if (circleInfo.getJoined() != null && CircleInfo.CircleRole.FOUNDER.value.equals
                    (circleInfo.getJoined().getRole())) {
                tvRole.setVisibility(View.VISIBLE);
                tvRole.setBackgroundResource(R.drawable.shape_bg_circle_radus_gold);
                tvRole.setText(tvRole.getResources().getString(R.string.circle_master));
            } else if (circleInfo.getJoined() != null && CircleInfo.CircleRole.ADMINISTRATOR
                    .value.equals(circleInfo.getJoined().getRole())) {
                tvRole.setVisibility(View.VISIBLE);
                tvRole.setText(tvRole.getResources().getString(R.string.administrator));
                tvRole.setBackgroundResource(R.drawable.shape_bg_circle_radus_gray);
            } else {
                tvRole.setVisibility(View.GONE);
            }
            // 未加入的，需要申请加入
        } else {
            boolean isJoined = circleInfo.getJoined() != null;
            TextView tvRole = holder.getView(R.id.tv_role);
            tvRole.setVisibility(View.GONE);
            if (isJoined) {
                if (circleInfo.getJoined() != null && CircleInfo.CircleRole.FOUNDER.value.equals
                        (circleInfo.getJoined().getRole())) {
                    tvRole.setVisibility(View.VISIBLE);
                    tvRole.setBackgroundResource(R.drawable.shape_bg_circle_radus_gold);
                    tvRole.setText(tvRole.getResources().getString(R.string.circle_master));
                } else if (circleInfo.getJoined() != null && CircleInfo.CircleRole.ADMINISTRATOR
                        .value.equals(circleInfo.getJoined().getRole())) {
                    tvRole.setVisibility(View.VISIBLE);
                    tvRole.setText(tvRole.getResources().getString(R.string.administrator));
                    tvRole.setBackgroundResource(R.drawable.shape_bg_circle_radus_gray);
                } else {
                    tvRole.setVisibility(View.GONE);
                }
            }

            TextView circleSubscribeFrame = holder.getView(R.id.tv_circle_subscrib_frame);

            CheckBox circleSubscribe = holder.getView(R.id.tv_circle_subscrib);
            circleSubscribe.setVisibility(!isJoined ? View.VISIBLE : View.GONE);
            // 设置订阅状态

            circleSubscribe.setChecked(isJoined);
            circleSubscribe.setText(isJoined ? context.getString(R.string.group_joined) : context
                    .getString(R.string.join_group));
            circleSubscribe.setPadding(isJoined ? context.getResources().getDimensionPixelSize(R
                    .dimen.spacing_small) : context.getResources()
                    .getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
            circleSubscribe.setEnabled(false);

            boolean canChange = circleInfo.getAudit() == 1 && !isJoined;

            circleSubscribeFrame.setVisibility(canChange ? View.VISIBLE : View.GONE);

            RxView.clicks(circleSubscribeFrame)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                    .subscribe(aVoid -> {
                        if (mCircleItemItemEvent == null) {
                            return;
                        }
                        if (mPresenter != null && CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode())) {
                            if (!mPresenter.handleTouristControl()) {
                                initPayPopWindow(mContext, position, circleInfo, circleInfo.getMoney
                                        (), mPresenter.getRatio(), mPresenter.getGoldName(), R.string
                                        .buy_pay_circle_desc);
                            }
                            return;
                        }
                        mCircleItemItemEvent.dealCircleJoinOrExit(position, circleInfo);
                    });
        }

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mCircleItemItemEvent == null) {
                        return;
                    }
                    mCircleItemItemEvent.toCircleDetail(circleInfo);
                });
        RxView.clicks(holder.getView(R.id.iv_circle_cover))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {

                });
    }
}
