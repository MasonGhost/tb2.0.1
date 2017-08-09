package com.zhiyicx.thinksnsplus.modules.information.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.xrichtext.RichTextEditor;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.information.publish.addinfo.AddInfoFragment.BUNDLE_PUBLISH_BEAN;

/**
 * @Author Jliuer
 * @Date 2017/08/07/9:43
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishInfoFragment extends TSFragment<PublishInfoContract.Presenter>
        implements PublishInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    @BindView(R.id.et_info_title)
    UserInfoInroduceInputView mEtInfoTitle;
    @BindView(R.id.riche_test)
    RichTextEditor mRicheTest;
    @BindView(R.id.im_arrowc)
    ImageView mImArrowc;
    @BindView(R.id.im_pic)
    ImageView mImPic;
    @BindView(R.id.im_setting)
    ImageView mImSetting;
    @BindView(R.id.rl_publish_tool)
    RelativeLayout mRlPublishTool;
    @BindView(R.id.pb_image_upload)
    LinearLayout mPbImageUpload;

    private PhotoSelectorImpl mPhotoSelector;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框
    private int[] mImageIdArray;// 图片id
    private int mPicTag;
    private int mPicAddTag;

    public static PublishInfoFragment getInstance(Bundle bundle) {
        PublishInfoFragment publishInfoFragment = new PublishInfoFragment();
        publishInfoFragment.setArguments(bundle);
        return publishInfoFragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.edit_info);
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        InfoPublishBean infoPublishBean = new InfoPublishBean();

        StringBuilder builder = new StringBuilder();
        List<RichTextEditor.EditData> datas = mRicheTest.buildEditData();
        for (RichTextEditor.EditData editData : datas) {
            builder.append(editData.inputStr);
            if (!editData.imagePath.isEmpty()) {
                builder.append(String.format(Locale.getDefault(),
                        MarkdownConfig.IMAGE_TAG, MarkdownConfig.IMAGE_TITLE, mImageIdArray[0]));
                mPicAddTag++;
            }
        }
        String content = builder.toString();
        infoPublishBean.setContent(content);
        infoPublishBean.setAmout(10);
        infoPublishBean.setCover(mImageIdArray[0]);
        infoPublishBean.setImage(mImageIdArray[0]);
        infoPublishBean.setTitle(mEtInfoTitle.getInputContent());
        Intent intent = new Intent(getActivity(), AddInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_PUBLISH_BEAN, infoPublishBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void initView(View rootView) {
        mToolbarLeft.setTextColor(SkinUtils.getColor(R.color.themeColor));
        mToolbarRight.setTextColor(SkinUtils.getColor(R.color.themeColor));
        initLisenter();
    }

    @Override
    protected void initData() {
        mImageIdArray = new int[100];
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_info;
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
        mPbImageUpload.setVisibility(View.VISIBLE);
        mImageIdArray[mPicTag] = mPicTag;
        String path = photoList.get(0).getImgUrl();
        mPresenter.uploadPic(path, "", true, 0, 0);
        mRicheTest.insertImage(path, mRicheTest.getWidth());

    }

    @Override
    public void uploadPicSuccess(int id) {
        mPbImageUpload.setVisibility(View.GONE);
        mImageIdArray[mPicTag] = id;
        mPicTag++;
    }

    @Override
    public void uploadPicFailed() {
        mPicTag--;
        mPbImageUpload.setVisibility(View.GONE);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void publishInfoFailed() {

    }

    @Override
    public void publishInfoSuccess() {

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

    @OnClick({R.id.im_arrowc, R.id.im_pic, R.id.im_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.im_arrowc:
                break;
            case R.id.im_pic:
                initPhotoPopupWindow();
                break;
            case R.id.im_setting:
                break;
        }
    }

    private void initLisenter() {
        RxView.globalLayouts(mRlPublishTool).subscribe(aVoid -> {
            int[] viewLacotion = new int[2];
            mRlPublishTool.getLocationOnScreen(viewLacotion);
            if (viewLacotion[1] > mRlPublishTool.getHeight()) {
                View rootview = getActivity().getWindow().getDecorView();
                View currentEdit = rootview.findFocus();
                if (currentEdit != null) {
                    int[] currentEditLacotion = new int[2];
                    currentEdit.getLocationOnScreen(currentEditLacotion);
                    int dy = currentEditLacotion[1] - viewLacotion[1] + currentEdit.getHeight();
                    mRicheTest.smoothScrollBy(0, dy);
                }
            }
        });
    }
}
