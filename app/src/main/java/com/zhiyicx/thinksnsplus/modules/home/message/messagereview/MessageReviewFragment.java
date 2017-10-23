package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter.TopDyanmicCommentItem;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter.TopNewsCommentItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewFragment extends TSListFragment<MessageReviewContract.Presenter,
        BaseListBean> implements MessageReviewContract.View {

    private String[] mTopTypes;
    private String mTopType;

    private ActionPopupWindow mActionPopupWindow;

    @BindView(R.id.v_shadow)
    View mVshadow;

    public static MessageReviewFragment newInstance(Bundle args) {
        MessageReviewFragment fragment = new MessageReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public String getType() {
        return mTopType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTopTypes = getResources().getStringArray(R.array.top_type);
        mTopType = mTopTypes[0];
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected MultiItemTypeAdapter<BaseListBean> getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        TopDyanmicCommentItem dyanmicCommentItem = new TopDyanmicCommentItem(getActivity(), mPresenter);
        TopNewsCommentItem newsCommentItem = new TopNewsCommentItem(getActivity(), mPresenter);
        multiItemTypeAdapter.addItemViewDelegate(dyanmicCommentItem);
        multiItemTypeAdapter.addItemViewDelegate(newsCommentItem);
        return multiItemTypeAdapter;
    }

    @Override
    protected String setCenterTitle() {
        mToolbarCenter.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToolbarCenter.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ico_detail_arrowdown, 0);
        return getString(R.string.review);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected void setCenterClick() {
        initTopPopWindow();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_review_list;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }

    private void initTopPopWindow() {
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.stick_type_dynamic_commnet))
                .item1Color(mTopType.equals(mTopTypes[0]) ? getColor(R.color.themeColor) : 0)
                .item2Str(getString(R.string.stick_type_news_commnet))
                .item2Color(mTopType.equals(mTopTypes[1]) ? getColor(R.color.themeColor) : 0)
//                .item3Str(getString(R.string.stick_type_group_commnet))
//                .item3Color(mTopType.equals(mTopTypes[2]) ? getColor(R.color.themeColor) : 0)
//                .item4Str(getString(R.string.stick_type_group_join))
//                .item4Color(mTopType.equals(mTopTypes[3]) ? getColor(R.color.themeColor) : 0)
                .item1ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.stick_type_dynamic_commnet));
                    mTopType = mTopTypes[0];
                    mPresenter.requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.stick_type_news_commnet));
                    mTopType = mTopTypes[1];
                    mPresenter.requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.stick_type_group_commnet));
                    mTopType = mTopTypes[2];
                    mPresenter.requestNetData(0L, false);
                    mActionPopupWindow.hide();
                })
                .item4ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.stick_type_group_join));
                    mTopType = mTopTypes[3];
                    mPresenter.requestNetData(0L, false);
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
        mActionPopupWindow.showTop();

    }
}
