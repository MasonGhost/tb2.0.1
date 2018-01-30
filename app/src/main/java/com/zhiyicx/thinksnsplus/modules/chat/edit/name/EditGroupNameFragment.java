package com.zhiyicx.thinksnsplus.modules.chat.edit.name;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupNameFragment extends TSFragment<EditGroupNameContract.Presenter> implements EditGroupNameContract.View {

    private static final int GROUP_NAME_MIN_LENGTH = 2;
    public static final String GROUP_ORIGINAL_NAME = "group_original_name";

    @BindView(R.id.edit_input)
    AppCompatEditText mEditInput;

    private String mNewName;
    private String mOldName;

    public static EditGroupNameFragment instance(Bundle bundle) {
        EditGroupNameFragment fragment = new EditGroupNameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        mOldName = getArguments().getString(GROUP_ORIGINAL_NAME);
        mEditInput.setHint(mOldName);
        RxTextView.textChanges(mEditInput)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mNewName = charSequence.toString();
                    checkButtonClickable();
                });
    }

    /**
     * 检测完成按钮是否可用
     */
    private void checkButtonClickable() {
        if (TextUtils.isEmpty(mNewName) || mNewName.length() < GROUP_NAME_MIN_LENGTH) {
            mToolbarRight.setClickable(false);
            mToolbarRight.setTextColor(getColor(R.color.normal_for_disable_button_text));
        } else {
            mToolbarRight.setClickable(true);
            mToolbarRight.setTextColor(getColor(R.color.themeColor));
        }
    }

    @Override
    protected void setRightClick() {

        if (!TextUtils.isEmpty(mOldName) && mNewName.equals(mOldName)) {
            showSnackErrorMessage(getString(R.string.chat_edit_group_name_no_change));
            return;
        }
        EventBus.getDefault().post(mNewName, EventBusTagConfig.EVENT_IM_GROUP_EDIT_NAME);
        getActivity().finish();
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_edit_group_name);
    }


    @Override
    protected String setRightTitle() {
        return getString(R.string.__picker_done);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_edit_group_name;
    }
}
