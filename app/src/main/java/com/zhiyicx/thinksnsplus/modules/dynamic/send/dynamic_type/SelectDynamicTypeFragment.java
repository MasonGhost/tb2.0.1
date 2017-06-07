package com.zhiyicx.thinksnsplus.modules.dynamic.send.dynamic_type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicActivity;
import com.zhiyicx.thinksnsplus.widget.IconTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                initAnimation(mSendImageDynamic);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void initAnimation(final View view) {
        view.post(new Runnable() {
            @Override
            public void run() {
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
