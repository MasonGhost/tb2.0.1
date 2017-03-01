package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForContent;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.adapter.DynamicDetailItemForDig;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public class DynamicDetailFragment extends TSListFragment<DynamicDetailContract.Presenter, DynamicBean> implements DynamicDetailContract.View {
    public static final String DYNAMIC_DETAIL_DATA = "dynamic_detail_data";

    @BindView(R.id.dd_dynamic_tool)
    DynamicDetailMenuView mDdDynamicTool;

    private List<DynamicBean> mDatas = new ArrayList<>();

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_dynamic_detail;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mDdDynamicTool.setImageNormalResourceIds(new int[]{R.mipmap.home_ico_good_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.detail_ico_good_uncollect
        });
        mDdDynamicTool.setImageCheckedResourceIds(new int[]{R.mipmap.home_ico_good_normal
                , R.mipmap.home_ico_comment_normal, R.mipmap.detail_ico_share_normal
                , R.mipmap.detail_ico_collect
        });
        mDdDynamicTool.setButtonText(new int[]{R.string.dynamic_like, R.string.comment
                , R.string.share, R.string.favorite});
    }

    @Override
    protected void initData() {
        // 处理上个页面传过来的动态数据
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(DYNAMIC_DETAIL_DATA)) {
            DynamicBean dynamicBean = bundle.getParcelable(DYNAMIC_DETAIL_DATA);
            DynamicBean dynamicContent = new DynamicBean();
            dynamicContent.setFeed(dynamicBean.getFeed());
            DynamicBean dynamicDig = new DynamicBean();
            dynamicDig.setFeed(dynamicBean.getFeed());
            dynamicDig.setTool(dynamicBean.getTool());
            mDatas.add(dynamicContent);
            mDatas.add(dynamicDig);
            refreshData();
        }
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected MultiItemTypeAdapter<DynamicBean> getAdapter() {
        MultiItemTypeAdapter<DynamicBean> adapter = new MultiItemTypeAdapter<>(getContext(), mDatas);
        adapter.addItemViewDelegate(new DynamicDetailItemForContent());
        adapter.addItemViewDelegate(new DynamicDetailItemForDig());
        return adapter;
    }

    @Override
    public void setPresenter(DynamicDetailContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    public static DynamicDetailFragment initFragment(Bundle bundle) {
        DynamicDetailFragment dynamicDetailFragment = new DynamicDetailFragment();
        dynamicDetailFragment.setArguments(bundle);
        return dynamicDetailFragment;
    }

}
