package com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumDetailsBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.MusicDetailRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_DEFAULT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_ABLUM_COLLECT;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description 专辑详情
 */
@FragmentScoped
public class MusicDetailPresenter extends AppBasePresenter<
        MusicDetailContract.View> implements MusicDetailContract.Presenter, OnShareCallbackListener {

    @Inject
    MusicDetailRepository mMusicDetailRepository;

    @Inject
    MusicAlbumDetailsBeanGreenDaoImpl mMusicAlbumDetailsBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    CommentRepository mCommentRepository;

    @Inject
    public SharePolicy mSharePolicy;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Inject
    public MusicDetailPresenter(MusicDetailContract
                                        .View rootView) {
        super(rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Override
    public void payNote(int position, int note) {
        WalletBean walletBean = mWalletBeanGreenDao.getSingleDataByUserId(AppApplication.getmCurrentLoginAuth().getUser_id());
        double balance = 0;
        if (walletBean != null) {
            balance = walletBean.getBalance();
        }
        double amount;
        amount = mRootView.getListDatas().get(position).getStorage().getAmount();

        if (balance < amount) {
            mRootView.goRecharge(WalletActivity.class);
            return;
        }
        Subscription subscribe = mCommentRepository.paykNote(note)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.transaction_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.getListDatas().get(position).getStorage().setPaid(true);
                        mRootView.getCurrentAblum().getMusics().get(position).getStorage().setPaid(true);
                        mRootView.refreshData(position);
                        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(mRootView.getCurrentAblum());
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
    public void getMusicAblum(String id) {
        Subscription subscribe = mMusicDetailRepository.getMusicAblum(id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<MusicAlbumDetailsBean>() {
                    @Override
                    protected void onSuccess(MusicAlbumDetailsBean data) {
                        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(data);
                        mRootView.setMusicAblum(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.albumHasBeDeleted();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.noNetWork();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void getMusicDetails(String music_id) {
        mMusicDetailRepository.getMusicDetails(music_id).compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<MusicDetaisBean>() {
                    @Override
                    protected void onSuccess(MusicDetaisBean data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    public void handleCollect(boolean isUnCollected, String special_id) {

        boolean is_collect = !mRootView.getCurrentAblum().getHas_collect();
        mRootView.getCurrentAblum().setHas_collect(is_collect);
        mRootView.getmMusicAlbumListBean().setHas_collect(is_collect);
        int countChange = isUnCollected ? 1 : -1;
        mRootView.getmMusicAlbumListBean().setCollect_count(mRootView.getCurrentAblum().getCollect_count() + countChange);
        mRootView.getCurrentAblum().setCollect_count(mRootView.getCurrentAblum().getCollect_count() + countChange);
        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(mRootView.getCurrentAblum());
        EventBus.getDefault().post(mRootView.getmMusicAlbumListBean(), EVENT_ABLUM_COLLECT);
        mRootView.setCollect(isUnCollected);
        mMusicDetailRepository.handleCollect(isUnCollected, special_id);

    }

    @Override
    public void shareMusicAlbum(Bitmap bitmap) {

        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();

        shareContent.setTitle(mRootView.getCurrentAblum() == null ? mContext.getString(R.string.unknown) : mRootView.getCurrentAblum().getTitle());
        shareContent.setContent(mRootView.getCurrentAblum() == null ? mContext.getString(R.string.unknown) : mRootView.getCurrentAblum().getIntro());
        if (bitmap == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        } else {
            shareContent.setBitmap(bitmap);
        }
        shareContent.setUrl(ApiConfig.APP_DOMAIN + APP_PATH_SHARE_DEFAULT);

        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        if (!isTourist()) {
            mMusicDetailRepository.shareAblum(mRootView.getCurrentAblum().getId() + "");
        }
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Override
    public MusicAlbumDetailsBean getCacheAblumDetail(int id) {
        return mMusicAlbumDetailsBeanGreenDao.getAblumByID(id);
    }
}
