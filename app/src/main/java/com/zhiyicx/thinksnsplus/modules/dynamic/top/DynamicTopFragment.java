package com.zhiyicx.thinksnsplus.modules.dynamic.top;

import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

/**
 * @Author Jliuer
 * @Date 2017/05/22/11:13
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicTopFragment extends TSFragment<DynamicTopContract.Presenter> implements DynamicTopContract.View{

    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.et_top_input)
    EditText mEtTopInput;
    @BindView(R.id.et_top_total)
    EditText mEtTopTotal;
    @BindView(R.id.bt_top)
    TextView mBtTop;
    @BindView(R.id.tv_dynamic_top_dec)
    TextView mTvDynamicTopDec;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;

    private List<Integer> mSelectDays;
    private int mCurrentDays;
    private float mInputMoney;

    public static DynamicTopFragment newInstance(){
        return new DynamicTopFragment();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.to_top);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1);
        mSelectDays.add(5);
        mSelectDays.add(10);
        initSelectDays(mSelectDays);
        mTvDynamicTopDec.setText(String.format(getString(R.string.to_top_description), 200f, 33f));
        mRbDaysGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_one:
                        mCurrentDays = mSelectDays.get(0);
                        break;
                    case R.id.rb_two:
                        mCurrentDays = mSelectDays.get(1);
                        break;
                    case R.id.rb_three:
                        mCurrentDays = mSelectDays.get(2);
                        break;
                }
                setConfirmEnable();
            }
        });

        RxTextView.textChanges(mEtTopInput)
                .compose(this.<CharSequence>bindToLifecycle())
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (!TextUtils.isEmpty(charSequence)) {
                            if (charSequence.toString().contains(".")) {
                                mEtTopInput.setError("只能充值整数");
                            }
                            mInputMoney = Float.parseFloat(charSequence.toString());
                        } else {
                            mInputMoney = 0f;
                        }
                        setConfirmEnable();
                    }
                });
    }

    private void initSelectDays(List<Integer> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.select_day), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.select_day), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.select_day), mSelectDays.get(2)));
    }

    private void setConfirmEnable() {
        mEtTopTotal.setText(String.valueOf(mCurrentDays * mInputMoney));
        mBtTop.setEnabled(mCurrentDays > 0 && mInputMoney > 0);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_top;
    }

    @OnClick(R.id.bt_top)
    public void onViewClicked() {
    }
}
