package com.zhiyicx.thinksnsplus.modules.chat.edit.owner;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.EditGroupOwnerAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * Created by Catherine on 2018/1/22.
 */

public class EditGroupOwnerFragment extends TSListFragment<EditGroupOwnerContract.Presenter, UserInfoBean>
        implements EditGroupOwnerContract.View {

    public static final String BUNDLE_GROUP_DATA = "bundle_group_data";

    @BindView(R.id.edit_search_friends)
    AppCompatEditText mEditSearchFriends;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;

    private ActionPopupWindow mAlertChangeOwnerPopupWindow;
    private UserInfoBean mNewOwner;
    private ChatGroupBean mChatGroupBean;

    public EditGroupOwnerFragment instance(Bundle bundle) {
        EditGroupOwnerFragment fragment = new EditGroupOwnerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        RxTextView.textChanges(mEditSearchFriends)
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence)){
                        mPresenter.getSearchResult(charSequence.toString());
                    } else {
                        mPresenter.requestNetData(0L, false);
                    }
                });
    }

    @OnClick({R.id.tv_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                getActivity().finish();
                break;
            default:
        }
    }

    @Override
    protected void initData() {
        super.initData();
        initAlertPopupWindow();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_edit_group_owner;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        EditGroupOwnerAdapter adapter = new EditGroupOwnerAdapter(getContext(), mListDatas);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                // 选中当前的人 那就返回
                if (mPresenter.checkNewOwner(mListDatas.get(position))) {
                    mNewOwner = mListDatas.get(position);
                    mAlertChangeOwnerPopupWindow.show();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    private void initAlertPopupWindow() {
        if (mAlertChangeOwnerPopupWindow == null) {
            mAlertChangeOwnerPopupWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.prompt))
                    .item2Str(getString(R.string.sure))
                    .desStr(getString(R.string.chat_edit_group_owner_edit_alert))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item2ClickListener(() -> {
                        EventBus.getDefault().post(mNewOwner, EventBusTagConfig.EVENT_IM_GROUP_CHANGE_OWNER);
                        getActivity().finish();
                    })
                    .bottomClickListener(() -> mAlertChangeOwnerPopupWindow.hide())
                    .build();
        }
    }

    @Override
    public ChatGroupBean getGroupData() {
        mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_DATA);
        return mChatGroupBean;
    }
}
