package com.zhiyicx.zhibolibrary.ui.fragment;


import android.os.Bundle;
import android.view.View;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.util.UiUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmptyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmptyFragment extends ZBLBaseFragment {
    private static final String ARG_PARAM1 = "param1";

    public EmptyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmptyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmptyFragment newInstance(String param1) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView() {
        return UiUtils.inflate(R.layout.zb_fragment_empty);
    }

    @Override
    protected void initData() {

    }

}
