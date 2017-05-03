package com.zhiyicx.thinksnsplus.modules.music_fm.music_play;

import android.graphics.Bitmap;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.thinksnsplus.R;

import javax.inject.Inject;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;

/**
 * @Author Jliuer
 * @Date 2017/02/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MusicPlayPresenter extends BasePresenter<MusicPlayContract.Repository,
        MusicPlayContract.View> implements MusicPlayContract.Presenter, OnShareCallbackListener {

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
    public void shareMusic(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(mRootView.getCurrentMusic().getMusic_info().getTitle());
        shareContent.setContent(mRootView.getCurrentMusic().getMusic_info().getLyric());
        shareContent.setUrl(String.format(ApiConfig.NO_PROCESS_IMAGE_PATH,
                mRootView.getCurrentMusic().getMusic_info().getStorage()));
        if (bitmap==null){
            shareContent.setResImage(R.mipmap.icon_256);
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
        mRepository.shareMusic(mRootView.getCurrentMusic().getMusic_info().getId() + "");
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
