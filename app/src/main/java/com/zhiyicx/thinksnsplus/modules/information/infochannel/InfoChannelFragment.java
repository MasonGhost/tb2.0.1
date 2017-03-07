package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.information.SearchActivity;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.itemtouch.DefaultItemTouchHelpCallback;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.itemtouch.DefaultItemTouchHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoChannelFragment extends TSFragment {

    @BindView(R.id.fragment_channel_editor)
    TextView mFragmentChannelEditor;
    @BindView(R.id.fragment_channel_complete)
    TextView mFragmentChannelComplete;
    @BindView(R.id.fragment_channel_content_subscribed)
    RecyclerView mFragmentChannelContentSubscribed;
    @BindView(R.id.fragment_channel_content_unsubscribe)
    RecyclerView mFragmentChannelContentUnsubscribe;

    private List<String> mStringList = new ArrayList<>();
    private List<String> mStringList1 = new ArrayList<>();
    private CommonAdapter mSubscribeAdapter;
    private CommonAdapter mUnSubscribeAdapter;
    private boolean isEditor;

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener =
            new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {

                @Override
                public void onSwiped(int adapterPosition) {
                    // 滑动删除的时候，从数据源移除，并刷新这个Item。
//            if (mDatas != null) {
//                mDatas.remove(adapterPosition);
//                adapter.notifyItemRemoved(adapterPosition);
//            }
                }

                @Override
                public boolean onMove(int srcPosition, int targetPosition) {
                    if (mStringList != null
                            && targetPosition != mStringList.size() - 1
                            && !isEditor) {
                        // 更换数据源中的数据Item的位置
                        Collections.swap(mStringList, srcPosition, targetPosition);
                        // 更新UI中的Item的位置，主要是给用户看到交互效果
                        mSubscribeAdapter.notifyItemMoved(srcPosition, targetPosition);
                        return true;
                    }
                    return false;
                }
            };

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_info_channel;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.information);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    protected void initView(View rootView) {
        mStringList.clear();
        for (int i = 0; i < 7; i++) {
            mStringList.add("");
            mStringList1.add("");
        }

        mFragmentChannelContentSubscribed.setLayoutManager(new GridLayoutManager(getActivity
                (), 4));
        mFragmentChannelContentUnsubscribe.setLayoutManager(new GridLayoutManager(getActivity(),
                4));

        DefaultItemTouchHelper itemTouchHelper = new DefaultItemTouchHelper(
                onItemTouchCallbackListener);
        itemTouchHelper.attachToRecyclerView(mFragmentChannelContentSubscribed);
        itemTouchHelper.setDragEnable(true);
        itemTouchHelper.setSwipeEnable(true);

        mSubscribeAdapter = new CommonAdapter<String>(getActivity(), R.layout
                .item_info_channel, mStringList) {
            @Override
            protected void convert(ViewHolder holder, String s, final int position) {
                ImageView delete = holder.getView(R.id.item_info_channel_deal);
                if (isEditor) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSubscribeAdapter.removeItem(position);
                    }
                });
            }
        };
        mFragmentChannelContentSubscribed.setAdapter(mSubscribeAdapter);

        mUnSubscribeAdapter = new CommonAdapter<String>(getActivity(), R.layout
                .item_info_channel, mStringList1) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        mFragmentChannelContentUnsubscribe.setAdapter(mUnSubscribeAdapter);

        mUnSubscribeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mSubscribeAdapter.addItem(mStringList1.get(position), 0);
                mUnSubscribeAdapter.removeItem(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.fragment_channel_editor, R.id.fragment_channel_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_channel_editor:
                isEditor = true;
                mSubscribeAdapter.notifyDataSetChanged();
                break;
            case R.id.fragment_channel_complete:
                isEditor = false;
                mSubscribeAdapter.notifyDataSetChanged();
                break;
        }
    }

}
