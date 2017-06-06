package com.zhiyicx.thinksnsplus.utils.recyclerview_diff;

import android.support.v7.util.DiffUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/06/15:54
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class RecyclerViewDiffUtil<T> extends DiffUtil.Callback {

    protected List<T> mOldDatas, mNewDatas;

    public RecyclerViewDiffUtil(@NotNull List<T> oldDatas,@NotNull List<T> newDatas) {
        this.mOldDatas = oldDatas;
        this.mNewDatas = newDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return mNewDatas.size();
    }
}
