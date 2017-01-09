package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/9
 * @contact email:450127106@qq.com
 */
public class UserInfoFragment extends TSFragment<UserInfoContract.Presenter> implements UserInfoContract.View {

    @BindView(R.id.iv_head_icon)
    ImageView mIvHeadIcon;
    @BindView(R.id.rl_change_head_container)
    RelativeLayout mRlChangeHeadContainer;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.ll_sex_container)
    LinearLayout mLlSexContainer;
    @BindView(R.id.tv_city)
    TextView mTvCity;
    @BindView(R.id.ll_city_container)
    LinearLayout mLlCityContainer;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void setPresenter(UserInfoContract.Presenter presenter) {
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

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.user_info);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.complete);
    }

    @OnClick({R.id.rl_change_head_container, R.id.ll_sex_container, R.id.ll_city_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_change_head_container:
                break;
            case R.id.ll_sex_container:
                break;
            case R.id.ll_city_container:
                break;
        }
    }
}
