package com.zhiyicx.thinksnsplus.modules.register;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterFragment extends TSFragment<RegisterPresenter> implements RegisterContract.View {


    @BindView(R.id.et_regist_nickname)
    EditText mEtRegistNickname;
    @BindView(R.id.et_regist_phone)
    EditText mEtRegistPhone;
    @BindView(R.id.bt_regist_send_vertify_code)
    Button mBtRegistSendVertifyCode;
    @BindView(R.id.pb_regist_loading)
    ImageView mPbRegistLoading;
    @BindView(R.id.et_regist_vertify_code)
    EditText mEtRegistVertifyCode;
    @BindView(R.id.et_regist_password)
    EditText mEtRegistPassword;
    @BindView(R.id.cb_login)
    CheckBox mCbLogin;
    @BindView(R.id.bt_regist_regist)
    Button mBtRegistRegist;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    protected void ComponentInject() {

    }

    @Override
    protected void initView(View rootView) {
        mEtRegistPhone.setText("15694005009");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_register;
    }


    @Override
    public void setPresenter(RegisterContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void killMyself() {

    }

    @OnClick({R.id.bt_regist_send_vertify_code, R.id.bt_regist_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_regist_send_vertify_code:
                mPresenter.getVertifyCode("15694005009");
                Toast.makeText(getActivity(),"vertify_code",Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_regist_regist:
                mPresenter.register();
                Toast.makeText(getActivity(),"bt_regist_regist",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
