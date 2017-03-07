package com.zhiyicx.thinksnsplus.modules.information.infosearch;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class SearchFragment extends TSFragment {

    @BindView(R.id.fragment_info_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_info_search_cancle)
    TextView mFragmentInfoSearchCancle;
    @BindView(R.id.fragment_info_search_result)
    RecyclerView mFragmentInfoSearchResult;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_search;
    }

    @Override
    protected void initView(View rootView) {
        mFragmentInfoSearchEdittext.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            ToastUtils.showToast("IME_ACTION_SEARCH");
                            return true;
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @OnClick({R.id.fragment_info_search_back, R.id.fragment_info_search_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_info_search_back:
                getActivity().finish();
                break;
            case R.id.fragment_info_search_cancle:
                getActivity().finish();
                break;
        }
    }
}
