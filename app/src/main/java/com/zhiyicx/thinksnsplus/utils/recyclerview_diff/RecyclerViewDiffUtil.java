package com.zhiyicx.thinksnsplus.utils.recyclerview_diff;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/06/06/15:54
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class RecyclerViewDiffUtil<T> extends DiffUtil.Callback {

    protected List<T> mOldDatas, mNewDatas;

    public RecyclerViewDiffUtil(@NotNull List<T> oldDatas, @NotNull List<T> newDatas) {
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

    protected RecyclerViewDiffUtil getCurrentDiffUtil() {
        return this;
    }

    public void diffNotify(@NotNull final RecyclerView.Adapter adapter, final boolean detectMoves) {
        Observable<DiffUtil.DiffResult> observable = Observable.defer(new Func0<Observable<DiffUtil.DiffResult>>() {
            @Override
            public Observable<DiffUtil.DiffResult> call() {
                return Observable.just(DiffUtil.calculateDiff(getCurrentDiffUtil(), detectMoves));
            }
        });
        observable.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DiffUtil.DiffResult>() {
                    @Override
                    public void call(DiffUtil.DiffResult diffResult) {
                        diffResult.dispatchUpdatesTo(adapter);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }
}
