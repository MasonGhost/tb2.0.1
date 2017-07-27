package com.zhiyicx.thinksnsplus.modules.q_a.publish.detail;

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2017/07/26/16:03
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PublishContentFragment extends TSFragment<PublishContentConstact.Presenter> implements
        PublishContentConstact.View{

    public static PublishContentFragment newInstance(Bundle bundle){
        PublishContentFragment publishContentFragment=new PublishContentFragment();
        publishContentFragment.setArguments(bundle);
        return publishContentFragment;
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
        return getString(R.string.qa_detail);
    }

    @Override
    protected void initView(View rootView) {
        mToolbarLeft.setTextColor(SkinUtils.getColor(R.color.themeColor));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_content;
    }
}
