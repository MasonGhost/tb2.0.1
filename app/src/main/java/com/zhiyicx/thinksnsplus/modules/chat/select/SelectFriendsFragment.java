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
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.exceptions.HyphenateException;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectFriendsAllAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectedFriendsAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.chat.ChatActivityV2.BUNDLE_CHAT_DATA;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsFragment extends TSListFragment<SelectFriendsContract.Presenter, UserInfoBean>
        implements SelectFriendsContract.View, SelectFriendsAllAdapter.OnUserSelectedListener {

    public static final String BUNDLE_GROUP_EDIT_DATA = "bundle_group_edit_data";
    public static final String BUNDLE_GROUP_IS_DELETE = "bundle_group_is_delete";

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

    /**
     * 群信息
     */
    private ChatGroupBean mChatGroupBean;
    /**
     * 是否是修改群资料
     */
    private boolean mIsFromEdit;
    /**
     * 是否是删除用户  如果是删除 那么则不用去请求好友列表
     */
    private boolean mIsDeleteMember;
    /**
     * 群组的用户
     */
    private List<UserInfoBean> mGroupUserList;

    public static SelectFriendsFragment instance(Bundle bundle) {
        SelectFriendsFragment friendsFragment = new SelectFriendsFragment();
        friendsFragment.setArguments(bundle);
        return friendsFragment;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        SelectFriendsAllAdapter allAdapter = new SelectFriendsAllAdapter(getContext(), mListDatas, this);
        return allAdapter;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSelectedList = new ArrayList<>();
        mSearchResultList = new ArrayList<>();
        getIntentData();
        setLeftTextColor(R.color.themeColor);
        initListener();
    }

    private void initListener() {
        RxView.focusChanges(mEditSearchFriends)
                .filter(aVoid -> mEditSearchFriends != null)
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

    /**
     * 获取传递过来的数据，如果是创建会话，那么则不会有传递过来的数据
     */
    private void getIntentData() {
        if (getArguments() != null) {
            mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_EDIT_DATA);
            mIsDeleteMember = getArguments().getBoolean(BUNDLE_GROUP_IS_DELETE);
            if (mChatGroupBean != null) {
                setCenterText(getString(!mIsDeleteMember ? R.string.chat_edit_group_add_member : R.string.chat_edit_group_remove_member));
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // 选中结果
        LinearLayoutManager selectManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvSelectResult.setLayoutManager(selectManager);
        mSelectedFriendsAdapter = new SelectedFriendsAdapter(getContext(), mSelectedList);
        mRvSelectResult.setAdapter(mSelectedFriendsAdapter);
        mRvSelectResult.addItemDecoration(new LinearDecoration(0, 0, 5, 5));

        // 搜索结果
        LinearLayoutManager searchManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRvSearchResult.setLayoutManager(searchManager);
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
            if (mChatGroupBean != null) {
                mPresenter.dealGroupMember(mSelectedList);
            } else {
                // 创建群
                mPresenter.createConversation(mSelectedList);
            }
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
        mIvSearchIcon.setVisibility(mSelectedList.size() > 0 ? View.GONE : View.VISIBLE);
        if (mSelectedList.size() > 0) {

            if (mChatGroupBean == null) {
                setRightText(String.format(getString(R.string.select_friends_right_title), mSelectedList.size()));
            } else {
                setRightText(String.format(getString(mIsDeleteMember ? R.string.chat_edit_group_remove_d : R.string.chat_edit_group_add_d), mSelectedList.size()));
            }

            mToolbarRight.setTextColor(getColor(R.color.themeColor));
        } else {
            if (mChatGroupBean == null) {
                setRightText(getString(R.string.select_friends_right_title_default));
            } else {
                setRightText(String.format(getString(mIsDeleteMember ? R.string.chat_edit_group_remove : R.string.chat_edit_group_add), mSelectedList.size()));
            }
            mToolbarRight.setTextColor(getColor(R.color.normal_for_disable_button_text));
        }
        mSelectedFriendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserSelected(UserInfoBean userInfoBean) {
        // 选中的列表中，如果是选中 那么直接加
        if (userInfoBean.getIsSelected() == 1) {
            mSelectedList.add(userInfoBean);
        } else {
            Iterator<UserInfoBean> userIterator = mSelectedList.iterator();
            while (userIterator.hasNext()) {
                UserInfoBean data = userIterator.next();
                if (data.getUser_id().equals(userInfoBean.getUser_id())) {
                    // 列表中已经有这个用户了->取消选中->直接移除这个人，这里因为有搜索列表，所以不能直接remove
                    userIterator.remove();
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
                    userInfoBean1.setIsSelected(userInfoBean.getIsSelected());
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
        if (type == EMConversation.EMConversationType.Chat) {
            EMClient.getInstance().chatManager().getConversation(id, type, true);
        } else {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(id);
            if (group == null) {
                showSnackErrorMessage("创建失败");
                return;
            }
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
    public boolean getIsDeleteMember() {
        return mIsDeleteMember;
    }

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }

    @Override
    public void dealGroupMemberResult() {
        getActivity().finish();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        checkUserIsSelected(data);
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public void onCacheResponseSuccess(List<UserInfoBean> data, boolean isLoadMore) {
        checkUserIsSelected(data);
        super.onCacheResponseSuccess(data, isLoadMore);
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
            if (mSelectedList.size() > 0) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (userInfoBean.getUser_id().equals(mSelectedList.get(i).getUser_id())) {
                        userInfoBean.setIsSelected(1);
                        break;
                    }
                }
            }
            // 如果是添加群成员 那么要把已经有的成员处理为不可点击
            if (!mIsDeleteMember && mChatGroupBean != null && mChatGroupBean.getAffiliations().size() > 0) {
                for (int i = 0; i < mChatGroupBean.getAffiliations().size(); i++) {
                    if (userInfoBean.getUser_id().equals(mChatGroupBean.getAffiliations().get(i).getUser_id())) {
                        userInfoBean.setIsSelected(-1);
                        break;
                    }
                }
            }
        }
    }
}
