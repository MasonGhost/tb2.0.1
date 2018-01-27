package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_list;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMusicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Author Jliuer
 * @Date 2017/02/13
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPresenter extends AppBasePresenter<MusicContract.View>
        implements MusicContract.Presenter {

    @Inject
    MusicAlbumListBeanGreenDaoImpl mMusicAlbumListDao;
    @Inject
    BaseMusicRepository mBaseMusicRepository;

    @Inject
    public MusicPresenter(MusicContract.View rootView) {
        super(rootView);
    }

    @Override
    public void payNote(int position, int note) {
        if (handleTouristControl()) {
            return;
        }
        double amount = mRootView.getListDatas().get(position).getPaid_node().getAmount();

        Subscription subscribe = handleIntegrationBlance((long) amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.transaction_doing)))
                .flatMap(o -> mCommentRepository.paykNote(note))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.transaction_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.getListDatas().get(position).getPaid_node().setPaid(true);
                        mRootView.refreshData(position);
                        mMusicAlbumListDao.insertOrReplace(mRootView.getListDatas().get(position));
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription subscription = mBaseMusicRepository.getMusicAblumList(maxId)
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<List<MusicAlbumListBean>>() {
                    @Override
                    protected void onSuccess(List<MusicAlbumListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }


                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(mBaseMusicRepository.getMusicAlbumFromCache(maxId), isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MusicAlbumListBean> data, boolean isLoadMore) {
        if (data.isEmpty()) {
            mMusicAlbumListDao.saveMultiData(data);
        } else {
            mMusicAlbumListDao.clearTable();
        }
        return false;
    }

    @Override
    public void updateOneMusic(MusicAlbumListBean albumListBean) {
        mMusicAlbumListDao.updateSingleData(albumListBean);
    }
}
