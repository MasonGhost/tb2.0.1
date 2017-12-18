package com.zhiyicx.thinksnsplus.modules.circle.manager.members.attorn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.StickySectionDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MemberListFragment;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersContract;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/12/18/10:03
 * @Email Jliuer@aliyun.com
 * @Description 转让圈子
 */
public class AttornCircleFragment extends MemberListFragment implements MembersContract.View {

    public static final int ATTORNCIRCLECODE = 1995;
    public static final String CIRCLEOWNER = "circleowner";

    private ActionPopupWindow mPopupWindow;
    private CircleMembers mNewFounder;

    public static AttornCircleFragment newInstance(Bundle bundle) {
        AttornCircleFragment attornCircleFragment = new AttornCircleFragment();
        attornCircleFragment.setArguments(bundle);
        return attornCircleFragment;
    }

    @Override
    public boolean needManager() {
        return false;
    }

    @Override
    public boolean needBlackList() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<CircleMembers>(getActivity(), R.layout.item_circle_member,
                mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CircleMembers circleMembers, int position) {
                holder.setText(R.id.tv_member_name, circleMembers.getUser().getName());
                holder.setVisible(R.id.iv_member_more, View.INVISIBLE);
                TextView tag = holder.getTextView(R.id.tv_member_tag);

                boolean isManager = CircleMembers.ADMINISTRATOR.equals(circleMembers.getRole());
                boolean isOwner = CircleMembers.FOUNDER.equals(circleMembers.getRole());

                tag.setVisibility((isManager || isOwner) ? View.VISIBLE : View.GONE);
                tag.setBackgroundResource(isOwner ? R.drawable.shape_bg_circle_radus_gold : R
                        .drawable.shape_bg_circle_radus_gray);
                tag.setText(isOwner ? R.string.circle_owner : R.string.circle_manager);

                RxView.clicks(holder.itemView)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> initPopupWindow(circleMembers));
            }
        };
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new StickySectionDecoration(mActivity, position -> {
            if (mListDatas.isEmpty() || position >= mListDatas.size()) {
                return null;
            }
            StickySectionDecoration.GroupInfo groupInfo = new StickySectionDecoration.GroupInfo(1, mActivity.getString(R.string
                    .circle_manager_attorn));
            groupInfo.setPosition(position);
            groupInfo.setGroupLength(mListDatas.size());
            return groupInfo;
        });
    }

    private void initPopupWindow(CircleMembers circleMembers) {
        if (mPopupWindow != null) {
            return;
        }
        mPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.circle_manager_sure_attorn))
                .item2Str(getString(R.string.determine))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mPopupWindow.hide();
                    mPresenter.attornCircle(circleMembers);
                })
                .bottomClickListener(() -> mPopupWindow.hide()).build();
        mPopupWindow.show();
    }

    @Override
    public void attornSuccess(CircleMembers circleMembers) {
        super.attornSuccess(circleMembers);
        mNewFounder = circleMembers;
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.DONE) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(CIRCLEOWNER, mNewFounder);
            intent.putExtras(bundle);
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        }
    }
}
