package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupNameFragment extends TSFragment<EditGroupNameContract.Presenter> implements EditGroupNameContract.View{

    public EditGroupNameFragment instance(Bundle bundle){
        EditGroupNameFragment fragment = new EditGroupNameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return 0;
    }
}
