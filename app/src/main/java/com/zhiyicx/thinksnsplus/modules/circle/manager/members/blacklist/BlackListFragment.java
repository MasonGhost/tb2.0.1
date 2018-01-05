package com.zhiyicx.thinksnsplus.modules.circle.manager.members.blacklist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import com.zhiyicx.thinksnsplus.modules.circle.manager.members.MembersPresenter;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/12/18/10:03
 * @Email Jliuer@aliyun.com
 * @Description 黑名单
 */
public class BlackListFragment extends MemberListFragment implements MembersContract.View {

    public static final int BLACKLISTCODE = 1998;
    public static final String CIRCLE_OWNER = "circleowner";
    public static final String CIRCLE_NAME = "circleName";

    private ActionPopupWindow mPopupWindow;
    private CircleMembers mNewFounder;

    private String mCircleName;

    public static BlackListFragment newInstance(Bundle bundle) {
        BlackListFragment attornCircleFragment = new BlackListFragment();
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
    public String getMemberType() {
        return CircleMembers.BLACKLIST;
    }

    @Override
    public void attornSuccess(CircleMembers circleMembers) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CIRCLEID, mListDatas.size());
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }
}
