package com.zhiyicx.thinksnsplus.modules.password;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
public class PasswordFragment extends TSFragment<PasswordContract.Presenter> implements PasswordContract.View {


    @BindView(R.id.et_old_password)
    PasswordEditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    PasswordEditText mEtNewPassword;
    @BindView(R.id.et_sure_new_password)
    PasswordEditText mEtSureNewPassword;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;

    public static PasswordFragment newInstance() {
        PasswordFragment fragment = new PasswordFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.immediate_regist);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void setPresenter(PasswordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
            System.out.println(" =INVISIBLE ");
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
            System.out.println(" =VISIBLE " + message);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
