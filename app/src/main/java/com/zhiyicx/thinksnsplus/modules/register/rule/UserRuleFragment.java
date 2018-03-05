package com.zhiyicx.thinksnsplus.modules.register.rule;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/10/27/13:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class UserRuleFragment extends TSFragment {

    public static final String RULE = "markdown_rule";
    public static final String TITLE = "markdown_TITLE";

    @BindView(R.id.md_user_rule)
    MarkdownView mMarkdownView;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    String mMarkDownRule;


    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    public static UserRuleFragment newInstance(Bundle bundle) {
        UserRuleFragment fragment = new UserRuleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mMarkDownRule = getArguments().getString(RULE, RULE);
        setCenterText(getArguments().getString(TITLE, getString(R.string.user_rule_register)));
        if (mMarkDownRule.contains("#")) {
            mMarkdownView.setVisibility(View.VISIBLE);
            mMarkdownView.loadMarkdown(mMarkDownRule);
        } else {
            mTvContent.setVisibility(View.VISIBLE);
            mTvContent.setText(mMarkDownRule);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_rule;
    }

    @Override
    public void onPause() {
        mMarkdownView.onPause();
        mMarkdownView.pauseTimers();
        super.onPause();

    }

    @Override
    public void onResume() {
        mMarkdownView.onResume();
        mMarkdownView.resumeTimers();
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destryWeb(mMarkdownView);
    }

    protected void destryWeb(WebView webView) {
        if (webView != null) {
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
        }
    }
}
