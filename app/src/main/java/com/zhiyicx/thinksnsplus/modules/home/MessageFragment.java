package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Describe 消息页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragment extends TSFragment {
    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;

    View mHeaderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
        mHeaderView= LayoutInflater.from(getActivity()).inflate(R.layout.header_message_list,null);
    }


    @Override
    protected void initData() {
        List<String> mDatas=new ArrayList<>();
        mRvMessageList.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item_message_list, mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        });
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

}
