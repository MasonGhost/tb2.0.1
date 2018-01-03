package com.zhiyicx.thinksnsplus.modules.circle.manager.report;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleReportListBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/12/14/9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ReporReviewFragment extends TSListFragment<ReporReviewContract.Presenter, CircleReportListBean>
        implements ReporReviewContract.View, TimePickerView.OnTimeSelectListener {

    public static final String SOURCEID = "sourceid";
    private ActionPopupWindow mActionPopupWindow;
    private Integer[] status = new Integer[]{null, 1, 0, 2};
    private Integer chooseStatus;

    @BindView(R.id.v_shadow)
    View mVshadow;

    private TimePickerView mTimePickerView;
    private Long mStartTime;
    private Long mEndTime;

    public static ReporReviewFragment newInstance(Bundle bundle) {
        ReporReviewFragment memberListFragment = new ReporReviewFragment();
        memberListFragment.setArguments(bundle);
        return memberListFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initTimepicker();
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
    protected String setCenterTitle() {
        mToolbarCenter.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.circle_report);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_review_list;
    }

    @Override
    protected void setCenterClick() {
        initTopPopWindow();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public Long getSourceId() {
        return getArguments().getLong(SOURCEID);
    }

    @Override
    public Integer getStatus() {
        return chooseStatus;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(mActivity, mListDatas);
        adapter.addItemViewDelegate(new CircleReportItem(mActivity, mPresenter));
        return adapter;
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
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.all))
                .item1Color(chooseStatus == null ? getColor(R.color.themeColor) : 0)
                .item2Str(getString(R.string.circle_report_done))
                .item2Color(status[1].equals(chooseStatus) ? getColor(R.color.themeColor) : 0)
                .item3Str(getString(R.string.circle_report_doing))
                .item3Color(status[2].equals(chooseStatus) ? getColor(R.color.themeColor) : 0)
                .item4Str(getString(R.string.circle_report_refuse))
                .item4Color(status[3].equals(chooseStatus) ? getColor(R.color.themeColor) : 0)
                .item1ClickListener(() -> chooseType(getString(R.string.circle_report), 0))
                .item2ClickListener(() -> chooseType(getString(R.string.circle_report_done), 1))
                .item3ClickListener(() -> chooseType(getString(R.string.circle_report_doing), 2))
                .item4ClickListener(() -> chooseType(getString(R.string.circle_report_refuse), 3))
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
        mActionPopupWindow.showTop();
    }

    private void chooseType(String title, int position) {
        mToolbarCenter.setText(title);
        chooseStatus = status[position];
        if (mPresenter != null) {
            mRefreshlayout.autoRefresh();
        }
        if (mActionPopupWindow != null) {
            mActionPopupWindow.hide();
        }
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
}
