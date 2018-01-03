package com.zhiyicx.thinksnsplus.modules.circle.manager.earning.record;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleEarningListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/06/02/15:42
 * @Email Jliuer@aliyun.com
 * @Description 账单
 */
public class EarningListFragment extends TSListFragment<EarningListContract.Presenter, CircleEarningListBean>
        implements EarningListContract.View, TimePickerView.OnTimeSelectListener {

    public static final String CIRCLE = "circle";
    public static final String TYPE = "type";

    public static final String TYPEALL = "all";
    public static final String TYPEJOIN = "join";
    public static final String TYPEPINNED = "pinned";

    @BindView(R.id.v_shadow)
    View mVshadow;

    private ActionPopupWindow mActionPopupWindow;
    private TimePickerView mTimePickerView;

    private String[] mTypes = new String[]{TYPEALL, TYPEJOIN, TYPEPINNED};
    private String[] mTitles;
    private String mType;
    private Long mStartTime;
    private Long mEndTime;
    private long circleId;

    public static EarningListFragment newInstance(Bundle bundle) {
        EarningListFragment fragment = new EarningListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_circle_screen;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mTimePickerView.show();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<CircleEarningListBean>(getActivity(), R.layout.item_withdrawals_detail, mListDatas) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void convert(ViewHolder holder, CircleEarningListBean recharge, int position) {
                TextView desc = holder.getView(R.id.withdrawals_desc);
                TextView time = holder.getView(R.id.withdrawals_time);
                TextView account = holder.getView(R.id.withdrawals_account);
                desc.setEnabled(true);
                String moneyStr = String.format(Locale.getDefault(), getString(R.string.dynamic_send_toll_select_money_),
                        PayConfig.realCurrency2GameCurrency(recharge.getAmount(), mPresenter.getRatio()));
                desc.setText("+ " + moneyStr);
                account.setText(recharge.getSubject());
                time.setText(TimeUtils.string2_ToDya_Yesterday_Week(recharge.getCreated_at()));
            }
        };
        return adapter;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CircleEarningListBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        mStartTime = mEndTime = null;
    }

    @Override
    public HeaderAndFooterWrapper getTSAdapter() {
        return mHeaderAndFooterWrapper;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals_detail;
    }

    @Override
    protected String setCenterTitle() {
        mToolbarCenter.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.detail);
    }

    @Override
    protected void setCenterClick() {
        mActionPopupWindow.showTop();
    }

    @Override
    public void onTimeSelect(Date date, View v) {

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        // 将时分秒,毫秒域清零
        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal1.set(Calendar.HOUR_OF_DAY, cal1.getActualMinimum(Calendar.HOUR_OF_DAY));
        cal1.set(Calendar.MINUTE, cal1.getActualMinimum(Calendar.MINUTE));
        cal1.set(Calendar.SECOND, cal1.getActualMinimum(Calendar.SECOND));
        cal1.set(Calendar.MILLISECOND, cal1.getActualMinimum(Calendar.MILLISECOND));
        mStartTime = cal1.getTimeInMillis() / 1000;

        cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal1.set(Calendar.HOUR_OF_DAY, cal1.getActualMaximum(Calendar.HOUR_OF_DAY));
        cal1.set(Calendar.MINUTE, cal1.getActualMaximum(Calendar.MINUTE));
        cal1.set(Calendar.SECOND, cal1.getActualMaximum(Calendar.SECOND));
        cal1.set(Calendar.MILLISECOND, cal1.getActualMaximum(Calendar.MILLISECOND));
        mEndTime = cal1.getTimeInMillis() / 1000;
        requestNetData(0L, false);

        LogUtils.d(mStartTime + "::" + mEndTime);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initTopPopWindow();
        initTimepicker();
    }

    @Override
    protected void initData() {
        mTitles = new String[]{getString(R.string.withdraw_all), getString(R.string.circle_earningn_member), getString(R.string.circle_earningn_top)};
        setCenterTitle(getArguments().getInt(TYPE));
        CircleInfo CircleInfo = getArguments().getParcelable(CIRCLE);
        circleId = CircleInfo.getId();
        super.initData();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public long getCircleId() {
        return circleId;
    }

    @Override
    public Long getStartTime() {
        return mStartTime;
    }

    @Override
    public Long getEndTime() {
        return mEndTime;
    }

    private void initTopPopWindow() {
        if (mActionPopupWindow != null) {
            return;
        }
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.withdraw_all))
                .item2Str(getString(R.string.circle_earningn_member))
                .item3Str(getString(R.string.circle_earningn_top))
                .item1ClickListener(() -> {
                    setCenterTitle(0);
                    requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    setCenterTitle(1);
                    requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    setCenterTitle(2);
                    requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .dismissListener(new ActionPopupWindow.ActionPopupWindowShowOrDismissListener() {
                    @Override
                    public void onShow() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowup, 0);
                        mVshadow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDismiss() {
                        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
                        mVshadow.setVisibility(View.GONE);
                    }
                })
                .build();
    }

    private void initTimepicker() {
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(2000, 0, 1);
        endDate.set(2020, 11, 31);

        mTimePickerView = new TimePickerView.Builder(mActivity, this)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, v -> {
                    TextView tvSubmit = (TextView) v.findViewById(R.id.btnSubmit);
                    TextView ivCancel = (TextView) v.findViewById(R.id.btnCancel);
                    tvSubmit.setOnClickListener(v12 -> {
                        mTimePickerView.returnData();
                        mTimePickerView.dismiss();
                    });
                    ivCancel.setOnClickListener(v1 -> mTimePickerView.dismiss());
                })
                .setContentSize(18)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.5f)
                //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(false)
//                .setDividerColor(0xFF24AD9D)
                .build();
    }

    private void setCenterTitle(int pos) {
        mType = mTypes[pos];
        mToolbarCenter.setText(mTitles[pos]);

    }
}
