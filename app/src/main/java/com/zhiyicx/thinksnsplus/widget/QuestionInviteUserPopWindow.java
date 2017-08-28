package com.zhiyicx.thinksnsplus.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

/**
 * @author Catherine
 * @describe 显示邀请的人数的弹框
 * @date 2017/8/17
 * @contact email:648129313@qq.com
 */

public class QuestionInviteUserPopWindow extends PopupWindow{

    private Activity mActivity;
    private View mParentView;
    private View mContentView;
    private float mAlpha;
    private UserInfoBean mUserInfoBean;

    public QuestionInviteUserPopWindow() {
    }

    public QuestionInviteUserPopWindow(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mParentView = builder.mParentView;
        this.mAlpha = builder.mAlpha;
        this.mUserInfoBean = builder.mUserInfoBean;
        initView();
    }

    private void initView() {
        initLayout();
        setWidth(mContentView.getResources().getDimensionPixelOffset(R.dimen.question_invite_width));
        setHeight(mContentView.getResources().getDimensionPixelOffset(R.dimen.question_invite_height));
        setFocusable(true);
        setOutsideTouchable(true);
        setAnimationStyle(R.style.style_actionPopupAnimation);
        setContentView(mContentView);
    }

    private void initLayout() {
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_question_invite, null);
        bindData();
        setOnDismissListener(() -> setWindowAlpha(1.0f));
    }

    private void setWindowAlpha(float alpha) {
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        params.verticalMargin = 100;
        mActivity.getWindow().setAttributes(params);
    }

    public void show() {
        setWindowAlpha(mAlpha);
        int width = UIUtils.getWindowWidth(mActivity) - mContentView.getResources().getDimensionPixelOffset(R.dimen.qa_top_select_width) -
                mContentView.getResources().getDimensionPixelOffset(R.dimen.spacing_normal);
        showAsDropDown(mParentView == null ? mContentView : mParentView, -20, 10);
    }

    public void setUserInfoBean(UserInfoBean userInfoBean){
        this.mUserInfoBean = userInfoBean;
        bindData();
    }

    private void bindData(){
        UserAvatarView userAvatarView = (UserAvatarView) mContentView.findViewById(R.id.iv_user_portrait);
        TextView tvUserName = (TextView) mContentView.findViewById(R.id.tv_user_name);
        ImageUtils.loadCircleUserHeadPic(mUserInfoBean, userAvatarView);
        tvUserName.setText(mUserInfoBean.getName());
        mContentView.setOnClickListener(v -> {
            PersonalCenterFragment.startToPersonalCenter(mActivity, mUserInfoBean);
        });
    }

    public void hide() {
        dismiss();
    }

    public static final class Builder {
        private Activity mActivity;
        private View mParentView;
        private float mAlpha;
        private UserInfoBean mUserInfoBean;

        private Builder() {
        }

        public QuestionInviteUserPopWindow.Builder with(Activity mActivity) {
            this.mActivity = mActivity;
            return this;
        }

        public QuestionInviteUserPopWindow.Builder parentView(View parentView) {
            this.mParentView = parentView;
            return this;
        }

        public QuestionInviteUserPopWindow.Builder alpha(float alpha) {
            this.mAlpha = alpha;
            return this;
        }

        public QuestionInviteUserPopWindow.Builder setData(UserInfoBean userInfoBean){
            this.mUserInfoBean = userInfoBean;
            return this;
        }

        public QuestionInviteUserPopWindow build() {
            return new QuestionInviteUserPopWindow(this);
        }
    }

    public static QuestionInviteUserPopWindow.Builder Builder() {
        return new QuestionInviteUserPopWindow.Builder();
    }
}
