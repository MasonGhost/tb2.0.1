package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.MusicAlbumDetailsBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.WalletActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPlayPresenter extends AppBasePresenter<MusicPlayContract.Repository,
        MusicPlayContract.View> implements MusicPlayContract.Presenter, OnShareCallbackListener {

    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    CommentRepository mCommentRepository;
    @Inject
    MusicAlbumDetailsBeanGreenDaoImpl mMusicAlbumDetailsBeanGreenDao;

    @Inject
    public MusicPlayPresenter(MusicPlayContract.Repository repository, MusicPlayContract
            .View rootView) {
        super(repository, rootView);
    }

    /**
     * 将Presenter从传入fragment
     */
    @Inject
    void setupListeners() {
        mRootView.setPresenter(this);
    }

    @Inject
    public SharePolicy mSharePolicy;

    @Override
    protected boolean useEventBus() {
        return true;
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

//        mRootView.getListDatas().get(position).getStorage().setPaid(true);
//        mRootView.getCurrentAblum().getMusics().get(position).getStorage().setPaid(true);
//        mRootView.refreshData(position);
//        EventBus.getDefault().post(mRootView.getListDatas().get(position), EventBusTagConfig
//                .EVENT_MUSIC_TOLL);
//        mMusicAlbumDetailsBeanGreenDao.insertOrReplace(mRootView.getCurrentAblum());
//        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));

        mCommentRepository.paykNote(note)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.transaction_doing)))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.getListDatas().get(position).getStorage().setPaid(true);
                        mRootView.getCurrentAblum().getMusics().get(position).getStorage().setPaid(true);
                        mRootView.refreshData(position);
                        EventBus.getDefault().post(mRootView.getListDatas().get(position), EventBusTagConfig.EVENT_MUSIC_TOLL);
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
                });
    }

    @Override
    public void shareMusic(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentMusic().getTitle());
        shareContent.setContent(mRootView.getCurrentMusic().getLyric());
        shareContent.setUrl(ImageUtils.imagePathConvertV2(mRootView.getCurrentMusic().getStorage().getId(),0,0, ImageZipConfig.IMAGE_50_ZIP));
        if (bitmap==null){
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.icon)));
        }else{
            shareContent.setBitmap(bitmap);
        }
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRepository.shareMusic(mRootView.getCurrentMusic().getId() + "");
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
    public void handleLike(boolean isLiked, String music_id) {
        mRepository.handleLike(isLiked, music_id);
    }
}
