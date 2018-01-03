package com.zhiyicx.thinksnsplus.modules.information.infochannel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchActivity;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.itemtouch.DefaultItemTouchHelpCallback;
import com.zhiyicx.thinksnsplus.widget.pager_recyclerview.itemtouch.DefaultItemTouchHelper;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment.SUBSCRIBE_EXTRA;

/**
 * @Author Jliuer
 * @Date 2017/03/06
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoChannelFragment extends TSFragment<InfoChannelConstract.Presenter>
        implements InfoChannelConstract.View {

    @BindView(R.id.fragment_channel_editor)
    TextView mFragmentChannelEditor;
    @BindView(R.id.fragment_channel_complete)
    TextView mFragmentChannelComplete;
    @BindView(R.id.fragment_channel_content_subscribed)
    RecyclerView mFragmentChannelContentSubscribed;
    @BindView(R.id.fragment_channel_content_unsubscribe)
    RecyclerView mFragmentChannelContentUnsubscribe;
    @BindView(R.id.info_prompt)
    TextView mInfoPrompt;

    private List<InfoTypeCatesBean> mMyCatesBeen;
    private List<InfoTypeCatesBean> mMoreCatesBeen;
    private CommonAdapter mSubscribeAdapter;
    private CommonAdapter mUnSubscribeAdapter;
    private boolean isEditor;
    private InfoTypeBean mInfoTypeBean;
    private DefaultItemTouchHelper mItemTouchHelper;
    public static final int RESULT_CODE = 100;

    public static InfoChannelFragment newInstance(Bundle params) {
        InfoChannelFragment fragment = new InfoChannelFragment();
        fragment.setArguments(params);
        return fragment;
    }

    private DefaultItemTouchHelpCallback.OnItemTouchCallbackListener onItemTouchCallbackListener =
            new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {

                @Override
                public void onSwiped(int adapterPosition) {

                }

                @Override
                public boolean onMove(int srcPosition, int targetPosition) {
                    if (mMyCatesBeen != null
                            && srcPosition != 0 && targetPosition != 0) {
                        // 更换数据源中的数据Item的位置
                        Collections.swap(mMyCatesBeen, srcPosition, targetPosition);
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
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setRightClick() {

        if (!TouristConfig.INFO_CAN_SEARCH && mPresenter.handleTouristControl()) {
            return;
        }
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    protected void setLeftClick() {
        backInfo();
    }

    @Override
    protected void initView(View rootView) {

        mFragmentChannelContentSubscribed.setLayoutManager(new GridLayoutManager(getActivity
                (), 4));
        mFragmentChannelContentUnsubscribe.setLayoutManager(new GridLayoutManager(getActivity(),
                4));

        mItemTouchHelper = new DefaultItemTouchHelper(
                onItemTouchCallbackListener);
        mItemTouchHelper.attachToRecyclerView(mFragmentChannelContentSubscribed);
        mItemTouchHelper.setDragEnable(true);
        mItemTouchHelper.setSwipeEnable(true);
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mInfoTypeBean = getArguments().getParcelable(BUNDLE_INFO_TYPE);
        }
        if (mInfoTypeBean != null) {
            mMyCatesBeen = mInfoTypeBean.getMy_cates();
            mMoreCatesBeen = mInfoTypeBean.getMore_cates();
        } else {
            mMyCatesBeen = new ArrayList<>();
            mMoreCatesBeen = new ArrayList<>();
        }

        mFragmentChannelContentSubscribed.setAdapter(initSubscribeAdapter());

        mFragmentChannelContentUnsubscribe.setAdapter(initUnsubscribeAdapter());
    }

    @OnClick({R.id.fragment_channel_editor, R.id.fragment_channel_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_channel_editor:
                mItemTouchHelper.setDragEnable(!isEditor);
                if (isEditor) {
                    mPresenter.doSubscribe(getFollows(mMyCatesBeen));
                    mInfoPrompt.setText(R.string.info_sort);
                    mFragmentChannelEditor.setText(getText(R.string.info_editor));
                } else {
                    mInfoPrompt.setText(R.string.info_delete);
                    mFragmentChannelEditor.setText(getText(R.string.complete));
                }
                isEditor = !isEditor;
                mSubscribeAdapter.notifyDataSetChanged();
                break;
            case R.id.fragment_channel_complete:
                InfoTypeBean infoTypeBean = new InfoTypeBean();
                infoTypeBean.setMore_cates(mMoreCatesBeen);
                infoTypeBean.setMy_cates(mMyCatesBeen);
                mPresenter.updateLocalInfoType(infoTypeBean);
                mPresenter.handleSubscribe(getFollows(mMyCatesBeen));
                mInfoTypeBean = infoTypeBean;
                backInfo();
                break;
            default:
        }
    }

    @Override
    public void setPresenter(InfoChannelConstract.Presenter presenter) {
        mPresenter = presenter;
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

    private void backInfo() {
        Intent intent = new Intent(getActivity(), InfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBSCRIBE_EXTRA, mInfoTypeBean);
        intent.putExtra(SUBSCRIBE_EXTRA, bundle);
        getActivity().setResult(RESULT_CODE, intent);
        getActivity().finish();
    }

    private String getFollows(List<InfoTypeCatesBean> bean) {
        StringBuilder ids = new StringBuilder();
        for (InfoTypeCatesBean data : bean) {
            if (data.getId() != -1) {
                ids.append(data.getId());
                ids.append(",");
            }
        }
        return ids.toString();
    }

    private CommonAdapter initUnsubscribeAdapter() {
        mUnSubscribeAdapter = new CommonAdapter<InfoTypeCatesBean>(getActivity(),
                R.layout.item_info_channel, mMoreCatesBeen) {
            @Override
            protected void convert(ViewHolder holder, InfoTypeCatesBean data,
                                   int position) {
                holder.setText(R.id.item_info_channel, data.getName());
            }
//
//            @Override
//            protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {
//                RxView.clicks(viewHolder.itemView)
//                        .throttleFirst(1, TimeUnit.SECONDS)
//                        .compose(bindToLifecycle())
//                        .subscribe(o -> {
//                            if (mOnItemClickListener != null) {
//                                int position = viewHolder.getAdapterPosition();
//                                mOnItemClickListener.onCommentTextClick(viewHolder.itemView, viewHolder, position);
//                            }
//                        });
//
//                viewHolder.itemView.setOnLongClickListener(v -> {
//                    if (mOnItemClickListener != null) {
//                        int position = viewHolder.getAdapterPosition();
//                        return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
//                    }
//                    return true;
//                });
//            }
        };

        mUnSubscribeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                InfoTypeCatesBean bean = mMoreCatesBeen.get(position);
                mSubscribeAdapter.addItem(new InfoTypeCatesBean(bean.getId(),
                        bean.getName(), true));
                mUnSubscribeAdapter.removeItem(position);
                if (!isEditor) {
                    mFragmentChannelEditor.performClick();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });

        return mUnSubscribeAdapter;
    }

    private CommonAdapter initSubscribeAdapter() {
        mSubscribeAdapter = new CommonAdapter<InfoTypeCatesBean>(getActivity(), R.layout
                .item_info_channel, mMyCatesBeen) {
            @Override
            protected void convert(ViewHolder holder, InfoTypeCatesBean data
                    , final int position) {
                TextView textView = holder.getView(R.id.item_info_channel);
                ImageView delete = holder.getView(R.id.item_info_channel_deal);

                if (position == 0) {
                    textView.setBackgroundResource(R.drawable.item_channel_bg_blue);
                    if (isEditor) {
                        textView.setBackgroundColor(Color.WHITE);
                    }
                } else {
                    textView.setBackgroundResource(R.drawable.item_channel_bg_normal);
                }
                if (isEditor && position != 0) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }
                holder.setText(R.id.item_info_channel, data.getName());
            }

//            @Override
//            protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {
//                RxView.clicks(viewHolder.itemView)
//                        .throttleFirst(1, TimeUnit.SECONDS)
//                        .compose(bindToLifecycle())
//                        .subscribe(o -> {
//                            if (mOnItemClickListener != null) {
//                                int position = viewHolder.getAdapterPosition();
//                                mOnItemClickListener.onCommentTextClick(viewHolder.itemView, viewHolder, position);
//                            }
//                        });
//
//                viewHolder.getConvertView().setOnLongClickListener(v -> {
//                    if (mOnItemClickListener != null) {
//                        int position = viewHolder.getAdapterPosition();
//                        return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
//                    }
//                    return true;
//                });
//            }
        };
        mSubscribeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (isEditor && position != 0) {
                    InfoTypeCatesBean bean = mMyCatesBeen.get(position);
                    mSubscribeAdapter.removeItem(position);
                    mUnSubscribeAdapter.addItem(new InfoTypeCatesBean(bean.getId(),
                            bean.getName(), false));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                if (!isEditor) {
                    mFragmentChannelEditor.performClick();
                }
                return false;
            }
        });

        return mSubscribeAdapter;
    }
}
