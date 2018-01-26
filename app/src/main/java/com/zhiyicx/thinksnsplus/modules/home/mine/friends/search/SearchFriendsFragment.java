package com.zhiyicx.thinksnsplus.modules.home.mine.friends.search;

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
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/25
 * @contact email:648129313@qq.com
 */

public class SearchFriendsFragment extends TSListFragment<SearchFriendsContract.Presenter, UserInfoBean>
        implements SearchFriendsContract.View{

    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;

    private String mKeyWord;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyFriendsListAdapter(getContext(), mListDatas, mPresenter);
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
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_search;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
       setEmptyViewVisiable(false);
        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                mKeyWord = mFragmentInfoSearchEdittext.getText().toString();
                mPresenter.requestNetData(0L, false);
                DeviceUtils.hideSoftKeyboard(getContext(), mFragmentInfoSearchEdittext);
            }
        });
        mRvList.setBackgroundResource(R.color.white);
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
    public String getKeyWord() {
        return TextUtils.isEmpty(mKeyWord) ? "" : mKeyWord;
    }
}
