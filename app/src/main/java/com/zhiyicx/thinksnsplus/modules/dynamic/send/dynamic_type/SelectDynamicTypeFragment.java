package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.MAX_DEFAULT_COUNT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_CHECK_IN_CLICK;

/**
 * @Author Jliuer
 * @Date 2017/05/25/14:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SelectDynamicTypeFragment extends TSFragment implements PhotoSelectorImpl.IPhotoBackListener {

    public static final String SEND_OPTION = "send_option";
    public static final String GROUP_ID = "group_id";
    public static final String TYPE = "type";

    @BindView(R.id.send_words_dynamic)
    IconTextView mSendWordsDynamic;
    @BindView(R.id.check_in)
    IconTextView mCheckIn;
    @BindView(R.id.send_image_dynamic)
    IconTextView mSendImageDynamic;
    @BindView(R.id.send_words_question)
    IconTextView mSendWordsQuestion;
    @BindView(R.id.im_close_dynamic)
    ImageView mImCloseDynamic;
    @BindView(R.id.select_dynamic_parent)
    LinearLayout mSelectDynamicParent;
    private PhotoSelectorImpl mPhotoSelector;

    private int mType = SendDynamicDataBean.NORMAL_DYNAMIC; // 动态还是圈子动态

    public static SelectDynamicTypeFragment getInstance(Bundle b) {
        SelectDynamicTypeFragment selectDynamicTypeFragment = new SelectDynamicTypeFragment();
        selectDynamicTypeFragment.setArguments(b);
        return selectDynamicTypeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(TYPE);
        }
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
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(getContext(), SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);

        if (systemConfigBean != null && systemConfigBean.isCheckin() && mType == SendDynamicDataBean.NORMAL_DYNAMIC) {
            mCheckIn.setVisibility(View.INVISIBLE);
            Observable.timer(600, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> initAnimation(mCheckIn));
        } else {
            mCheckIn.setVisibility(View.GONE);
        }

        Observable.timer(900, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> initAnimation(mSendWordsQuestion));


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

    @OnClick({R.id.send_words_dynamic, R.id.send_image_dynamic, R.id.check_in, R.id.im_close_dynamic, R.id.send_words_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_words_dynamic:
                SendDynamicDataBean sendWordsDynamicDataBean = new SendDynamicDataBean();
                sendWordsDynamicDataBean.setDynamicBelong(mType);
                sendWordsDynamicDataBean.setDynamicType(SendDynamicDataBean.TEXT_ONLY_DYNAMIC);
                if (getArguments() != null) {
                    sendWordsDynamicDataBean.setDynamicChannlId(getArguments().getLong(GROUP_ID));
                }
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendWordsDynamicDataBean);
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoom_out);
                break;
            case R.id.send_image_dynamic:
                clickSendPhotoTextDynamic();
//                SendDynamicDataBean sendImageDynamicDataBean = new SendDynamicDataBean();
//                sendImageDynamicDataBean.setDynamicBelong(SendDynamicDataBean.NORMAL_DYNAMIC);
//                sendImageDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
//                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendImageDynamicDataBean);
                break;
            case R.id.check_in:

                EventBus.getDefault().post(true, EVENT_CHECK_IN_CLICK);
                getActivity().finish();

                break;

            case R.id.im_close_dynamic:
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.zoom_out);
                break;

            case R.id.send_words_question:
                // 提问
                break;
            default:
        }
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        // 跳转到发送动态页面
        SendDynamicDataBean sendDynamicDataBean = new SendDynamicDataBean();
        sendDynamicDataBean.setDynamicBelong(mType);
        sendDynamicDataBean.setDynamicPrePhotos(photoList);
        if (getArguments() != null) {
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
