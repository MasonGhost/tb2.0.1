package com.zhiyicx.thinksnsplus.modules.tb.dynamic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.BuildConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.report.ReportResourceBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForAdvert;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForZeroImage;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.report.ReportActivity;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment.DynamicCommentListActivity;
import com.zhiyicx.thinksnsplus.modules.tb.dynamic.comment.DynamicCommentListFragment;
import com.zhiyicx.thinksnsplus.modules.tb.mechainism.MechanismCenterFragment;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareActivity;
import com.zhiyicx.thinksnsplus.modules.tb.share.DynamicShareBean;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.Subscriber;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.CAN_COMMENT;

/**
 * @author Jungle68
 * @describe
 * @date 2018/2/28
 * @contact master.jungle68@gmail.com
 */
public class TBMainDynamicFragment extends TBDynamicFragment {

    private int mCurrentPostion = -1;

    public static TBMainDynamicFragment newInstance(String dynamicType) {
        TBMainDynamicFragment fragment = new TBMainDynamicFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    public static TBMainDynamicFragment newInstance(String dynamicType, UserInfoBean userInfoBean) {
        TBMainDynamicFragment fragment = new TBMainDynamicFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        args.putParcelable(PersonalCenterFragment.PERSONAL_CENTER_DATA, userInfoBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        TBMainDynamicListItemForZeroImage dynamicListItemForZeroImage = new TBMainDynamicListItemForZeroImage(getContext());
        /*
         关注
          */
        dynamicListItemForZeroImage.setOnFollowlistener((data, followView) -> {
            followClick(data, followView);
        });
        setAdapter(adapter, dynamicListItemForZeroImage);
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        setAdapter(adapter, new DynamicListItemForAdvert(getContext()));
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    private void followClick(DynamicDetailBeanV2 data, TextView followView) {
        if (mPresenter.handleTouristControl()) {
            return;
        }
        if (!data.getUserInfoBean().getFollower()) {
            // 关注
            mPresenter.followUser(data.getUserInfoBean());
            data.getUserInfoBean().setFollower(true);
            refreshData();
        } else {
            // 更多
            initOtherDynamicPopupWindow(data, followView);
            mOtherDynamicPopWindow.show();

        }
        if (getParentFragment() != null && getParentFragment() instanceof MechanismCenterFragment.OnMerchainismInfoChangedListener) {
            ((MechanismCenterFragment.OnMerchainismInfoChangedListener) getParentFragment()).handleFollow();
        }

    }

    @Override
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
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
                mCurrentPostion = dataPosition;
                break;
            case 1:
                // 评论
                if ((!TouristConfig.DYNAMIC_CAN_COMMENT && mPresenter.handleTouristControl()) ||
                        mListDatas.get(dataPosition).getId() == null || mListDatas.get
                        (dataPosition).getId() == 0) {
                    return;
                }
                mCurrentPostion = dataPosition;
                if (mOnItemCommentClickListener != null) {
                    mOnItemCommentClickListener.onItemCommentClick(dataPosition, mListDatas.get(dataPosition), getDynamicType());
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
        if(mListDatas.get(dataPosition).isHas_digg()){
            return;
        } else {
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
        if (mCurrentPostion > -1) {
            mListDatas.get(mCurrentPostion).setShare_count(mListDatas.get(mCurrentPostion).getShare_count() + 1);
            refreshData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePop(mOtherDynamicPopWindow);
        releasePop(mMyDynamicPopWindow);
    }

    public void setOnItemCommentClickListener(OnItemCommentClickListener onItemCommentClickListener) {
        mOnItemCommentClickListener = onItemCommentClickListener;
    }

    public interface OnItemCommentClickListener {
        void onItemCommentClick(int pos, DynamicDetailBeanV2 detailBeanV2, String dynamicType);
    }

    public void updateFollower(boolean follower){
        for(DynamicDetailBeanV2 dynamicDetailBeanV2 : mListDatas){
            dynamicDetailBeanV2.getUserInfoBean().setFollower(follower);
        }
        refreshData();
    }

    private OnItemCommentClickListener mOnItemCommentClickListener;

    @Override
    protected int setEmptView() {return R.mipmap.def_news_flash_prompt;
    }
}
