package com.zhiyicx.thinksnsplus.modules.circle.manager.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.View;
import android.widget.Button;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningContract;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningPresenter;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.CircleEarningPresenterModule;
import com.zhiyicx.thinksnsplus.modules.circle.manager.earning.DaggerCircleEarningComponent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/12/15/9:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PermissionFragment extends TSFragment<CircleEarningContract.Presenter> implements CircleEarningContract.View {

    public static final String PERMISSION = "permission";
    public static final String CIRCLEID = "circleid";

    public static final int PERMISSION_ALL = 0;
    public static final int PERMISSION_OWNER = 1;
    public static final int PERMISSION_MANAGER = 2;

    public static final int PERMISSION_REQUEST = 1994;

    @BindView(R.id.tv_permission_all)
    AppCompatCheckedTextView mTvPermissionAll;
    @BindView(R.id.tv_permission_owner)
    AppCompatCheckedTextView mTvPermissionOwner;
    @BindView(R.id.tv_permission_manager)
    AppCompatCheckedTextView mTvPermissionManager;
    @BindView(R.id.permission_frame)
    Button mFrame;

    private List<String> mPermissionType = new ArrayList<>();
    private long mCircleId;
    private int mDefaultPermission;
    private StringBuilder mPermissions;

    @Inject
    CircleEarningPresenter mCircleDetailPresenter;

    public static PermissionFragment newInstance(Bundle bundle) {
        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.circle_post_permission);
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() == null) {
            return;
        }
        setCheckPosition(getArguments().getInt(PERMISSION));
        mCircleId = getArguments().getLong(CIRCLEID);
    }

    @Override
    protected void initData() {
        DaggerCircleEarningComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .circleEarningPresenterModule(new CircleEarningPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_post_permission;
    }


    @OnClick({R.id.tv_permission_all, R.id.tv_permission_owner, R.id.tv_permission_manager})
    public void onViewClicked(View view) {
        mPermissionType.clear();
        mFrame.setVisibility(View.VISIBLE);
        switch (view.getId()) {
            case R.id.tv_permission_all:
                if (mDefaultPermission == PERMISSION_ALL) {
                    return;
                }
                setCheckPosition(PERMISSION_ALL);
                mPermissionType.add(CircleMembers.FOUNDER);
                mPermissionType.add(CircleMembers.ADMINISTRATOR);
                mPermissionType.add(CircleMembers.MEMBER);
                break;
            case R.id.tv_permission_owner:
                if (mDefaultPermission == PERMISSION_OWNER) {
                    return;
                }
                setCheckPosition(PERMISSION_OWNER);
                mPermissionType.add(CircleMembers.FOUNDER);
                break;
            case R.id.tv_permission_manager:
                if (mDefaultPermission == PERMISSION_MANAGER) {
                    return;
                }
                setCheckPosition(PERMISSION_MANAGER);
                mPermissionType.add(CircleMembers.FOUNDER);
                mPermissionType.add(CircleMembers.ADMINISTRATOR);
                break;
            default:
        }
        mPresenter.setCirclePermissions(mCircleId, mPermissionType);
    }

    public void setCheckPosition(int checkPosition) {
        mDefaultPermission = checkPosition;
        switch (checkPosition) {
            case PERMISSION_ALL:
                mTvPermissionAll.setChecked(true);
                mTvPermissionOwner.setChecked(false);
                mTvPermissionManager.setChecked(false);
                break;
            case PERMISSION_OWNER:
                mTvPermissionAll.setChecked(false);
                mTvPermissionOwner.setChecked(true);
                mTvPermissionManager.setChecked(false);
                break;
            case PERMISSION_MANAGER:
                mTvPermissionAll.setChecked(false);
                mTvPermissionOwner.setChecked(false);
                mTvPermissionManager.setChecked(true);
                break;
            default:
        }
    }

    @Override
    public void permissionResult(List<String> permission) {
        mPermissions = new StringBuilder();
        for (String str : permission) {
            mPermissions.append(str + ",");
        }
        mFrame.setVisibility(View.GONE);
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (mPermissions == null) {
            mActivity.finish();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(PERMISSION, mPermissions.toString());
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        mFrame.setVisibility(View.GONE);
    }
}
