package com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.InputLimitView.OnSendClickListener;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailHeader;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailCommentItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2018/3/21
 * @Contact master.jungle68@gmail.com
 */
public class DynamicCommentListFragment extends TSListFragment<DynamicCommentListContract.Presenter, DynamicCommentBean>
        implements DynamicCommentListContract.View, OnUserInfoClickListener, OnCommentTextClickListener,
        OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, DynamicDetailHeader.OnImageClickLisenter,
        TextViewUtils.OnSpanTextClickListener, DynamicDetailCommentItem.OnCommentResendListener {
    public static final String DYNAMIC_DETAIL_DATA = "dynamic_detail_data";
    public static final String DYNAMIC_DETAIL_DATA_TYPE = "dynamic_detail_data_type";
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;

    private DynamicDetailBeanV2 mDynamicBean;// 上一个页面传进来的数据

    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;
    private PayPopWindow mPayImagePopWindow;

    private ActionPopupWindow mReSendCommentPopWindow;

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_comment_list_tb;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {

        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    protected void initData() {
        // 处理上个页面传过来的动态数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            if (mDynamicBean != null) {
                //        设置动态详情列表数据
                onNetResponseSuccess(mDynamicBean.getComments(), false);
            }
        }
        mIlvComment.setSendButtonVisiable(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mDynamicBean != null && mPresenter.checkCurrentDynamicIsDeleted(mDynamicBean.getUser_id(), mDynamicBean.getFeed_mark())) {// 检测动态是否已经被删除了
            dynamicHasBeDeleted();
        }
    }

    @Override
    protected MultiItemTypeAdapter<DynamicCommentBean> getAdapter() {
        MultiItemTypeAdapter<DynamicCommentBean> adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        DynamicDetailCommentItem dynamicDetailCommentItem = new DynamicDetailCommentItem();
        dynamicDetailCommentItem.setOnUserInfoClickListener(this);
        dynamicDetailCommentItem.setOnCommentTextClickListener(this);
        dynamicDetailCommentItem.setOnCommentResendListener(this);
        adapter.addItemViewDelegate(dynamicDetailCommentItem);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onImageClick(int iamgePosition, long amount, int note) {
        initImageCenterPopWindow(iamgePosition, amount, note, R.string.buy_pay_words_desc, true);
    }

    public static DynamicCommentListFragment initFragment(Bundle bundle) {
        DynamicCommentListFragment dynamicDetailFragment = new DynamicCommentListFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

    @Override
    public void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead) {
        initImageCenterPopWindow(position, amount,
                note, R.string.buy_pay_words_desc, false);
    }

    @Override
    public DynamicDetailBeanV2 getCurrentDynamic() {
        return mDynamicBean;
    }

    @Override
    public Bundle getArgumentsBundle() {
        return getArguments();
    }

    @Override
    public void dynamicHasBeDeleted() {
        setLoadViewHolderImag(R.mipmap.img_default_delete);
        showLoadViewLoadErrorDisableClick();
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    /**
     * 设置toolBar上面的关注状态
     */
    private void setToolBarRightFollowState(UserInfoBean userInfoBean1) {

    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mPresenter.sendCommentV2(mReplyUserId, text);
    }

    @Override
    public void reSendComment(DynamicCommentBean dynamicCommentBean) {
        initReSendCommentPopupWindow(dynamicCommentBean, getCurrentDynamic().getId());
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        handleItemClick(position);
    }

    private void handleItemClick(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {
            if (mListDatas.get(position).getComment_id() != null) {
                initDeleteComentPopupWindow(mListDatas.get(position).getComment_id(), position);
                mDeletCommentPopWindow.show();
            }
        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            showCommentView();
            String contentHint = getString(R.string.default_input_hint);
            if (mListDatas.get(position).getUser_id() != AppApplication.getmCurrentLoginAuth().getUser_id()) {
                contentHint = getString(R.string.reply, mListDatas.get(position).getCommentUser().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        goReportComment(position);

        return false;
    }

    private void goReportComment(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        // 举报
        if (mListDatas.get(position).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            ReportActivity.startReportActivity(mActivity, new ReportResourceBean(mListDatas.get(position).getCommentUser(), mListDatas.get
                    (position).getComment_id().toString(),
                    null, null, mListDatas.get(position).getComment_content(), ReportType.COMMENT));

        } else {

        }
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    /**
     * 初始化评论删除选择弹框
     *
     * @param comment_id      dynamic comment id
     * @param commentPosition current comment position
     */
    private void initDeleteComentPopupWindow(final long comment_id, final int commentPosition) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(BuildConfig.USE_TOLL ? getString(R.string.dynamic_list_top_comment) : null)
                .item2Str(getString(R.string.dynamic_list_delete_comment))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    StickTopFragment.startSticTopActivity(getActivity(), StickTopFragment.TYPE_DYNAMIC, getCurrentDynamic().getId(), comment_id);
                    mDeletCommentPopWindow.hide();
                })
                .item2ClickListener(() -> {
                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mPresenter.deleteCommentV2(comment_id, commentPosition);
                    }, true);
                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }


    /**
     * @param imagePosition 图片位置
     * @param amout         费用
     * @param note          支付节点
     * @param strRes        文字说明
     * @param isImage       是否是图片收费
     */
    private void initImageCenterPopWindow(final int imagePosition, long amout,
                                          final int note, int strRes, final boolean isImage) {
        mPayImagePopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes) + getString(R
                        .string.buy_pay_member), amout, mPresenter.getIntegrationGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_integration), amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPayImagePopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayImagePopWindow.hide())
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
        mPayImagePopWindow.show();
    }

    /**
     * 初始化重发评论选择弹框
     */
    private void initReSendCommentPopupWindow(final DynamicCommentBean commentBean, final long
            feed_id) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mReSendCommentPopWindow.hide();
                    mPresenter.reSendComment(commentBean, feed_id);
                })
                .bottomClickListener(() -> mReSendCommentPopWindow.hide())
                .build();
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onCommentTextClick(int position) {
        handleItemClick(position);
    }

    @Override
    public void onCommentTextLongClick(int position) {
        goReportComment(position);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getActivity().finish();
        getActivity().overridePendingTransition(-1, R.anim.slide_out_bottom);

    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    public void onDestroyView() {
        dismissPop(mDeletCommentPopWindow);
        dismissPop(mPayImagePopWindow);
        dismissPop(mReSendCommentPopWindow);
        super.onDestroyView();
    }
}
