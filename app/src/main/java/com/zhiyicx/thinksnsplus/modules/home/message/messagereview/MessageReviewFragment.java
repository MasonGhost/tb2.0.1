package com.zhiyicx.thinksnsplus.modules.home.message.messagereview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.PinnedBean;
import com.zhiyicx.thinksnsplus.data.beans.TSNotifyExtraBean;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter.BaseTopItem;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter.TopDyanmicCommentItem;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.NOTIFICATION_KEY_FEED_PINNED_COMMENT;
import static com.zhiyicx.thinksnsplus.config.NotificationConfig.NOTIFICATION_KEY_NEWS_PINNED_COMMENT;

/**
 * @Author Jliuer
 * @Date 2017/7/5/20:55
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageReviewFragment extends TSListFragment<MessageReviewContract.Presenter,
        BaseListBean> implements MessageReviewContract.View{

    public static final String REVIEW_LIST = "review_list";

    private String[] mTopTypes;
    private String mTopType;

    private ActionPopupWindow mReviewPopWindow;
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
        return new CustomLinearDecoration(0, 1, 0, 0, ContextCompat.getDrawable(getContext(), R.drawable.shape_recyclerview_divider));
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
        TopDyanmicCommentItem dyanmicCommentItem = new TopDyanmicCommentItem(getActivity(),mPresenter);
        multiItemTypeAdapter.addItemViewDelegate(dyanmicCommentItem);
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
        initTopPopWindow();
    }

    @Override
    protected void setCenterClick() {
        mActionPopupWindow.showTop();
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
        if (mActionPopupWindow != null) {
            return;
        }
        mActionPopupWindow = ActionPopupWindow.builder()
                .with(getActivity())
                .isFocus(true)
                .isOutsideTouch(true)
                .parentView(mDriver)
                .animationStyle(ActionPopupWindow.NO_ANIMATION)
                .item1Str(getString(R.string.stick_type_dynamic_commnet))
                .item2Str(getString(R.string.stick_type_news_commnet))
                .item3Str(getString(R.string.stick_type_group_commnet))
                .item4Str(getString(R.string.stick_type_group_join))
                .item1ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_all));
                    mTopType = mTopTypes[0];
                    mPresenter.requestNetData(mMaxId, false);
                    mActionPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_out));
                    mTopType = mTopTypes[1];
                    mPresenter.requestNetData(mMaxId, false);
                    mActionPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_in));
                    mTopType = mTopTypes[2];
                    mPresenter.requestNetData(mMaxId, false);
                    mActionPopupWindow.hide();
                })
                .item3ClickListener(() -> {
                    mToolbarCenter.setText(getString(R.string.withdraw_in));
                    mTopType = mTopTypes[3];
                    mPresenter.requestNetData(mMaxId, false);
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
}
