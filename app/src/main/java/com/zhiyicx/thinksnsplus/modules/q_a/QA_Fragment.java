package com.zhiyicx.thinksnsplus.modules.q_a;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QATopicFragmentContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QA_InfoContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.search.container.QASearchContainerActivity;
import com.zhiyicx.thinksnsplus.widget.coordinatorlayout.ScrollAwareFABBehavior;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/07/25/11:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_Fragment extends TSFragment {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.rb_qa)
    RadioButton mRbQa;
    @BindView(R.id.rb_topic)
    RadioButton mRbTopic;
    @BindView(R.id.rg_qa_type)
    RadioGroup mRgQaType;
    @BindView(R.id.iv_serach)
    ImageView mIvSerach;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    @BindView(R.id.qa_fragment_container)
    FrameLayout mQaFragmentContainer;
    @BindView(R.id.ll_toolbar_container_parent)
    LinearLayout mLlToolbarContainerParent;
    @BindView(R.id.btn_send_dynamic)
    ImageView mBtnSendDynamic;

    private boolean mIsAnimatingOut = false;
    private static final android.view.animation.Interpolator INTERPOLATOR =
            new FastOutSlowInInterpolator();

    private QA_InfoContainerFragment mQA_ListInfoFragment;
    private QATopicFragmentContainerFragment mQA_TopicInfoFragment;

    @Inject
    AuthRepository mAuthRepository;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        initListener();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mIvSerach;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_qa;
    }

    private void initListener() {
        mRbQa.setChecked(true);
        RxRadioGroup.checkedChanges(mRgQaType).subscribe(integer -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            hideFragment(fragmentTransaction);
            switch (integer) {
                case R.id.rb_qa:
                    if (mQA_ListInfoFragment == null) {
                        mQA_ListInfoFragment = QA_InfoContainerFragment.getInstance();
                        fragmentTransaction.add(R.id.qa_fragment_container, mQA_ListInfoFragment);
                    } else {
                        fragmentTransaction.show(mQA_ListInfoFragment);
                    }
                    mBtnSendDynamic.setVisibility(View.VISIBLE);
                    break;
                case R.id.rb_topic:
                    if (mQA_TopicInfoFragment == null) {
                        mQA_TopicInfoFragment = QATopicFragmentContainerFragment.getInstance();
                        fragmentTransaction.add(R.id.qa_fragment_container, mQA_TopicInfoFragment);
                    } else {
                        fragmentTransaction.show(mQA_TopicInfoFragment);
                    }
                    mBtnSendDynamic.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            fragmentTransaction.commit();
        });

        RxView.clicks(mBtnSendDynamic)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 登录
                    if (mAuthRepository.isTourist()) {
                        showLoginPop();
                    } else {
                        startActivity(new Intent(getActivity(), PublishQuestionActivity.class));
                    }
                });

        RxView.clicks(mIvBack)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> getActivity().finish());

        RxView.clicks(mIvSerach)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                            // 登录
                            if (mAuthRepository.isTourist()) {
                                showLoginPop();
                            } else {
                                startActivity(new Intent(getActivity(), QASearchContainerActivity.class));
                            }
                        }
                );
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mQA_ListInfoFragment != null) {
            fragmentTransaction.hide(mQA_ListInfoFragment);
        }
        if (mQA_TopicInfoFragment != null) {
            fragmentTransaction.hide(mQA_TopicInfoFragment);
        }
    }

    // 退出动画
    public void animateOut() {
        ViewCompat.animate(mBtnSendDynamic).translationY(100.0f).alpha(0.0f).scaleX(0.0f).scaleY(0.0f)
                .setInterpolator(INTERPOLATOR).withLayer()
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        mIsAnimatingOut = true;
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        mIsAnimatingOut = false;
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        mIsAnimatingOut = false;
                    }
                }).start();
    }

    // 进入动画
    public void animateIn() {
        // 这里的translationY表示的是移动到起始点
        ViewCompat.animate(mBtnSendDynamic).translationY(0.0F).alpha(1.0F).scaleX(1.0F).scaleY(1.0F)
                .setInterpolator(INTERPOLATOR).withLayer().setListener(null)
                .start();

    }

}
