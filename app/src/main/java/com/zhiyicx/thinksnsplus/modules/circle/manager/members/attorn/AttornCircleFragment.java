package com.zhiyicx.thinksnsplus.modules.circle.manager.members.attorn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.recycleviewdecoration.StickySectionDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MemberListFragment;
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Locale;
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
    public static final String CIRCLE_OWNER = "circleowner";
    public static final String CIRCLE_NAME = "circleName";

    private ActionPopupWindow mPopupWindow;
    private CircleMembers mNewFounder;

    private String mCircleName;

    public static AttornCircleFragment newInstance(Bundle bundle) {
        AttornCircleFragment attornCircleFragment = new AttornCircleFragment();
        attornCircleFragment.setArguments(bundle);
        return attornCircleFragment;
    }

    @Override
    protected void initData() {
        super.initData();
        mCircleName = getArguments().getString(CIRCLE_NAME);
    }

    @Override
    public boolean needFounder() {
        return false;
    }

    @Override
    public boolean needBlackList() {
        return false;
    }

    @Override
    protected boolean needMore() {
        return false;
    }

    @Override
    protected boolean isAttornCircle() {
        return true;
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

    @Override
    protected void initPopWindow(View v, int pos, CircleMembers circleMembers) {
        mPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.info_publish_hint))
                .desStr(String.format(Locale.getDefault(), getString(R.string.circle_manager_sure_attorn), mCircleName,
                        circleMembers.getUser().getName()))
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
            bundle.putParcelable(CIRCLE_OWNER, mNewFounder);
            intent.putExtras(bundle);
            mActivity.setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        }
    }
}
