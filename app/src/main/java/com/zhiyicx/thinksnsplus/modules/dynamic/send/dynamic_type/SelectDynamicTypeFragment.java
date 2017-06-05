package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/05/25/14:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SelectDynamicTypeFragment extends TSFragment {

    @BindView(R.id.send_words_dynamic)
    IconTextView mSendWordsDynamic;
    @BindView(R.id.send_image_dynamic)
    IconTextView mSendImageDynamic;
    @BindView(R.id.im_close_dynamic)
    ImageView mImCloseDynamic;
    @BindView(R.id.select_dynamic_parent)
    LinearLayout mSelectDynamicParent;

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        getActivity().getWindow().getDecorView().setBackgroundColor(getColor(R.color.tym));
        initAnimation(mSendWordsDynamic);
        initAnimation(mSendImageDynamic);
    }

    @Override
    protected void initData() {

    }

    private void initAnimation(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
                int vertical_distance = mSelectDynamicParent.getHeight() - view.getTop();
                AnimatorSet mAnimatorSet = new AnimatorSet();
                ViewCompat.setPivotX(view, view.getWidth() / 2.0f);
                ViewCompat.setPivotY(view, view.getHeight() / 2.0f);
                mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimatorSet.setDuration(1200);

                ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "translationY", vertical_distance, 0);

                AnimatorSet mAnimatorSetLate = mAnimatorSet.clone();
                mAnimatorSetLate.playTogether(
                        ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f, 0.9f, 1),
                        ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f, 0.9f, 1));
                mAnimatorSet.play(alpha).with(scaleX).with(scaleY).with(translationY).before(mAnimatorSetLate);
                mAnimatorSet.start();

            }
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
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendWordsDynamicDataBean);
                break;
            case R.id.send_image_dynamic:
                SendDynamicDataBean sendImageDynamicDataBean = new SendDynamicDataBean();
                sendImageDynamicDataBean.setDynamicBelong(SendDynamicDataBean.MORMAL_DYNAMIC);
                sendImageDynamicDataBean.setDynamicType(SendDynamicDataBean.PHOTO_TEXT_DYNAMIC);
                SendDynamicActivity.startToSendDynamicActivity(getContext(), sendImageDynamicDataBean);
                break;
            case R.id.im_close_dynamic:

                break;
        }
        getActivity().finish();
        getActivity().overridePendingTransition(0, R.anim.zoom_out);
//        getActivity().overridePendingTransition(0, R.anim.slide_out_bottom);
    }

}
