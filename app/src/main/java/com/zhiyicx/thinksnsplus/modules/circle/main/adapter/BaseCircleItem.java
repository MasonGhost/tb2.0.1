package com.zhiyicx.thinksnsplus.modules.circle.main.adapter;

import android.app.Activity;

import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/11/21/14:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseCircleItem implements ItemViewDelegate<CircleInfo> {

    public static final long MYJOINEDCIRCLE = -1L;
    public static final long RECOMMENDCIRCLE = -2L;
    protected PayPopWindow mPayPopWindow;
    protected IBaseTouristPresenter mPresenter;

    public BaseCircleItem() {
    }

    public BaseCircleItem(CircleItemItemEvent circleItemItemEvent) {
        mCircleItemItemEvent = circleItemItemEvent;
    }

    protected CircleItemItemEvent mCircleItemItemEvent;

    public interface CircleItemItemEvent {
        void toAllJoinedCircle(CircleInfo circleInfo);

        void toCircleDetail(CircleInfo circleInfo);

        void changeRecommend();

        void dealCircleJoinOrExit(int position, CircleInfo circleInfo);
    }

    /**
     * @param context
     * @param position
     * @param circleInfo
     * @param amout      金额
     * @param ratio      转换率
     * @param goldName   单位名称
     * @param strRes     描述文字
     */
    protected void initPayPopWindow(Activity context, final int position, CircleInfo circleInfo,
                                    long amout, int ratio, String goldName, int strRes) {

        mPayPopWindow = PayPopWindow.builder()
                .with(context)
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(context.getString(strRes) + context.getString(R
                        .string.buy_pay_member), PayConfig.realCurrency2GameCurrency(amout, ratio), goldName))
                .buildLinksStr(context.getString(R.string.buy_pay_member))
                .buildTitleStr(context.getString(R.string.buy_pay))
                .buildItem1Str(context.getString(R.string.buy_pay_in))
                .buildItem2Str(context.getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(context.getString(R.string.buy_pay_money), PayConfig.realCurrency2GameCurrency(amout, ratio)))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    if (mCircleItemItemEvent != null) {
                        mCircleItemItemEvent.dealCircleJoinOrExit(position, circleInfo);
                    }
                    mPayPopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayPopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayPopWindow.show();

    }

    public void setPresenter(IBaseTouristPresenter presenter) {
        mPresenter = presenter;
    }

    public CircleItemItemEvent getCircleItemItemEvent() {
        return mCircleItemItemEvent;
    }
}
