package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QA_LIstInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragment extends TSListFragment<QA_ListInfoConstact.Presenter,QA_LIstInfoBean>{

    public static final String BUNDLE_QA_TYPE = "qa_type";
    public static final String BUNDLE_QA = "qa";
    private String mQAInfoType;

    public static QA_ListInfoFragment newInstance(String params) {
        QA_ListInfoFragment fragment = new QA_ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.setRefreshing(true);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
    }

    @Override
    protected List requestCacheData(Long maxId, boolean isLoadMore) {
        return mListDatas;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
//        super.requestNetData(maxId, isLoadMore);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<QA_LIstInfoBean>(getActivity(), R.layout.item_qa_content,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, QA_LIstInfoBean o, int position) {

            }
        };
    }
}
