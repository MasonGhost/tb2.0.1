package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/12/26
 * @Contact master.jungle68@gmail.com
 */
public interface IStickTopRepository {
    Observable<BaseJsonV2<Integer>> stickTop(String type, long parent_id, double amount, int day);
    Observable<BaseJsonV2<Integer>> stickTop(String type,long parent_id,long child_id, double amount, int day);
}
