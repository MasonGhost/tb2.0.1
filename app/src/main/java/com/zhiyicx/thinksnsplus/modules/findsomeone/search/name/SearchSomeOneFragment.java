package com.zhiyicx.thinksnsplus.modules.findsomeone.search.name;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListAdapter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Jungle68
 * @describe 用户搜索界面
 * @date 2017/1/9
 * @contact master.jungle68@gmail.com
 */
public class SearchSomeOneFragment extends TSListFragment<SearchSomeOneContract.Presenter, UserInfoBean> implements SearchSomeOneContract.View,
        MultiItemTypeAdapter.OnItemClickListener {

    public static final String BUNDLE_LOCATION_STRING = "location_string";


    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;

    @BindView(R.id.fragment_search_container)
    RelativeLayout mFragmentInfoSearchContainer;
    @BindView(R.id.fragment_search_cancle)
    TextView mTvSearchCancel;


    public static SearchSomeOneFragment newInstance(Bundle args) {

        SearchSomeOneFragment fragment = new SearchSomeOneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_location_search;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTvSearchCancel;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setEmptyViewVisiable(false);
        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.searchUser(mFragmentInfoSearchEdittext.getText().toString());
                DeviceUtils.hideSoftKeyboard(getContext(), mFragmentInfoSearchEdittext);
            }
        });
        mRvList.setBackgroundResource(R.color.white);
//        RxTextView.afterTextChangeEvents(mFragmentInfoSearchEdittext)
//                .subscribe(textViewAfterTextChangeEvent -> {
//                    mPresenter.searchUser(textViewAfterTextChangeEvent.editable().toString());
//                });
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(BUNDLE_LOCATION_STRING))) {
            mFragmentInfoSearchEdittext.setText(getArguments().getString(BUNDLE_LOCATION_STRING));
            mFragmentInfoSearchEdittext.setSelection(getArguments().getString(BUNDLE_LOCATION_STRING).length());
        }
        mPresenter.getRecommentUser();
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @OnClick({R.id.fragment_search_back, R.id.fragment_search_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_back:
                getActivity().finish();
                break;
            case R.id.fragment_search_cancle:
                getActivity().finish();
                break;
            default:
        }
    }

    @Override
    protected CommonAdapter getAdapter() {
        CommonAdapter adapter = new FindSomeOneListAdapter(getActivity(), R.layout.item_find_some_list, mListDatas, mPresenter);
        adapter.setOnItemClickListener(this);
        return adapter;

    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_search;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//
//        UserInfoBean bean = mListDatas.get(position);
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(BUNDLE_DATA, bean);
//        intent.putExtras(bundle);
//        getActivity().setResult(RESULT_OK, intent);
//        getActivity().finish();
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }
}
