package com.zhiyicx.thinksnsplus.modules.findsomeone.contianer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.findsomeone.search.name.SearchSomeOneActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneContainerFragment extends TSFragment {


    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;

    public static FindSomeOneContainerFragment newInstance(Bundle bundle) {
        FindSomeOneContainerFragment findSomeOneContainerFragment = new FindSomeOneContainerFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_find_some_container;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        initListener();
    }

    @Override
    protected void initData() {
        ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager()
                , FindSomeOneContainerViewPagerFragment.initFragment(getActivity().getIntent().getExtras())
                , R.id.fragment_container);


    }

    private void initListener() {
    }


    @OnClick({R.id.tv_toolbar_center, R.id.tv_toolbar_right_two, R.id.tv_toolbar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_center:
                startActivity(new Intent(getActivity(),SearchSomeOneActivity.class));

                break;
            case R.id.tv_toolbar_right_two:
                break;
            case R.id.tv_toolbar_right:
                break;
        }
    }
}
