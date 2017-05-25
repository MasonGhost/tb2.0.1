package com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/05/25/16:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PictureTollFragment extends TSFragment {
    @BindView(R.id.tv_choose_tip_toll_ways)
    TextView mTvChooseTipTollWays;
    @BindView(R.id.rb_ways_one)
    RadioButton mRbWaysOne;
    @BindView(R.id.rb_ways_two)
    RadioButton mRbWaysTwo;
    @BindView(R.id.rb_days_group_toll_ways)
    RadioGroup mRbDaysGroupTollWays;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.v_line)
    View mVLine;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.bt_top)
    TextView mBtTop;

    private ArrayList<Integer> mSelectDays;

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected void initView(View rootView) {
        mTvChooseTip.setText(R.string.choose_recharge_money);
    }

    @Override
    protected void initData() {
        mSelectDays = new ArrayList<>();
        mSelectDays.add(1);
        mSelectDays.add(5);
        mSelectDays.add(10);
        initSelectDays(mSelectDays);
    }

    private void initSelectDays(List<Integer> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money), mSelectDays.get(2)));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_picture_toll;
    }

    @OnClick(R.id.bt_top)
    public void onViewClicked() {

    }
}
