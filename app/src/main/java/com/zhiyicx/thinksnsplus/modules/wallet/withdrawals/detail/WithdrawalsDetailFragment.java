package com.zhiyicx.thinksnsplus.modules.wallet.withdrawals.detail;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawalsDetailBean;
import com.zhiyicx.thinksnsplus.modules.wallet.account.AccountActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/05/23/10:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WithdrawalsDetailFragment extends TSListFragment<WithdrawalsDetailConstract.Presenter, WithdrawalsDetailBean>
        implements WithdrawalsDetailConstract.View {

    @BindView(R.id.v_shadow)
    View mVshadow;

    private ActionPopupWindow mActionPopupWindow;

    public static WithdrawalsDetailFragment newInstance() {
        return new WithdrawalsDetailFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdrawals_detail;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.withdraw_details);
    }

    @Override
    protected void setCenterClick() {
        mActionPopupWindow.showTop();
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
//        mToolbarCenter.setCompoundDrawables(null,null,getResources().getDrawable(R.mipmap.arr),null);
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.withdraw_all))
                .item2Str(getString(R.string.withdraw_out))
                .item3Str(getString(R.string.withdraw_in))
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .item2ClickListener(new ActionPopupWindow.ActionPopupWindowItem2ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .item3ClickListener(new ActionPopupWindow.ActionPopupWindowItem3ClickListener() {
                    @Override
                    public void onItemClicked() {
                        mActionPopupWindow.hide();
                    }
                })
                .dismissListener(new ActionPopupWindow.ActionPopupWindowShowOrDismissListener() {
                    @Override
                    public void onShow() {
                        mVshadow.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onDismiss() {
                        mVshadow.setVisibility(View.GONE);
                    }
                })
                .build();
    }

    @Override
    protected void initData() {
        super.initData();
        WithdrawalsDetailBean test = new WithdrawalsDetailBean();
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
        mListDatas.add(test);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<WithdrawalsDetailBean>(getActivity(), R.layout.item_withdrawals_detail, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, WithdrawalsDetailBean s, int position) {
                TextView desc = holder.getView(R.id.withdrawals_desc);
                if (position % 2 == 0) {
                    desc.setEnabled(false);
                }
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                startActivity(new Intent(getActivity(), AccountActivity.class));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));

    }
}
