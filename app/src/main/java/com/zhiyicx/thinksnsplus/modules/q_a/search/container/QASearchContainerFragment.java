package com.zhiyicx.thinksnsplus.modules.q_a.search.container;

import android.os.Bundle;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class QASearchContainerFragment extends TSFragment<QASearchContainerContract.Presenter> implements QASearchContainerContract.View {


    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;

    private QASearchContainerViewPagerFragment mFindSomeOneContainerViewPagerFragment;

    public static QASearchContainerFragment newInstance(Bundle bundle) {
        QASearchContainerFragment findSomeOneContainerFragment = new QASearchContainerFragment();
        findSomeOneContainerFragment.setArguments(bundle);
        return findSomeOneContainerFragment;

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_qa_search_container;
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
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mFindSomeOneContainerViewPagerFragment = QASearchContainerViewPagerFragment.initFragment(getActivity().getIntent().getExtras());

        ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager()
                , mFindSomeOneContainerViewPagerFragment
                , R.id.fragment_container);

        initListener();


    }

    @Override
    protected void initData() {


    }

    private void initListener() {

        RxTextView.afterTextChangeEvents(mFragmentInfoSearchEdittext)
                .compose(this.bindToLifecycle())
                .subscribe(textViewAfterTextChangeEvent -> {
                    mFindSomeOneContainerViewPagerFragment.onSearhChanged(textViewAfterTextChangeEvent.editable().toString());
                });

    }


    @OnClick({R.id.fragment_search_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_cancle:
                getActivity().finish();
                break;
            default:
        }


    }

}
