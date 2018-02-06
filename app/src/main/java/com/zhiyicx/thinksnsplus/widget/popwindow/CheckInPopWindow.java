package com.zhiyicx.thinksnsplus.widget.popwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CheckInBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;


/**
 * @Describe 签到提示框
 * @Author Jungle68
 * @Date 2017/8/9
 * @Contact master.jungle68@gmail.com
 */

public class CheckInPopWindow extends PopupWindow {
    protected View mContentView;
    protected View mParentView;

    protected TextView mTvTotalCheckIn;
    protected TextView mTvTotoalGold;
    protected TextView mTvCheckInGetGold;
    protected TextView mTvCheckIn;
    protected RecyclerView mRvUserCheckInList;

    protected boolean isWrap = true;
    protected float mAlpha = CustomPopupWindow.POPUPWINDOW_ALPHA;
    protected Drawable mBackgroundDrawable = new ColorDrawable(0x00000000);// 默认为透明
    protected int mAnimationStyle = -1;

    private RecyclerView.LayoutManager mLayoutManager;
    private CommonAdapter mCommonAdapter;
    private List<UserInfoBean> mListData = new ArrayList<>();
    private OnCheckInClickListener mOnCheckInClickListener;
    private CheckInBean mCheckInBean;

    private double mWalletRatio = 100;
    private String mGoldName;

    public CheckInPopWindow(View parentView, CheckInBean checkInBean, String goldName, double mWalletRatio, OnCheckInClickListener l) {
        this.mParentView = parentView;
        this.mOnCheckInClickListener = l;
        this.mWalletRatio = mWalletRatio;
        this.mCheckInBean = checkInBean;
        this.mGoldName = goldName;
        initLayout();
        initData();
    }

    private void initData() {

    }

    protected void initLayout() {
        mContentView = LayoutInflater.from(mParentView.getContext()).inflate(R.layout.pop_check_in, null);
        setOnDismissListener(() -> setWindowAlpha(1.0f));
        setWidth(isWrap ? LinearLayout.LayoutParams.WRAP_CONTENT : LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(isWrap ? LinearLayout.LayoutParams.WRAP_CONTENT : LinearLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(mBackgroundDrawable);
        //如果设置了对话则使用对话
        if (mAnimationStyle != -1) {
            setAnimationStyle(mAnimationStyle);
        }
        setContentView(mContentView);

        mContentView.findViewById(R.id.iv_cancle).setOnClickListener(view -> dismiss());
        mTvTotalCheckIn = (TextView) mContentView.findViewById(R.id.tv_total_check_in);

        mTvTotoalGold = (TextView) mContentView.findViewById(R.id.tv_totoal_gold);

        mTvCheckInGetGold = (TextView) mContentView.findViewById(R.id.tv_check_in_get_gold);

        mTvCheckIn = (TextView) mContentView.findViewById(R.id.tv_check_in);
        RxView.clicks(mTvCheckIn)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mOnCheckInClickListener != null) {
                        mOnCheckInClickListener.onCheckInClick();
                    }
                });


        mRvUserCheckInList = (RecyclerView) mContentView.findViewById(R.id.rv_user_check_in_list);

        mLayoutManager = new LinearLayoutManager(mParentView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvUserCheckInList.setLayoutManager(mLayoutManager);
        mRvUserCheckInList.setHasFixedSize(true);
        mRvUserCheckInList.addItemDecoration(new LinearDecoration(0, 0, mParentView.getResources().getDimensionPixelOffset(com.zhiyicx.thinksnsplus
                .R.dimen.spacing_small), 0));


        mCommonAdapter = new CommonAdapter<UserInfoBean>(mParentView.getContext(), R.layout.item_check_in_user, mListData) {
            @Override
            protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {

                ImageUtils.loadUserHead(userInfoBean, (ImageView) holder.getView(R.id.iv_head), false);
                holder.setText(R.id.tv_rank, String.valueOf(position + 1));
                RxView.clicks(holder.getView(R.id.iv_head))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean));

            }
        };
        mRvUserCheckInList.setAdapter(mCommonAdapter);
        setData(mCheckInBean, mWalletRatio, mGoldName);
    }

    /**
     * 更新数据
     *
     * @param checkInBean
     * @param walletRatio
     */
    public void setData(CheckInBean checkInBean, double walletRatio, String goldName) {
        mTvTotalCheckIn.setText(mParentView.getContext().getString(R.string.check_in_total_day_format, checkInBean.getLast_checkin_count()));
        mTvTotoalGold.setText("+" + mContentView.getResources().getString(R.string.buy_pay_integration,(int)PayConfig.realCurrency2GameCurrency
                (checkInBean
                        .getAttach_balance(),
                (int)
                        walletRatio)));
        mListData.clear();
        mListData.addAll(checkInBean.getRank_users());
        mCommonAdapter.notifyDataSetChanged();
        if (checkInBean.isChecked_in()) {
            mTvCheckIn.setEnabled(false);
            mTvCheckIn.setText(mTvCheckIn.getResources().getString(R.string.checked));
        } else {
            mTvCheckIn.setEnabled(true);
            mTvCheckIn.setText(mTvCheckIn.getResources().getString(R.string.check_in));

        }
        if (!TextUtils.isEmpty(goldName)) {
            mTvCheckInGetGold.setText(mTvCheckInGetGold.getResources().getString(R.string.check_in_today_get_gold_format, goldName));
        }
    }


    /**
     * 默认显示到中间
     */
    public void show() {
        setWindowAlpha(mAlpha);
        if (mParentView == null) {
            showAtLocation(mContentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {
            showAtLocation(mParentView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    private void setWindowAlpha(float alpha) {
        try {
            WindowManager.LayoutParams params = ((Activity) mParentView.getContext()).getWindow().getAttributes();
            params.alpha = alpha;
            params.verticalMargin = 100;
            ((Activity) mParentView.getContext()).getWindow().setAttributes(params);
        } catch (Exception e) {
        }
    }

    public interface OnCheckInClickListener {
        void onCheckInClick();
    }

}
