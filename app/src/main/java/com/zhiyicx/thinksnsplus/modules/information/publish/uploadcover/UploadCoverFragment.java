package com.zhiyicx.thinksnsplus.modules.information.publish.uploadcover;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.modules.information.publish.PublishInfoContract;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoFragment.BUNDLE_PUBLISH_BEAN;

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
    @BindView(R.id.iv_info_cover_iamge)
    ImageView mIvInfoCoverIamge;

    private InfoPublishBean mInfoPublishBean;
    private PayPopWindow mPayInfoPopWindow;
    private PhotoSelectorImpl mPhotoSelector;

    public static UploadCoverFragment newInstance(Bundle bundle) {

        UploadCoverFragment fragment = new UploadCoverFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInfoPublishBean = getArguments().getParcelable(BUNDLE_PUBLISH_BEAN);
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
        String path = photoList.get(0).getImgUrl();
        mPresenter.uploadPic(path, "", true, 0, 0);
        Glide.with(getContext())
                .load(path)
                .crossFade()
                .centerCrop()
                .into(mIvInfoCoverIamge);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void uploadPicSuccess(int id) {
        mInfoPublishBean.setImage(id);
    }

    @Override
    public void uploadPicFailed() {

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
    }


    private void initListener() {
        RxView.clicks(mBtSure)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> initPayInfoPopWindow());

    }


    private void initPayInfoPopWindow() {
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
                        .string.buy_pay_member), PayConfig.realCurrencyFen2Yuan(mInfoPublishBean.getAmout())))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.publish_info_pay_in))
                .buildItem2Str(getString(R.string.publish_info_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_money), PayConfig.realCurrencyFen2Yuan(mInfoPublishBean.getAmout())))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPayInfoPopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> {
                    mPayInfoPopWindow.hide();
                })
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
}
