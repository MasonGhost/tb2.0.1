package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */

public class ExpertSearchFragment extends TSListFragment<ExpertSearchContract.Presenter, ExpertBean>
        implements ExpertSearchContract.View{

    public ExpertSearchFragment instance(Bundle bundle){
        ExpertSearchFragment fragment = new ExpertSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_search_expert;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
