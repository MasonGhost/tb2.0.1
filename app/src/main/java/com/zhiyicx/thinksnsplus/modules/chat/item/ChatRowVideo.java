package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;
import com.zhiyicx.thinksnsplus.R;

import java.io.File;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class ChatRowVideo extends ChatBaseRow{

    private ImageView mIvVideoAvatar;
    private TextView mTvVideoLength;

    public ChatRowVideo(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        String localThumb = videoBody.getLocalThumb();
        if (localThumb != null) {

            showVideoThumbView(localThumb, mIvVideoAvatar, videoBody.getThumbnailUrl(), message);
        }
        if (videoBody.getDuration() > 0) {
            String time = DateUtils.toTime(videoBody.getDuration());
            mTvVideoLength.setText(time);
        }

        EMLog.d(TAG,  "video thumbnailStatus:" + videoBody.thumbnailDownloadStatus());
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                mIvVideoAvatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_default_image);
            } else {
                // System.err.println("!!!! not back receive, show image directly");
                mIvVideoAvatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_default_image);
                if (localThumb != null) {
                    showVideoThumbView(localThumb, mIvVideoAvatar, videoBody.getThumbnailUrl(), message);
                }
            }
            return;
        }else{
            if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                mIvVideoAvatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_default_image);
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                mIvVideoAvatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_default_image);
                showVideoThumbView(localThumb, mIvVideoAvatar, videoBody.getThumbnailUrl(), message);
            }
        }
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mIvVideoAvatar = (ImageView) findViewById(R.id.iv_video_avatar);
        mTvVideoLength = (TextView) findViewById(R.id.tv_video_length);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.SEND ?
                R.layout.item_chat_list_send_video : R.layout.item_chat_list_receive_video, this);
    }

    /**
     * show video thumbnails
     *
     * @param localThumb
     *            local path for thumbnail
     * @param iv
     * @param thumbnailUrl
     *            Url on server for thumbnails
     * @param message
     */
    private void showVideoThumbView(final String localThumb, final ImageView iv, String thumbnailUrl, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(localThumb);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
        } else {
            mIvVideoAvatar.setImageResource(com.hyphenate.easeui.R.drawable.ease_default_image);
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    if (new File(localThumb).exists()) {
                        return ImageUtils.decodeScaleImage(localThumb, 160, 160);
                    } else {
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        EaseImageCache.getInstance().put(localThumb, result);
                        iv.setImageBitmap(result);

                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                EMClient.getInstance().chatManager().downloadThumbnail(message);
                            }
                        }

                    }
                }
            }.execute();
        }

    }
}
