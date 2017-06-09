package com.zhiyicx.thinksnsplus.utils.recyclerview_diff;


import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/06/16:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicBeanDiff extends RecyclerViewDiffUtil<DynamicBean> {

    private DynamicBeanDiff(@NotNull List<DynamicBean> oldDatas, @NotNull List<DynamicBean> newDatas) {
        super(oldDatas, newDatas);
    }

    public static DynamicBeanDiff getInstance( List<DynamicBean> oldDatas,  List<DynamicBean> newDatas){

        return new DynamicBeanDiff(oldDatas,newDatas);
    }

    /**
     * 是否同一个item
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getFeed_id().equals(mNewDatas.get(newItemPosition).getFeed_id());
    }

    /**
     * 这个方法替代equals方法去检查是否相等
     * 仅在areItemsTheSame()返回true时，才调用
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DynamicBean oldDynamic = mOldDatas.get(oldItemPosition);
        DynamicBean newDynamic = mNewDatas.get(newItemPosition);
        return false;
    }

    @Override
    protected RecyclerViewDiffUtil getCurrentDiffUtil() {
        return null;
    }
}
