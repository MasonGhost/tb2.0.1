package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectFriendsAllAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectedFriendsAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2.BUNDLE_CHAT_DATA;

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
    @BindView(R.id.fl_search_result)
    FrameLayout mFlSearchResult;
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
                .subscribe(aBoolean -> mFlSearchResult.setVisibility(aBoolean ? View.VISIBLE : View.GONE));
        RxTextView.textChanges(mEditSearchFriends)
                .subscribe(charSequence -> {
                    if (!TextUtils.isEmpty(charSequence)) {
                        // 搜索
                        mPresenter.getFriendsListByKey((long) mSearchResultList.size(), charSequence.toString());
                    }
                });
        RxView.clicks(mFlSearchResult)
                .subscribe(aVoid -> {
                    mEditSearchFriends.clearFocus();
                    mSearchResultList.clear();
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
        if (mSelectedList.size() > 0) {
            mPresenter.createConversation(mSelectedList);
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_select_friends;
    }

    /**
     * 聊天按钮是否可以点击
     */
    private void checkData() {
        mToolbarRight.setEnabled(mSelectedList.size() != 0);
        if (mSelectedList.size() > 0) {
            setRightText(String.format(getString(R.string.select_friends_right_title), mSelectedList.size()));
            mToolbarRight.setTextColor(getColor(R.color.themeColor));
        } else {
            setRightText(getString(R.string.select_friends_right_title_default));
            mToolbarRight.setTextColor(getColor(R.color.normal_for_disable_button_text));
        }
        mSelectedFriendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserSelected(UserInfoBean userInfoBean) {
        // 选中的列表中，如果是选中 那么直接加
        if (userInfoBean.isSelected()) {
            mSelectedList.add(userInfoBean);
        } else {
            for (UserInfoBean userInfoBean1 : mSelectedList) {
                if (userInfoBean1.getUser_id().equals(userInfoBean.getUser_id())) {
                    // 列表中已经有这个用户了->取消选中->直接移除这个人，这里因为有搜索列表，所以不能直接remove
                    mSelectedList.remove(userInfoBean1);
                }
            }
        }

        // 处理全部列表, 搜索有数据，则表示此时为搜索(在隐藏搜索列表时清空了列表)
        if (mSearchResultList.size() > 0 && mEditSearchFriends.hasFocus()) {
            int position = 0;
            List<UserInfoBean> newList = new ArrayList<>();
            for (UserInfoBean userInfoBean1 : mListDatas) {
                if (userInfoBean1.getUser_id().equals(userInfoBean.getUser_id())) {
                    position = mListDatas.indexOf(userInfoBean1);
                    userInfoBean1.setSelected(userInfoBean.isSelected());
                    newList.add(userInfoBean1);
                    break;
                }
            }
            mListDatas.remove(position);
            mListDatas.addAll(position, newList);
            mAdapter.notifyDataSetChanged();
        }
        checkData();
    }

    @Override
    public void getFriendsListByKeyResult(List<UserInfoBean> userInfoBeans) {
        checkUserIsSelected(userInfoBeans);
        mSearchResultList.clear();
        mSearchResultList.addAll(userInfoBeans);
        mSearchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public void createConversionResult(List<ChatUserInfoBean> list, EMConversation.EMConversationType type, int chatType, String id) {
        if (type == EMConversation.EMConversationType.Chat){
            EMClient.getInstance().chatManager().getConversation(id, type, true);
        } else {
            EMClient.getInstance().groupManager().getGroup(id);
        }
        Intent to = new Intent(getActivity(), ChatActivityV2.class);
        Bundle bundle = new Bundle();
        bundle.putString(EaseConstant.EXTRA_USER_ID, id);
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, chatType);
        bundle.putParcelableArrayList(ChatConfig.MESSAGE_CHAT_MEMBER_LIST, (ArrayList<? extends Parcelable>) list);
        to.putExtra(BUNDLE_CHAT_DATA, bundle);
        startActivity(to);
        getActivity().finish();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        checkUserIsSelected(data);
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }

    /**
     * 获取网络数据后，调用此方法来判断是否已选中
     *
     * @param list data
     */
    private void checkUserIsSelected(List<UserInfoBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (UserInfoBean userInfoBean : list) {
            // 给已经选中的用户手动设置
            for (int i = 0; i < mSelectedList.size(); i++) {
                if (userInfoBean.getUser_id().equals(mSelectedList.get(i).getUser_id())) {
                    userInfoBean.setSelected(true);
                    break;
                }
            }
        }
    }
}
