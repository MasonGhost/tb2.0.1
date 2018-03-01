package com.zhiyicx.thinksnsplus.modules.tb.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jungle68
 * @describe 用户搜索界面
 * @date 2017/1/9
 * @contact master.jungle68@gmail.com
 */
public class SearchMechanismUserFragment extends TSListFragment<SearchMechanismUserContract.Presenter, UserInfoBean> implements
        SearchMechanismUserContract.View,
        MultiItemTypeAdapter.OnItemClickListener {

    protected static final int DEFAULT_FIRST_SHOW_HISTORY_SIZE = 5;

    @Inject
    SearchMechanismUserPresenter mSearchMechanismUserPresenter;

    @BindView(R.id.rv_search_history)
    RecyclerView mRvSearchHistory;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;

    @BindView(R.id.fragment_search_cancle)
    TextView mTvSearchCancel;

    private MultiItemTypeAdapter mHsitoryAdapter;
    private List<SearchHistoryBean> mHistoryData = new ArrayList<>();

    public static SearchMechanismUserFragment newInstance() {
        SearchMechanismUserFragment fragment = new SearchMechanismUserFragment();
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_mehsnism_user_search;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected View getRightViewOfMusicWindow() {
        return mTvSearchCancel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSearchMechanismUserComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .searchMechanismUserPresenterModule(new SearchMechanismUserPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setEmptyViewVisiable(false);
        RxTextView.editorActionEvents(mFragmentInfoSearchEdittext).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                mPresenter.searchUser(mFragmentInfoSearchEdittext.getText().toString());
                DeviceUtils.hideSoftKeyboard(getContext(), mFragmentInfoSearchEdittext);
                // 请求网络数据，就隐藏历史
                mRvSearchHistory.setVisibility(View.GONE);
            }
        });
        mRvList.setBackgroundResource(R.color.white);
//        RxTextView.afterTextChangeEvents(mFragmentInfoSearchEdittext)
//                .subscribe(textViewAfterTextChangeEvent -> {
//                    mPresenter.searchUser(textViewAfterTextChangeEvent.editable().toString());
//                });
    }

    @Override
    protected void initData() {
        super.initData();
        initHistoryView();
    }

    private void initHistoryView() {
        mHistoryData.addAll(mPresenter.getFirstShowHistory());
        if (mHistoryData.size() >= DEFAULT_FIRST_SHOW_HISTORY_SIZE) {
            mHistoryData.add(new SearchHistoryBean(getString(R.string.show_all_history), SearchHistoryBean.TYPE_DEFAULT));
        }
        mRvSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvSearchHistory.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), getItemDecorationSpacing()), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvSearchHistory.setHasFixedSize(sethasFixedSize());
        mRvSearchHistory.setItemAnimator(new DefaultItemAnimator());//设置动画
        getHistoryAdapter();
        mRvSearchHistory.setAdapter(mHsitoryAdapter);
        refreshHistory();
    }

    private void refreshHistory() {
        mHsitoryAdapter.notifyDataSetChanged();
        if (mHistoryData.isEmpty()) {
            mRvSearchHistory.setVisibility(View.GONE);
        } else {
            mRvSearchHistory.setVisibility(View.VISIBLE);
        }
    }


    public void getHistoryAdapter() {
        mHsitoryAdapter = new MultiItemTypeAdapter<>(getContext(), mHistoryData);
        mHsitoryAdapter.addItemViewDelegate(new ItemViewDelegate<SearchHistoryBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_qa_search_history_list;
            }

            @Override
            public boolean isForViewType(SearchHistoryBean item, int position) {
                return !SearchHistoryBean.TYPE_DEFAULT.equals(item.getType());
            }

            @Override
            public void convert(ViewHolder holder, SearchHistoryBean qaSearchHistoryBean, SearchHistoryBean lastT, int position, int
                    itemCounts) {
                holder.setText(R.id.tv_content, qaSearchHistoryBean.getContent());
                RxView.clicks(holder.getView(R.id.tv_content))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            // 搜索
                            mFragmentInfoSearchEdittext.setText(qaSearchHistoryBean.getContent());
                            mPresenter.searchUser(mFragmentInfoSearchEdittext.getText().toString());
                            DeviceUtils.hideSoftKeyboard(mActivity.getApplicationContext(), holder.getView(R.id.tv_content));
                            // 请求网络数据，就隐藏历史
                            mRvSearchHistory.setVisibility(View.GONE);
                        });
                RxView.clicks(holder.getView(R.id.iv_delete))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            mPresenter.deleteSearchHistory(mHistoryData.get(position));
                            mHistoryData.remove(position);
                            mHsitoryAdapter.notifyItemRemoved(position);
                            mHsitoryAdapter.notifyDataSetChanged();

                        });
            }
        });
        mHsitoryAdapter.addItemViewDelegate(new ItemViewDelegate<SearchHistoryBean>() {
            @Override
            public int getItemViewLayoutId() {
                return R.layout.item_qa_search_history_cotrol;
            }

            @Override
            public boolean isForViewType(SearchHistoryBean item, int position) {
                return SearchHistoryBean.TYPE_DEFAULT.equals(item.getType());

            }

            @Override
            public void convert(ViewHolder holder, SearchHistoryBean o, SearchHistoryBean lastT, int position, int itemCounts) {
                holder.setText(R.id.tv_content, o.getContent());
                holder.setTextColor(R.id.tv_content, getColor(R.color.themeColor));
                RxView.clicks(holder.getView(R.id.tv_content))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> {
                            if (o.getContent().equals(getString(R.string.show_all_history))) { // 显示所有历史
                                mHistoryData.clear();
                                mHistoryData.addAll(mPresenter.getAllSearchHistory());
                                mHistoryData.add(new SearchHistoryBean(getString(R.string.clear_all_history), SearchHistoryBean
                                        .TYPE_DEFAULT));
                                mHsitoryAdapter.notifyDataSetChanged();

                            } else { // 清空历史
                                mHistoryData.clear();
                                mPresenter.cleaerAllSearchHistory();
                                mHsitoryAdapter.notifyDataSetChanged();

                            }
                        });
            }
        });
    }


    @OnClick({R.id.fragment_search_back, R.id.fragment_search_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_back:
                getActivity().finish();
                break;
            case R.id.fragment_search_cancle:
                getActivity().finish();
                break;
            default:
        }
    }

    @Override
    protected CommonAdapter getAdapter() {
        CommonAdapter adapter = new SearchMechainsimUserListAdapter(getActivity(), R.layout.item_search_mechainsim_user_list, mListDatas, mPresenter);
        adapter.setOnItemClickListener(this);
        return adapter;

    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_search;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//
//        UserInfoBean bean = mListDatas.get(position);
//        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(BUNDLE_DATA, bean);
//        intent.putExtras(bundle);
//        getActivity().setResult(RESULT_OK, intent);
//        getActivity().finish();
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }
}
