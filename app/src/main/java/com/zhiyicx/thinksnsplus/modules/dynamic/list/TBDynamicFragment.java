package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareActivity;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareBean;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class TBDynamicFragment extends DynamicFragment {
    public static final String BUNDLE_SHARE_DATA = "data";

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurrentUserinfo = getArguments().getParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA);
    }

    /**
     * view 调整为 followView
     *
     * @param followView
     * @param dataPosition
     * @param viewPosition
     */
    @Override
    public void onMenuItemClick(View followView, int dataPosition, int viewPosition) {
        dataPosition -= mHeaderAndFooterWrapper.getHeadersCount();
        switch (viewPosition) {
            // 0 1 2 3 代表 view item 位置

            case 0:
                // 喜欢
                if ((!TouristConfig.DYNAMIC_CAN_DIGG && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                handleLike(dataPosition);
                break;

            case 2:
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
                        String.valueOf(mListDatas.get(dataPosition).getId()));
                bundle.putSerializable(BUNDLE_SHARE_DATA, dynamicShareBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case 3:
                // 更多

                if (mListDatas.get(dataPosition)
                        .getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                } else if (mListDatas.get(dataPosition).getFeed_from() != -1) {
                    initOtherDynamicPopupWindow(mListDatas.get(dataPosition), followView);
                    mOtherDynamicPopWindow.show();
                } else {
                    // 广告
                }
                break;
            default:

        }
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
}
