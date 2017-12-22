package com.zhiyicx.thinksnsplus.modules.circle.publish.choose_circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * @author Jliuer
 * @Date 2017/11/28/14:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class ChooseCircleFragment extends TSFragment<ChooseCircleContract.Presenter> implements ChooseCircleContract.View {

    public static final String BUNDLE_CIRCLE = "circle";
    private static final int DEFAULT_COLUMN = 4;

    public static final int CHOOSE_CIRCLE = 1994;

    @BindView(R.id.fragment_channel_content_unsubscribe)
    RecyclerView mRvCircleList;

    private List<CircleInfo> mCircleInfos = new ArrayList<>();
    private CommonAdapter mAdapter;

    public static ChooseCircleFragment newInstance(Bundle bundle) {
        ChooseCircleFragment fragment = new ChooseCircleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_publish_add_category;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.select_circle);
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
        mRvCircleList.setLayoutManager(new GridLayoutManager(getActivity(),
                DEFAULT_COLUMN));
        mRvCircleList.setAdapter(initAdapter());
    }

    private CommonAdapter initAdapter() {
        mAdapter = new CommonAdapter<CircleInfo>(getActivity(),
                R.layout.item_info_channel, mCircleInfos) {
            @Override
            protected void convert(ViewHolder holder, CircleInfo data, int position) {
                holder.setText(R.id.item_info_channel, data.getName());
            }
        };

        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CircleInfo bean = mCircleInfos.get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_CIRCLE, bean);
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
    public CircleClient.MineCircleType getMineCircleType() {
        return CircleClient.MineCircleType.ALLOW;
    }

    @Override
    public void onNetResponseSuccess(List<CircleInfo> data) {
        if (data.isEmpty()) {
            startActivity(new Intent(getActivity(), CircleMainActivity.class));
            mActivity.finish();
            return;
        }
        mCircleInfos.clear();
        mCircleInfos.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
        mPresenter.getMyJoinedCircleList();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }
}
