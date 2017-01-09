package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
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
    ImageLoader mImageLoader;

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
    @Override
    protected void initView(View rootView) {
        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
        mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.header_message_list, null);
    }


    @Override
    protected void initData() {
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        List<String> mDatas = new ArrayList<>();
        mDatas.add("nihao"); // 测试数据，暂时使用
        mDatas.add("nihao1");
        mDatas.add("nihao2");
        mDatas.add("nihao3");
        mRvMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMessageList.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), 10f), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvMessageList.setHasFixedSize(true);
        mRvMessageList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRvMessageList.setAdapter(new CommonAdapter<String>(getActivity(), R.layout.item_message_list, mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                Glide.with(getContext()).load(R.mipmap.ico_eye_open).into((ImageView) holder.getView(R.id.iv_headpic));
//                mImageLoader.loadImage(getContext(), GlideImageConfig.builder());
                holder.setText(R.id.tv_name, "张三");
                holder.setText(R.id.tv_content, "我的天的道德观念我的是高科技的思考国际快递发几个客服房间打开数据库");
                holder.setText(R.id.tv_time, "2016-05-06");
                holder.setText(R.id.tv_tip, "109");
            }
        });
    }


}
