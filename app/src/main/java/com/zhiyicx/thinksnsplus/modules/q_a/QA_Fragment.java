package com.zhiyicx.thinksnsplus.modules.q_a;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_container.QA_InfoContainerFragment;

import butterknife.BindView;

/**
 * @Author Jliuer
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

    private QA_InfoContainerFragment mQA_ListInfoFragment, mQA_TopicInfoFragment;

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
        initListener();
    }

    @Override
    protected void initData() {

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
                    break;
                case R.id.rb_topic:
                    if (mQA_TopicInfoFragment == null) {
                        mQA_TopicInfoFragment = QA_InfoContainerFragment.getInstance();
                        fragmentTransaction.add(R.id.qa_fragment_container, mQA_TopicInfoFragment);
                    } else {
                        fragmentTransaction.show(mQA_TopicInfoFragment);
                    }
                    break;
                default:
                    break;
            }
            fragmentTransaction.commit();
        });
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mQA_ListInfoFragment != null) {
            fragmentTransaction.hide(mQA_ListInfoFragment);
        }
        if (mQA_TopicInfoFragment != null) {
            fragmentTransaction.hide(mQA_TopicInfoFragment);
        }
    }

}
