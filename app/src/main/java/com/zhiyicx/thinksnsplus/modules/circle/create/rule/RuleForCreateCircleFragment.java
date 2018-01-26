package com.zhiyicx.thinksnsplus.modules.circle.create.rule;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleContract;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/12/25/9:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RuleForCreateCircleFragment extends TSFragment<CreateCircleContract.Presenter> implements CreateCircleContract.View {

    @Override
    public void setCircleInfo(CircleInfo data) {

    }

    public static final String RULE = "markdown_rule";

    @BindView(R.id.md_user_rule)
    MarkdownView mMarkdownView;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    String mMarkDownRule;

    @Override
    protected String setCenterTitle() {
        return getString(R.string.circle_rule);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    public static RuleForCreateCircleFragment newInstance(Bundle bundle) {
        RuleForCreateCircleFragment fragment = new RuleForCreateCircleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        mPresenter.getRule();
    }

    @Override
    public void setCircleRule(String rule) {
        mTvContent.setText(rule);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_rule;
    }
}
