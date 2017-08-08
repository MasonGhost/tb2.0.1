package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.edittext.InfoInputEditText;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class AddInfoFragment extends TSFragment<AddInfoContract.Presenter> implements AddInfoContract.View {


    @BindView(R.id.bt_add_category)
    CombinationButton mBtAddCategory;
    @BindView(R.id.fl_tags)
    TagFlowLayout mFlTags;
    @BindView(R.id.ll_tag_container)
    LinearLayout mLlTagContainer;
    @BindView(R.id.tv_from)
    InfoInputEditText mTvFrom;
    @BindView(R.id.tv_author)
    InfoInputEditText mTvAuthor;
    @BindView(R.id.v_horizontal_line)
    View mVHorizontalLine;
    @BindView(R.id.et_info_summary)
    UserInfoInroduceInputView mEtInfoSummary;
    private ActionPopupWindow mLoginoutPopupWindow;// 退出登录选择弹框

    public static AddInfoFragment newInstance(Bundle bundle) {

        AddInfoFragment fragment = new AddInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddInfoFragment newInstance() {
        return new AddInfoFragment();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_publish_add_info;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_title);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.next);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
    }


    private void initListener() {
        // 栏目
        RxView.clicks(mBtAddCategory)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> showSnackSuccessMessage("category"));
        // 标签
        RxView.clicks(mLlTagContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> showSnackSuccessMessage("tags"));

    }

    /**
     * 初始化登录选择弹框
     */
    private void initLoginOutPopupWindow() {
        if (mLoginoutPopupWindow != null) {
            return;
        }
        mLoginoutPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.is_sure_login_out))
                .item2Str(getString(R.string.login_out_sure))
                .item2Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mLoginoutPopupWindow.hide();
                })
                .bottomClickListener(() -> mLoginoutPopupWindow.hide()).build();

    }


}
