package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.HanziToPinyin;
import com.zhiyicx.common.utils.recycleviewdecoration.StickySectionDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MemberListFragment extends TSListFragment<MembersContract.Presenter, CircleMembers>
        implements MembersContract.View {

    public static final String CIRCLEID = "circleID";

    @BindView(R.id.fragment_search_back)
    ImageView mFragmentSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_search_cancle)
    TextView mFragmentSearchCancle;

    private int[] mFrouLengh;

    private ChooseBindPopupWindow mPopupWindow;

    private boolean isSearch;

    List<CircleMembers> cache = new ArrayList<>();

    public static MemberListFragment newInstance(Bundle bundle) {
        MemberListFragment memberListFragment = new MemberListFragment();
        memberListFragment.setArguments(bundle);
        return memberListFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_circle_members;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public long getCIrcleId() {
        return getArguments().getLong(CIRCLEID);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<CircleMembers>(getActivity(), R.layout.item_circle_member,
                mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CircleMembers circleMembers, int position) {
                holder.setText(R.id.tv_member_name, circleMembers.getUser().getName());
                ImageView more = holder.getImageViwe(R.id.iv_member_more);
                TextView tag = holder.getTextView(R.id.tv_member_tag);
                more.setVisibility(CircleMembers.FOUNDER.equals(circleMembers.getRole()) ? View.INVISIBLE : View.VISIBLE);
                boolean isManager = CircleMembers.ADMINISTRATOR.equals(circleMembers.getRole());
                boolean isOwner = CircleMembers.FOUNDER.equals(circleMembers.getRole());
                tag.setVisibility((isManager || isOwner) ? View.VISIBLE : View.GONE);
                tag.setBackgroundResource(isOwner ? R.drawable.shape_bg_circle_radus_gold : R.drawable.shape_bg_circle_radus_gray);
                tag.setText(isOwner ? R.string.circle_owner : R.string.circle_manager);
                RxView.clicks(more)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            initPopWindow(more, position, circleMembers);
                        });
            }
        };
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        RxTextView.textChanges(mFragmentInfoSearchEdittext).subscribe(charSequence -> filterData(charSequence));
    }

    @Override
    public void setGroupLengh(int[] grouLengh) {
        mFrouLengh = grouLengh;
    }

    @Override
    public int[] getGroupLengh() {
        return mFrouLengh;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CircleMembers> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        cache.clear();
        cache.addAll(data);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {

        return new StickySectionDecoration(getActivity(), position -> {
            if (mListDatas.isEmpty() || position >= mListDatas.size() || isSearch) {
                return null;
            }
            CircleMembers members = mListDatas.get(position);
            StickySectionDecoration.GroupInfo groupInfo = null;
            switch (members.getRole()) {
                case CircleMembers.FOUNDER:
                case CircleMembers.ADMINISTRATOR:
                    groupInfo = new StickySectionDecoration.GroupInfo(1,
                            String.format(Locale.getDefault(), mActivity.getString(R.string.circle_manager_format), mFrouLengh[0] + mFrouLengh[1]));
                    groupInfo.setPosition(position);
                    groupInfo.setGroupLength(mFrouLengh[0] + mFrouLengh[1]);
                    break;
                case CircleMembers.MEMBER:
                    groupInfo = new StickySectionDecoration.GroupInfo(2,
                            String.format(Locale.getDefault(), mActivity.getString(R.string.circle_member_format), mFrouLengh[2]));
                    groupInfo.setPosition(position);
                    groupInfo.setGroupLength(mFrouLengh[2]);
                    break;
                case CircleMembers.BLACKLIST:
                    groupInfo = new StickySectionDecoration.GroupInfo(3,
                            String.format(Locale.getDefault(), mActivity.getString(R.string.circle_blacklist_format), mFrouLengh[3]));
                    groupInfo.setPosition(position);
                    groupInfo.setGroupLength(mFrouLengh[3]);
                    break;
                default:
            }

            return groupInfo;
        });
    }

    protected void initPopWindow(View v, int pos, CircleMembers members) {
        boolean isManager = CircleMembers.ADMINISTRATOR.equals(members.getRole());
        boolean isMember = CircleMembers.MEMBER.endsWith(members.getRole());
        boolean isBlackList = CircleMembers.BLACKLIST.equals(members.getRole());

        mPopupWindow = ChooseBindPopupWindow.Builder()
                .with(mActivity)
                .alpha(0.8f)
                .itemlStr(mActivity.getString(isManager ? R.string.cancel_manager : (isMember ? R.string.appoint_manager : R.string.cancle_circle)))
                .item2Str(mActivity.getString(isManager ? R.string.empty : (isMember ? R.string.cancle_circle : R.string.cancle_blacklist)))
                .item3Str(mActivity.getString(isManager ? R.string.empty : (isMember ? R.string.appoint_blacklist : R.string.empty)))
                .isOutsideTouch(true)
                .itemListener(position -> {
                    MembersPresenter.MemberHandleType type = null;

                    if (isManager) {
                        type = MembersPresenter.MemberHandleType.CANCLE_MANAFER;
                    }
                    if ((isMember && pos == 1) || (isBlackList && pos == 0)) {
                        type = MembersPresenter.MemberHandleType.CANCLE_MEMBER;
                    }

                    if (isMember && pos == 0) {
                        type = MembersPresenter.MemberHandleType.APPOINT_MANAFER;
                    }

                    if (isMember && pos == 2) {
                        type = MembersPresenter.MemberHandleType.CANCLE_BLACKLIST;
                    }

                    if (isBlackList && pos == 1) {
                        type = MembersPresenter.MemberHandleType.CANCLE_BLACKLIST;
                    }
                    mPresenter.dealCircleMember(type, members);
                    mPopupWindow.hide();
                })
                .build();
        mPopupWindow.showAsDropDown(v);
    }

    private void filterData(CharSequence filterStr) {
        if (TextUtils.isEmpty(filterStr)) {
            isSearch = false;
            mListDatas.clear();
            mListDatas.addAll(cache);
        } else {
            isSearch = true;
            List<CircleMembers> result = new ArrayList<>();
            for (CircleMembers sortModel : mListDatas) {
                String name = sortModel.getUser().getName();
                if (name.contains(filterStr.toString()) ||
                        HanziToPinyin.getInstance().getSpellStr(name).startsWith(filterStr.toString())) {
                    result.add(sortModel);
                }
            }
            mListDatas.clear();
            mListDatas.addAll(result);
        }
        refreshData();
    }

    @OnClick({R.id.fragment_search_back, R.id.fragment_search_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_back:
                break;
            case R.id.fragment_search_cancle:
                break;
            default:
        }
    }
}
