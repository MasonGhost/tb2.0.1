package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */
public class FindPasswordFragment extends TSFragment<FindPasswordContract.Presenter> implements FindPasswordContract.View {

    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;

    public static FindPasswordFragment newInstance() {
        FindPasswordFragment fragment = new FindPasswordFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.change_password);
    }


    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }


    @Override
    public void setPresenter(FindPasswordContract.Presenter presenter) {
        this.mPresenter=presenter;
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
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }


}
