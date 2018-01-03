package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewAfterTextChangeEvent;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 地区搜索界面
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class LocationSearchFragment extends TSListFragment<LocationSearchContract.Presenter, LocationBean> implements LocationSearchContract.View,
        MultiItemTypeAdapter.OnItemClickListener {

    public static final String BUNDLE_DATA = "DATA";
    public static final String BUNDLE_LOCATION_STRING = "location_string";

    @BindView(R.id.fragment_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_search_cancle)
    TextView mFragmentInfoSearchCancle;
    @BindView(R.id.fragment_search_container)
    RelativeLayout mFragmentInfoSearchContainer;


    public static LocationSearchFragment newInstance(Bundle args) {

        LocationSearchFragment fragment = new LocationSearchFragment();
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
        return mFragmentInfoSearchCancle;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setEmptyViewVisiable(false);
        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.searchLocation(mFragmentInfoSearchEdittext.getText().toString());
                DeviceUtils.hideSoftKeyboard(getContext(), mFragmentInfoSearchEdittext);
            }
        });
        mRvList.setBackgroundResource(R.color.white);
//        RxTextView.afterTextChangeEvents(mFragmentInfoSearchEdittext)
//                .subscribe(textViewAfterTextChangeEvent -> {
//                    mPresenter.searchLocation(textViewAfterTextChangeEvent.editable().toString());
//
//                });
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString(BUNDLE_LOCATION_STRING))) {
            mFragmentInfoSearchEdittext.setText(getArguments().getString(BUNDLE_LOCATION_STRING));
            mFragmentInfoSearchEdittext.setSelection(getArguments().getString(BUNDLE_LOCATION_STRING).length());
        }
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0f;
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
        }
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new LocationSearchListAdapter(getActivity(), R.layout.item_location_search, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

        LocationBean bean = mListDatas.get(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_DATA, bean);
        intent.putExtras(bundle);
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

}
