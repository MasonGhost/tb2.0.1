package com.zhiyicx.thinksnsplus.modules.tb.rank;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author Jliuer
 * @Date 2018/02/28/13:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RankListFragment extends TSListFragment<RankListContract.Presenter,RankData> {


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tbranklist;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ranking);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
