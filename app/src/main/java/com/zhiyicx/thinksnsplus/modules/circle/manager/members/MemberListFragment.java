package com.zhiyicx.thinksnsplus.modules.circle.manager.members;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.StickySectionDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/12/08/15:38
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MemberListFragment extends TSListFragment<MembersContract.Presenter, CircleMembers>
        implements MembersContract.View {

    public static final String CIRCLEID = "circleID";

    private int[] mFrouLengh;

    public static MemberListFragment newInstance(Bundle bundle) {
        MemberListFragment memberListFragment = new MemberListFragment();
        memberListFragment.setArguments(bundle);
        return memberListFragment;
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
            }
        };
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void setGroupLengh(int[] grouLengh) {
        mFrouLengh = grouLengh;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<CircleMembers> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {

        return new StickySectionDecoration(getActivity(), position -> {
            if (mListDatas.isEmpty()||position >= mListDatas.size()) {
                return null;
            }
            CircleMembers members = mListDatas.get(position);
            StickySectionDecoration.GroupInfo groupInfo = null;
            switch (members.getRole()) {
                case CircleMembers.FOUNDER:
                case CircleMembers.ADMINISTRATOR:
                    groupInfo = new StickySectionDecoration.GroupInfo(1, "管理员");
                    groupInfo.setPosition(0);
                    groupInfo.setGroupLength(mFrouLengh[0] + mFrouLengh[1]);
                    break;
                case CircleMembers.MEMBER:
                    groupInfo = new StickySectionDecoration.GroupInfo(2, "成员");
                    groupInfo.setPosition(position);
                    groupInfo.setGroupLength(mFrouLengh[2]);
                    break;
                case CircleMembers.BLACKLIST:
                    groupInfo = new StickySectionDecoration.GroupInfo(3, "黑名单");
                    groupInfo.setPosition(position);
                    groupInfo.setGroupLength(mFrouLengh[3]);
                    break;
                default:
            }

            return groupInfo;
        });
    }
}
