package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectFriendsAllAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectedFriendsAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsFragment extends TSListFragment<SelectFriendsContract.Presenter, UserInfoBean>
        implements SelectFriendsContract.View, SelectFriendsAllAdapter.OnUserSelectedListener {

    @BindView(R.id.iv_search_icon)
    ImageView mIvSearchIcon;
    @BindView(R.id.rv_select_result)
    RecyclerView mRvSelectResult;
    @BindView(R.id.edit_search_friends)
    AppCompatEditText mEditSearchFriends;
    @BindView(R.id.rv_search_result)
    RecyclerView mRvSearchResult;

    private List<UserInfoBean> mSelectedList;
    private List<UserInfoBean> mSearchResultList;
    private SelectFriendsAllAdapter mSearchResultAdapter;
    private SelectedFriendsAdapter mSelectedFriendsAdapter;

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SelectFriendsAllAdapter allAdapter = new SelectFriendsAllAdapter(getContext(), mListDatas, this);
        return allAdapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        setLeftTextColor(R.color.themeColor);
        // 选中结果
        LinearLayoutManager selectManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvSelectResult.setLayoutManager(selectManager);
        // 搜索结果
        LinearLayoutManager searchManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvSearchResult.setLayoutManager(searchManager);

        RxView.focusChanges(mEditSearchFriends)
                .subscribe(aBoolean -> {
                    if (aBoolean){

                    }
                });
        RxTextView.textChanges(mEditSearchFriends)
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence)){
                        // 搜索
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        // 选中的结果
        mSelectedList = new ArrayList<>();
        mSelectedFriendsAdapter = new SelectedFriendsAdapter(getContext(), mSelectedList);
        mRvSelectResult.setAdapter(mSelectedFriendsAdapter);
        mRvSelectResult.addItemDecoration(new LinearDecoration(0, 0, 5, 5));
        // 搜索的结果
        mSearchResultList = new ArrayList<>();
        mSearchResultAdapter = new SelectFriendsAllAdapter(getContext(), mSearchResultList, this);
        mRvSearchResult.setAdapter(mSearchResultAdapter);
        checkData();
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.select_friends_right_title_default);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.select_friends_center_title);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected void setLeftClick() {
        getActivity().finish();
    }

    @Override
    protected void setRightClick() {
        // 发起聊天
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_select_friends;
    }

    /**
     * 聊天按钮是否可以点击
     */
    private void checkData(){
        mToolbarRight.setEnabled(mSelectedList.size() != 0);
        if (mSelectedList.size() > 0){
            setRightText(String.format(getString(R.string.select_friends_right_title), mSelectedList.size()));
            mToolbarRight.setTextColor(getColor(R.color.themeColor));
        } else {
            setRightText(getString(R.string.select_friends_right_title_default));
            mToolbarRight.setTextColor(getColor(R.color.normal_for_disable_button_text));
        }
        mSelectedFriendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserSelected(UserInfoBean userInfoBean, int position) {
        // 选中
        if (userInfoBean.isSelected()){
            mSelectedList.add(userInfoBean);
        } else {
            mSelectedList.remove(userInfoBean);
        }
        checkData();
    }
}
