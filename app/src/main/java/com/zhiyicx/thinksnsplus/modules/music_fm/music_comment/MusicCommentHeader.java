package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description 歌曲评论头部信息
 */
public class MusicCommentHeader {
    private View mMusicCommentHeader;
    private Context mContext;

    private TextView mTitle;
    private TextView mListenCount;
    private TextView mCommentCount;
    private ImageView mHeaderImag;
    private ImageLoader mImageLoader;

    public MusicCommentHeader(Context context) {
        this.mContext = context;
        mMusicCommentHeader = LayoutInflater.from(context).inflate(R.layout
                .view_header_music_comment, null);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMusicCommentHeader.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTitle = (TextView) mMusicCommentHeader.findViewById(R.id.head_info_music_title);
        mListenCount = (TextView) mMusicCommentHeader.findViewById(R.id.head_info_music_listen);
        mCommentCount = (TextView) mMusicCommentHeader.findViewById(R.id.tv_comment_count);
        mHeaderImag = (ImageView) mMusicCommentHeader.findViewById(R.id.head_info_music_comment);
    }

    public View getMusicCommentHeader() {
        return mMusicCommentHeader;
    }

    public void setHeadInfo(MusicAlbumDetailsBean.MusicsBean musicsBean) {
        mTitle.setText(musicsBean.getMusic_info().getTitle());
        mListenCount.setText(musicsBean.getMusic_info().getTaste_count()+"");
        String url = String.format(ApiConfig.IMAGE_PATH,
                musicsBean.getMusic_info().getSinger().getCover().getId(), 50);
        LogUtils.d(url);
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .imagerView(mHeaderImag)
                .url(url)
                .build());
        setCommentList(0);
    }

    public void setCommentList(int size){
        mCommentCount.setText(mContext.getResources().getString(R.string.dynamic_comment_count,size));
    }
}
