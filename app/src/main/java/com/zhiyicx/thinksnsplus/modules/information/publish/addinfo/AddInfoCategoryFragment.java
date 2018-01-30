package com.zhiyicx.thinksnsplus.modules.information.publish.addinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * @Describe 完善文章信息界面
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class AddInfoCategoryFragment extends TSFragment<AddInfoContract.Presenter> implements AddInfoContract.View {

    public static final String BUNDLE_PUBLISH_CATEGORY = "publish_category";
    private static final int DEFAULT_COLUMN = 4;

    @BindView(R.id.fragment_channel_content_unsubscribe)
    RecyclerView mFragmentChannelContentUnsubscribe;

    private List<InfoTypeCatesBean> mMoreCatesBeen;
    private CommonAdapter mAdapter;

    public static AddInfoCategoryFragment newInstance(Bundle bundle) {

        AddInfoCategoryFragment fragment = new AddInfoCategoryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_publish_add_category;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.info_choose_categorys);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mFragmentChannelContentUnsubscribe.setLayoutManager(new GridLayoutManager(getActivity(),
                DEFAULT_COLUMN));
    }

    private CommonAdapter initUnsubscribeAdapter() {
        mAdapter = new CommonAdapter<InfoTypeCatesBean>(getActivity(),
                R.layout.item_info_channel, mMoreCatesBeen) {
            @Override
            protected void convert(ViewHolder holder, InfoTypeCatesBean data,
                                   int position) {
                holder.setText(R.id.item_info_channel, data.getName());
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                InfoTypeCatesBean bean = mMoreCatesBeen.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_PUBLISH_CATEGORY, bean);
                intent.putExtras(bundle);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mAdapter;
    }

    @Override
    public void setInfoType(List<InfoTypeCatesBean> infoType) {
        mMoreCatesBeen.clear();
        mMoreCatesBeen.addAll(infoType);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        mMoreCatesBeen = mPresenter.getInfoTypeBean();
        mPresenter.getInfoType();
        mFragmentChannelContentUnsubscribe.setAdapter(initUnsubscribeAdapter());
    }

}
