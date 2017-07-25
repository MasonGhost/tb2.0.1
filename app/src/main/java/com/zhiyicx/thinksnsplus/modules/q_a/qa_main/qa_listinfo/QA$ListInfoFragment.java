package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA$ListInfoFragment extends TSListFragment{

    public static final String BUNDLE_QA_TYPE = "qa_type";
    public static final String BUNDLE_QA = "qa";
    private String mQAInfoType;

    public static QA$ListInfoFragment newInstance(String params) {
        QA$ListInfoFragment fragment = new QA$ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
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
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<String>(getActivity(), R.layout.item_info,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, String o, int position) {

            }
        };
    }
}
