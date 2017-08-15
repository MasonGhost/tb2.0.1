package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;

import java.util.List;

import butterknife.BindView;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */
public class PublishQuestionFragment extends TSListFragment<PublishQuestionContract.Presenter, QAListInfoBean>
        implements PublishQuestionContract.View {

    public static final String BUNDLE_PUBLISHQA_BEAN = "publish_bean";

    @BindView(R.id.et_qustion)
    EditText mEtQustion;

    @BindView(R.id.line)
    View mLine;

    private String mQuestionStr = "";
    private ActionPopupWindow mEditWarningPopupWindow;// 退出编辑警告弹框

    public static PublishQuestionFragment newInstance() {
        Bundle args = new Bundle();
        PublishQuestionFragment fragment = new PublishQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_qustion;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        if (mQuestionStr.endsWith("?") || mQuestionStr.endsWith("？")) {
            addTopic();
        } else {
            showSnackErrorMessage(getString(R.string.qa_publish_title_toast));
        }

    }

    private void addTopic() {
        Intent intent = new Intent(getActivity(), AddTopicActivity.class);
        Bundle bundle = new Bundle();
        QAPublishBean qaPublishBean = new QAPublishBean();
        qaPublishBean.setSubject(mQuestionStr);
        bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, qaPublishBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initListener();
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, "all", isLoadMore);
    }

    private void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        mPresenter.requestNetData(subject, maxId, type, isLoadMore);
    }

    @Override
    protected List<QAListInfoBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return super.requestCacheData(maxId, isLoadMore);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        PublishQuestionAdapter adapter = new PublishQuestionAdapter(getContext(), R.layout.item_publish_question, mListDatas);
        return adapter;
    }

    /**
     * 初始化图片选择弹框
     */
    private void initEditWarningPop() {
        DeviceUtils.hideSoftKeyboard(getContext(), mEtQustion);
        if (mEditWarningPopupWindow != null) {
            return;
        }
        mEditWarningPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.edit_quit))
                .item2Str(getString(R.string.save_to_draft_box))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .item2ClickListener(() -> {
                    mEditWarningPopupWindow.hide();
                })
                .bottomClickListener(() -> mEditWarningPopupWindow.hide()).build();
    }

    private void initListener() {
        RxTextView.textChanges(mEtQustion)
                .subscribe(charSequence -> {
                    mQuestionStr = charSequence.toString().trim();
                    if (!TextUtils.isEmpty(mQuestionStr)) {
                        mToolbarRight.setEnabled(true);
                        // TODO: 20177/25  搜索相同的問題
                    } else {
                        mToolbarRight.setEnabled(false);
                    }
                    requestNetData(mQuestionStr, 0L, "all", false);
                }, throwable -> mToolbarRight.setEnabled(false));

        mEtQustion.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        mToolbarRight.performClick();
                        DeviceUtils.hideSoftKeyboard(getContext(), mEtQustion);
                        return true;
                    }
                    return false;
                });
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mQuestionStr)) {
            super.setLeftClick();
        } else {
            initEditWarningPop();
            mEditWarningPopupWindow.show();
        }
    }
}
