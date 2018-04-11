package com.zhiyicx.thinksnsplus.widget;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public abstract class EmptyItem<T> implements ItemViewDelegate<T> {

    private int mEmptyView;
    private int mHeight = -1;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment_empty;
    }

    @Override
    public abstract boolean isForViewType(T item, int position);

    @Override
    public void convert(ViewHolder holder, T baseListBean, T lastT, int position,int itemCounts) {
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setNeedTextTip(false);
        emptyView.setErrorType(EmptyView.STATE_NODATA);
        emptyView.setErrorImag(getEmptyView());
        if(mHeight != -1){
            emptyView.getLayoutParams().height = getHeight();
        }
    }

    protected void setEmptView(int emptyView){
        this.mEmptyView = emptyView;
    }

    private int getEmptyView(){
        return mEmptyView;
    }

    protected void setHeight(int height){
        this.mHeight = height;
    }

    private int getHeight(){
        return this.mHeight;
    }

}
