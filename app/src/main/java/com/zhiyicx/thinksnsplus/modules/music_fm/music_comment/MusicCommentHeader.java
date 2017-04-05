package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;

import java.io.Serializable;

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
        if (musicsBean == null)
            return;
        mTitle.setText(musicsBean.getMusic_info().getTitle());
        mListenCount.setText(musicsBean.getMusic_info().getTaste_count() + "");
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .imagerView(mHeaderImag)
                .url(ImageUtils.imagePathConvert(musicsBean.getMusic_info().getSinger().getCover().getId() + "",
                        ImageZipConfig.IMAGE_70_ZIP))
                .build());
    }

    public void setHeadInfo(HeaderInfo headInfo) {
        if (headInfo == null)
            return;
        mTitle.setText(headInfo.getTitle());
        mListenCount.setText(headInfo.getLitenerCount());
        mImageLoader.loadImage(mContext, GlideImageConfig.builder()
                .imagerView(mHeaderImag)
                .url(headInfo.getImageUrl())
                .build());
    }

    public void setCommentList(int size) {
        mCommentCount.setText(mContext.getResources().getString(R.string.dynamic_comment_count, "" + size));
    }

    public static class HeaderInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private int id;
        private String title;
        private String litenerCount;
        private String imageUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLitenerCount() {
            return litenerCount;
        }

        public void setLitenerCount(String litenerCount) {
            this.litenerCount = litenerCount;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }
}
