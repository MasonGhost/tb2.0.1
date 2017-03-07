package com.zhiyicx.thinksnsplus.modules.personal_center;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/7
 * @contact email:450127106@qq.com
 */

public class PersonalCenterFragment extends TSListFragment<PersonalCenterContract.Presenter, DynamicBean> implements PersonalCenterContract.View {
    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        return null;
    }

    @Override
    public void setPresenter(PersonalCenterContract.Presenter presenter) {

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

    public static PersonalCenterFragment initFragment(Bundle bundle) {
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        personalCenterFragment.setArguments(bundle);
        return personalCenterFragment;
    }
}
