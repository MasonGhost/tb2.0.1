package com.zhiyicx.zhibolibrary.presenter;

import android.support.v7.widget.RecyclerView;

import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.view.ListBaseView;

import java.util.ArrayList;

/**
 * Created by jess on 16/4/22.
 */
public abstract class ListBasePresenter<T, M, V extends ListBaseView> extends BasePresenter<M, V> {
    protected int mPage;
    protected ArrayList<T> mListDatas;
    protected RecyclerView.Adapter mAdapter;
    public static final int PAGE_LIMIT = 10;


    public ListBasePresenter(M model, V rootView) {
        super(model, rootView);
    }


    protected void prepare(boolean isMore) {
        if (!isMore) {
            mPage = 1;//如果刷新,PAGE默认为1
        }
        initAdapter();//初始化adapter
    }


    /**
     * 刷新数据
     *
     * @param apiList
     * @param isMore
     */
    protected void refresh(BaseJson<T[]> apiList, boolean isMore) {

        ++mPage;
        isShowMoreLoading(true);
        if (apiList.data.length == 0) {//没有数据
            isShowMoreLoading(false);
            if (!isMore) {//如果是上拉刷新，没有数据则清理以前的数据
                mListDatas.clear();
                mAdapter.notifyDataSetChanged();//通知页面更新数据
            }
            nonePrompt(isMore);
            return;
        }

        if (!isMore) {
            cleanStatus();
            mListDatas.clear();//如果是上拉刷新，清理之前的数据
            if (apiList.data.length < PAGE_LIMIT) {//第一次数据少于最大数量则不能加载更多
                isShowMoreLoading(false);
            }
        }
        else {
            if (apiList.data.length == 0)
                isShowMoreLoading(false);
        }
        for (Object data : apiList.data) {//添加数据
            mListDatas.add((T) data);
        }
        mAdapter.notifyDataSetChanged();//通知页面更新数据
    }

    protected void cleanStatus() {

    }

    /**
     * 加载时遇到网络状况不佳
     */
    protected void loadForNetBad() {
        if (mListDatas != null && mListDatas.size() > 0) {
            //如果列表有数据则清空
            mListDatas.clear();
            mAdapter.notifyDataSetChanged();//通知页面更新数据
        }
        mRootView.showNetBadPH();

    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mListDatas == null) {//如果list为空说明第一次拉取列表
            mListDatas = new ArrayList<>();
            mAdapter = getAdapter(mListDatas);
            setAdapter(mAdapter);
        }
    }

    /**
     * 获得适配器
     *
     * @return
     */
    public abstract RecyclerView.Adapter getAdapter(ArrayList<T> listDatas);

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public abstract void setAdapter(RecyclerView.Adapter adapter);


    /**
     * 没有数据提示信息
     *
     * @param isMore
     */
    public abstract void nonePrompt(boolean isMore);

    /**
     * 是否显示加载更多
     *
     * @param isShow
     */
    public abstract void isShowMoreLoading(boolean isShow);
}
