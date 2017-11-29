package com.zhiyicx.thinksnsplus.modules.live.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLStarExchangeActivity;
import com.zhiyicx.zhibolibrary.ui.fragment.ZBLUserVideoFragment;

/**
 * @author LiuChao
 * @describe 粉丝和关注列表
 * @date 2017/2/13
 * @contact email:450127106@qq.com
 */

public class MineLiveFragment extends ZBLUserVideoFragment implements View.OnClickListener {
    public static final String BUNDLE_USID = "usid";


    public static MineLiveFragment newInstance(Bundle bundle) {
        MineLiveFragment fragment = new MineLiveFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String usid = getArguments().getString(BUNDLE_USID);
            setUsid(usid);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mRootView.findViewById(com.zhiyicx.zhibolibrary.R.id.rl_back).setOnClickListener(this);
        mRootView.findViewById(com.zhiyicx.zhibolibrary.R.id.rl_right).setOnClickListener(this);
        ((TextView)mRootView.findViewById(com.zhiyicx.zhibolibrary.R.id.toolbar_title)).setText(getString(R.string.mine_live));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.zhiyicx.zhibolibrary.R.id.rl_back) {
            getActivity().finish();
        } else if (v.getId() == com.zhiyicx.zhibolibrary.R.id.rl_right) {
            Intent intent = new Intent(getActivity(), ZBLStarExchangeActivity.class);
            startActivity(intent);
        }
    }
}
