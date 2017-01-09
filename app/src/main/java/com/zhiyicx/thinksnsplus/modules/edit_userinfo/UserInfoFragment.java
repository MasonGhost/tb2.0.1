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

import com.bigkoo.pickerview.OptionsPickerView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AreaBean;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<AreaBean> options1Items;
    private ArrayList<ArrayList<AreaBean>> options2Items;
    private int mCityOption1;//用来记录地区中滚轮的位置
    private int mCityOption2;
    private OptionsPickerView mAreaPickerView;// 地域选择器

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initView(View rootView) {
        initCityPickerView();
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
                mAreaPickerView.setSelectOptions(mCityOption1, mCityOption2);
                mAreaPickerView.show();
                break;
        }
    }

    @Override
    public void setAreaData(ArrayList<AreaBean> options1Items, ArrayList<ArrayList<AreaBean>> options2Items) {
        this.options1Items = options1Items;
        this.options2Items = options2Items;
        mAreaPickerView.setPicker(options1Items, options2Items, true);
        mAreaPickerView.setCyclic(false);// 设置是否可以循环滚动
    }

    /**
     * 初始化城市选择器
     */
    private void initCityPickerView() {
        if (mAreaPickerView != null) {
            return;
        }
        mAreaPickerView = new OptionsPickerView(getActivity());
        mAreaPickerView.setCancelable(true);// 触摸是否自动消失
        mAreaPickerView.setTitle("请选择城市");
        mAreaPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (options2Items.size() <= options1 || options2Items.get(options1).size() <= option2) {
                    return;//避免pickview控件的bug
                }
                //EventBus.getDefault().post(options2Items.get(options1).get(option2), "update_location");
                String areaText = options1Items.get(options1).getPickerViewText();
                String city = options2Items.get(options1).get(option2).getPickerViewText();
                city = city.equals("全部") ? areaText : city;//如果为全部则不显示
                mTvCity.setText(city);//更新位置
                mCityOption1 = options1;
                mCityOption2 = option2;
            }
        });
        mPresenter.getAreaData();
    }
}
