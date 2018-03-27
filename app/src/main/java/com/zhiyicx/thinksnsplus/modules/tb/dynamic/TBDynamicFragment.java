package com.zhiyicx.thinksnsplus.modules.tb.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment.DynamicCommentListActivity;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment.DynamicCommentListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareActivity;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareBean;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.simple.eventbus.Subscriber;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.CAN_COMMENT;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class TBDynamicFragment extends DynamicFragment {
    public static final String BUNDLE_SHARE_DATA = "data";

    private int mCurrentClickItemPosition = -1;

    public static TBDynamicFragment newInstance(String dynamicType, OnCommentClickListener l) {
        TBDynamicFragment fragment = new TBDynamicFragment();
        fragment.setOnCommentClickListener(l);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    public static TBDynamicFragment newInstance(String dynamicType, OnCommentClickListener l, UserInfoBean userInfoBean) {
        TBDynamicFragment fragment = new TBDynamicFragment();
        fragment.setOnCommentClickListener(l);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        args.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, userInfoBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUserinfo = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void followViewClick(DynamicDetailBeanV2 data, TextView followView) {
        if (mPresenter.handleTouristControl()) {
            return;
        }
        if (!data.getUserInfoBean().getFollower()) {
            // 关注
            mPresenter.followUser(data.getUserInfoBean());
            data.getUserInfoBean().setFollower(true);
        } else {
            // 更多
            initOtherDynamicPopupWindow(data, followView);
            mOtherDynamicPopWindow.show();

        }

    }

    /**
     * view 调整为 followView
     *
     * @param contentView
     * @param dataPosition
     * @param viewPosition
     */
    @Override
    public void onMenuItemClick(View contentView, int dataPosition, int viewPosition) {
        dataPosition -= mHeaderAndFooterWrapper.getHeadersCount();
        switch (viewPosition) {
            // 0 1 2 3 代表 view item 位置
            case 0:
                if (mPresenter.handleTouristControl()) {
                    return;
                }
                // 分享
                Intent intent = new Intent(mActivity, DynamicShareActivity.class);
                Bundle bundle = new Bundle();
                String content = mListDatas.get(dataPosition).getFeed_content();
                if (!TextUtils.isEmpty(content)) {
                    content = content.replaceAll(MarkdownConfig.NETSITE_FORMAT, "");
                }
                DynamicShareBean dynamicShareBean = new DynamicShareBean(mListDatas.get(dataPosition).getUserInfoBean(),
                        TimeUtils.utc2LocalStr(mListDatas.get(dataPosition).getCreated_at()),
                        content,
                        String.valueOf(mListDatas.get(dataPosition).getId()), UserInfoRepository.SHARETYPEENUM.FEED.value);
                bundle.putSerializable(BUNDLE_SHARE_DATA, dynamicShareBean);
                intent.putExtras(bundle);
                startActivity(intent);
                mCurrentClickItemPosition = dataPosition;
                break;
            case 1:
                // 评论
                if (CAN_COMMENT == mListDatas.get(dataPosition).getCan_comment()) {
                    // 还未发送成功的动态列表不查看详情
                    if (mListDatas.get(dataPosition).getId() == null || mListDatas.get(dataPosition).getId() == 0) {
                        return;
                    }
                    Intent commentListIntent = new Intent(getActivity(), DynamicCommentListActivity.class);
                    Bundle commentListBundle = new Bundle();
                    commentListBundle.putParcelable(DynamicCommentListFragment.DYNAMIC_DETAIL_DATA, mListDatas.get(dataPosition));
                    commentListBundle.putString(DynamicCommentListFragment.DYNAMIC_DETAIL_DATA_TYPE, getDynamicType());
                    commentListIntent.putExtras(commentListBundle);
                    startActivity(commentListIntent);
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.keep_on);

                } else {
                    showSnackWarningMessage(getString(R.string.dynamic_not_support_comment));
                }
                break;

            case 2:
                // 喜欢
                if ((!TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition, contentView);
                break;


            default:

        }
    }

    /**
     * 喜欢
     *
     * @param dataPosition
     */
    protected void handleLike(int dataPosition, View contentView) {

        // 先更新界面，再后台处理
        mListDatas.get(dataPosition).setHas_digg(!mListDatas.get(dataPosition)
                .isHas_digg());
        mListDatas.get(dataPosition).setFeed_digg_count(mListDatas.get(dataPosition)
                .isHas_digg() ?
                mListDatas.get(dataPosition).getFeed_digg_count() + 1 : mListDatas.get
                (dataPosition).getFeed_digg_count() - 1);
        DynamicListMenuView dynamicListMenuView = (DynamicListMenuView) contentView.findViewById(R.id.dlmv_menu);
        dynamicListMenuView.setItemTextAndStatus(ConvertUtils.numberConvert(mListDatas.get(dataPosition)
                .getFeed_digg_count()), mListDatas.get(dataPosition).isHas_digg(), 2);
        mPresenter.handleLike(mListDatas.get(dataPosition).isHas_digg(),
                mListDatas.get(dataPosition).getId(), dataPosition);
    }


    /**
     * 初始化他人动态操作选择弹框
     *
     * @param dynamicBean curent dynamic
     */
    protected void initOtherDynamicPopupWindow(final DynamicDetailBeanV2 dynamicBean, View followView) {
        mOtherDynamicPopWindow = ActionPopupWindow.builder()
                .item1Str(dynamicBean.getUserInfoBean().getFollower() ? getString(R.string.cancel_follow) : "")
                .item2Str(dynamicBean.getFeed_from() == -1 ? "" : getString(R.string.report))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 取消关注
                    mPresenter.followUser(dynamicBean.getUserInfoBean());
                    dynamicBean.getUserInfoBean().setFollower(false);
                    followView.setVisibility(View.VISIBLE);
                    ((TextView) followView).setCompoundDrawables(null, null, null,
                            null);
                    ((TextView) followView).setText(getString(R.string.add_follow));
                    followView.setBackgroundResource(R.drawable.shape_radus_box_themecolor);
//                    refreshData();
                    mOtherDynamicPopWindow.hide();
                })
                .item2ClickListener(() -> {                    // 举报帖子
                    if (mPresenter.handleTouristControl()) {
                        return;
                    }

                    String img = "";
                    if (dynamicBean.getImages() != null && !dynamicBean.getImages().isEmpty()) {
                        img = ImageUtils.imagePathConvertV2(dynamicBean.getImages().get(0).getFile(), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img), getResources()
                                        .getDimensionPixelOffset(R.dimen.report_resource_img),
                                100);
                    }
                    ReportResourceBean reportResourceBean = new ReportResourceBean(dynamicBean.getUserInfoBean()
                            , String.valueOf(dynamicBean.getId())
                            , "", img,
                            dynamicBean.getFeed_content()
                            , ReportType.DYNAMIC);
                    reportResourceBean.setDesCanlook(dynamicBean.getPaid_node() == null || dynamicBean
                            .getPaid_node().isPaid());
                    ReportActivity.startReportActivity(mActivity, reportResourceBean);
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .bottomClickListener(() -> {
                    mOtherDynamicPopWindow.hide();
                    showBottomView(true);
                })
                .build();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_DYNAMIC_SHARE)
    private void updateDynamicShare(DynamicShareBean data) {
        if (mCurrentClickItemPosition > -1) {
            mListDatas.get(mCurrentClickItemPosition).setShare_count(mListDatas.get(mCurrentClickItemPosition).getShare_count() + 1);
            refreshData();
        }
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.def_hone_network_prompt;
    }
}
