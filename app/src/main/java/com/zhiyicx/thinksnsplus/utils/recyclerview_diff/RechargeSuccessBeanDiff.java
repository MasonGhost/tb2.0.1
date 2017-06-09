package com.zhiyicx.thinksnsplus.utils.recyclerview_diff;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/09/10:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class RechargeSuccessBeanDiff extends RecyclerViewDiffUtil<RechargeSuccessBean> {

    public RechargeSuccessBeanDiff(@NotNull List<RechargeSuccessBean> oldDatas, @NotNull List<RechargeSuccessBean> newDatas) {
        super(oldDatas, newDatas);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldDatas.get(oldItemPosition).getId() == mNewDatas.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItemPosition, newItemPosition);
    }
}
