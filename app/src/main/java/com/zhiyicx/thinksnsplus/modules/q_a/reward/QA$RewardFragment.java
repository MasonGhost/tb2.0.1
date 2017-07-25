package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;

/**
 * @author Catherine
 * @describe 悬赏页面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class QA$RewardFragment extends TSFragment<QA$RewardContract.Presenter> implements QA$RewardContract.View{


    public static QA$RewardFragment instance(Bundle bundle){
        QA$RewardFragment fragment = new QA$RewardFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_post_reward;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }
}
