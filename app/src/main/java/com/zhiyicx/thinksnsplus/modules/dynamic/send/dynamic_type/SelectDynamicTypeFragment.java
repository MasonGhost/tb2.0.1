package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.MAX_DEFAULT_COUNT;

/**
 * @Author Jliuer
 * @Date 2017/05/25/14:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SelectDynamicTypeFragment extends TSFragment implements PhotoSelectorImpl.IPhotoBackListener {

    public static final String SEND_OPTION = "send_option";
    public static final String GROUP_ID = "group_id";

    @BindView(R.id.send_words_dynamic)
    IconTextView mSendWordsDynamic;
    @BindView(R.id.send_image_dynamic)
    IconTextView mSendImageDynamic;
    @BindView(R.id.im_close_dynamic)
    ImageView mImCloseDynamic;
    @BindView(R.id.select_dynamic_parent)
    LinearLayout mSelectDynamicParent;
    private PhotoSelectorImpl mPhotoSelector;

    public static SelectDynamicTypeFragment getInstance(Bundle b) {
        SelectDynamicTypeFragment selectDynamicTypeFragment = new SelectDynamicTypeFragment();
        selectDynamicTypeFragment.setArguments(b);
        return selectDynamicTypeFragment;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        getActivity().getWindow().getDecorView().setBackgroundColor(getColor(R.color.tym));
        initAnimation(mSendWordsDynamic);
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> initAnimation(mSendImageDynamic));
    }

    @Override
    protected void initData() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    private void initAnimation(final View view) {
        view.post(() -> {
            AnimatorSet mAnimatorSet = new AnimatorSet();
            int vertical_distance = mSelectDynamicParent.getBottom() - view.getTop();
            ViewCompat.setPivotX(view, view.getWidth() / 2.0f);
            ViewCompat.setPivotY(view, view.getHeight() / 2.0f);
            mAnimatorSet.setDuration(1200);
            mAnimatorSet.setInterpolator(new OvershootInterpolator(1f));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", vertical_distance, 0);
            mAnimatorSet.play(translationY);
            view.setVisibility(View.VISIBLE);
            mAnimatorSet.start();
        });

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_type;
    }

    @OnClick({R.id.send_words_dynamic, R.id.send_image_dynamic, R.id.im_close_dynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_words_dynamic:
                SendDynamicDataBean sendWordsDynamicDataBean = new SendDynamicDataBean();
                sendWordsDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
                sendWordsDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                if (getArguments() != null) {
                    sendWordsDynamicDataBean.setDynamicBelong(SendDynamicDataBean.GROUP_DYNAMIC);
                    sendWordsDynamicDataBean.setDynamicChannlId(getArguments().getLong(GROUP_ID));
                }
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendWordsDynamicDataBean);
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoom_out);
                break;
            case R.id.send_image_dynamic:
                clickSendPhotoTextDynamic();
//                SendDynamicDataBean sendImageDynamicDataBean = new SendDynamicDataBean();
//                sendImageDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
//                sendImageDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
//                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendImageDynamicDataBean);
                break;
            case R.id.im_close_dynamic:
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoom_out);
                break;
        }
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 跳转到发送动态页面
        SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
        sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
        sendDynamicDataBean.setDynamicPrePhotos(photoList);
        if (getArguments() != null) {
            sendDynamicDataBean.setDynamicBelong(SendDynamicDataBean.GROUP_DYNAMIC);
            sendDynamicDataBean.setDynamicChannlId(getArguments().getLong(GROUP_ID));
        }
        sendDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
        SendDynamicActivity.startToSendDynamicActivity(getContext(), sendDynamicDataBean);
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.zoom_out);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void clickSendPhotoTextDynamic() {
        mPhotoSelector.getPhotoListFromSelector(MAX_DEFAULT_COUNT, null);
    }
}
