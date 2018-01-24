package com.zhiyicx.thinksnsplus.modules.information.publish.uploadcover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.information.my_info.ManuscriptsActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;
import com.zhiyicx.thinksnsplus.modules.information.publish.detail.EditeInfoDetailFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class UploadCoverFragment extends TSFragment<PublishInfoContract.Presenter>
        implements PublishInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    @BindView(R.id.bt_sure)
    TextView mBtSure;
    @BindView(R.id.fl_info_cover_container)
    FrameLayout mFlInfoCoverContainer;
    @BindView(R.id.iv_info_cover_iamge)
    ImageView mIvInfoCoverIamge;
    @BindView(R.id.tv_info_cover)
    TextView mTvInfoCover;

    private PayPopWindow mPayInfoPopWindow;
    private PhotoSelectorImpl mPhotoSelector;
    /**
     * 图片选择弹框
     */
    private ActionPopupWindow mPhotoPopupWindow;
    private ActionPopupWindow mCoverInstructionsPopupWindow;

    public static UploadCoverFragment newInstance(Bundle bundle) {
        UploadCoverFragment fragment = new UploadCoverFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.reset_cover);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mIvInfoCoverIamge.setVisibility(View.GONE);
        mTvInfoCover.setVisibility(View.VISIBLE);
        int imageId = RegexUtils.getImageId(EditeInfoDetailFragment.mInfoPublishBean.getContent());
        if (EditeInfoDetailFragment.mInfoPublishBean.isRefuse()) {
            EditeInfoDetailFragment.mInfoPublishBean.setImage((long) imageId < 0 ? null : (long) imageId);
        } else {
            EditeInfoDetailFragment.mInfoPublishBean.setImage(EditeInfoDetailFragment.mInfoPublishBean.getCover() < 0 ? null : (long) EditeInfoDetailFragment.mInfoPublishBean.getCover());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList.isEmpty()) {
            return;
        }
        mBtSure.setEnabled(false);
        String path = photoList.get(0).getImgUrl();
        mPresenter.uploadPic(path, "", true, 0, 0);
        mTvInfoCover.setVisibility(View.GONE);
        mIvInfoCoverIamge.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(path)
                .centerCrop()
                .into(mIvInfoCoverIamge);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void uploadPicSuccess(int id) {
        if (showUplaoding()) {
            showSnackSuccessMessage(getString(R.string.cover_upload_success));
        }
        EditeInfoDetailFragment.mInfoPublishBean.setImage((long) id);
        mBtSure.setEnabled(true);
    }

    @Override
    public void uploadPicFailed() {
        mBtSure.setEnabled(true);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_upload_cover;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_cover);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();

    }

    @Override
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
        if (EditeInfoDetailFragment.mInfoPublishBean.isRefuse() && EditeInfoDetailFragment.mInfoPublishBean.getImage() != null) {
            int w = getResources().getDimensionPixelSize(R.dimen.upload_info_cover_width);
            int h = getResources().getDimensionPixelSize(R.dimen.upload_info_cover_height);
            mTvInfoCover.setVisibility(View.GONE);
            mIvInfoCoverIamge.setVisibility(View.VISIBLE);

            Glide.with(getActivity())
                    .load(ImageUtils.imagePathConvertV2(EditeInfoDetailFragment.mInfoPublishBean.getImage().intValue(), w, h, ImageZipConfig.IMAGE_70_ZIP))
                    .centerCrop()
                    .into(mIvInfoCoverIamge);
        }
        mBtSure.setText(getString(mPresenter.getSystemConfigBean().getNewsContribute().hasPay()
                && !EditeInfoDetailFragment.mInfoPublishBean.isRefuse() ? R.string.publish_withpay_info : R.string.publish_info));

    }

    @Override
    public boolean showUplaoding() {
        return true;
    }

    private void initListener() {
        //两秒钟之内只取一个点击事件，防抖操作
        RxView.clicks(mFlInfoCoverContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> initPhotoPopupWindow());
        //两秒钟之内只取一个点击事件，防抖操作
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 不要封面也可以发布了
//                    if (EditeInfoDetailFragment.mInfoPublishBean.getImage() <= 0 && EditeInfoDetailFragment.mInfoPublishBean.getCover() <= 0) {
//                        initWithdrawalsInstructionsPop();
//                        return;
//                    }
                    if (EditeInfoDetailFragment.mInfoPublishBean.isRefuse()) {
                        mPresenter.publishInfo(EditeInfoDetailFragment.mInfoPublishBean);
                        return;
                    }
                    initPayInfoPopWindow();
                });

    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        if (prompt == Prompt.DONE) {
            Intent intent = new Intent();
            if (EditeInfoDetailFragment.mInfoPublishBean.isRefuse()) {
                intent.setClass(getActivity(), ManuscriptsActivity.class);
            } else {
                intent.setClass(getActivity(), InfoActivity.class);
            }
            EditeInfoDetailFragment.mInfoPublishBean = null;
            startActivity(intent);
            mActivity.finish();
        }
    }

    private void initPayInfoPopWindow() {
        if (!mPresenter.getSystemConfigBean().getNewsContribute().hasPay()) {
            mPresenter.publishInfo(EditeInfoDetailFragment.mInfoPublishBean);
            return;
        }
        if (mPayInfoPopWindow != null) {
            mPayInfoPopWindow.show();
            return;
        }
        mPayInfoPopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(R.string.publish_pay_info) + getString(R
                        .string.buy_pay_member), PayConfig.realCurrency2GameCurrency(EditeInfoDetailFragment.mInfoPublishBean
                        .getAmout(), mPresenter.getRatio()), mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.send_info_pay))
                .buildItem1Str(getString(R.string.publish_info_pay_in))
                .buildItem2Str(getString(R.string.publish_info_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_money), PayConfig
                        .realCurrency2GameCurrency(EditeInfoDetailFragment.mInfoPublishBean.getAmout(), mPresenter.getRatio())))
                .buildCenterPopWindowItem1ClickListener(() -> {
///                    EditeInfoDetailFragment.mInfoPublishBean.setContent(EditeInfoDetailFragment.mInfoPublishBean.getSubject() + EditeInfoDetailFragment.mInfoPublishBean.getContent());
                    mPresenter.publishInfo(EditeInfoDetailFragment.mInfoPublishBean);
                    mPayInfoPopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayInfoPopWindow.hide())
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
        mPayInfoPopWindow.show();

    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            mPhotoPopupWindow.show();
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
        mPhotoPopupWindow.show();
    }

    public void initWithdrawalsInstructionsPop() {
        if (mCoverInstructionsPopupWindow != null) {
            mCoverInstructionsPopupWindow.show();
            return;
        }
        mCoverInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.instructions))
                .desStr(getString(R.string.upload_info_cover_instructions))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mCoverInstructionsPopupWindow.hide())
                .build();
        mCoverInstructionsPopupWindow.show();
    }
}
