package com.zhiyicx.thinksnsplus.modules.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import java.io.File;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private ImageView mImageView;
    private TextView loadProgress;
    private ProgressBar mProgressBar;
    private ActionPopupWindow mActionPopupWindow;
    private Context context;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.item_gallery_photo, container, false);
        mImageView = (ImageView) v.findViewById(R.id.iv_pager);
        mProgressBar = (ProgressBar) v.findViewById(R.id.pb_progress);
        loadProgress = (TextView) v.findViewById(R.id.tv_load_progress);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
            }
        });
        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mActionPopupWindow == null) {
                    mActionPopupWindow = ActionPopupWindow.builder()
                            .backgroundAlpha(1.0f)
                            .bottomStr(context.getString(R.string.cancel))
                            .item1Str(context.getString(R.string.save_to_photo))
                            .isOutsideTouch(true)
                            .isFocus(true)
                            .with((Activity) context)
                            .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                                @Override
                                public void onItem1Clicked() {
                                    mActionPopupWindow.hide();
                                }
                            })
                            .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                                @Override
                                public void onBottomClicked() {
                                    mActionPopupWindow.hide();
                                }
                            })
                            .build();
                }
                mActionPopupWindow.show();
                return false;
            }
        });
        return v;
    }

    public void saveImage() {
        //BitmapUtils.saveBitmap(getActivity(), mImageView.getDrawingCache(), StringUtils.getImageNameByUrl(mImageUrl));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (TextUtils.isEmpty(mImageUrl)) {
            mImageView.setImageResource(R.mipmap.ic_launcher);
            return;
        } else {
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

            if (canLoadImage) {
                Glide.with(context)
                        .using(new ProgressModelLoader(mHandler))
                        .load(mImageUrl)
                        .thumbnail(0.1f)
                        .override(800, 800)
                        .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                        .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                        .into(new ImageViewTarget<GlideDrawable>(mImageView) {
                            @Override
                            protected void setResource(GlideDrawable resource) {

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                super.onResourceReady(resource, glideAnimation);
                                mImageView.setImageDrawable(resource);
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ProgressListener.SEND_LOAD_PROGRESS) {
                int totalReadBytes = msg.arg1;
                int lengthBytes = msg.arg2;
                String progressResult = (((float) totalReadBytes / (float) lengthBytes) * 100) + "";
                String[] results = progressResult.split(".");
                if (results != null && results.length > 0) {
                    loadProgress.setText(results[0] + "%/" + "100%");
                    LogUtils.i("progress-result:-->"+progressResult + " msg.arg1-->" + msg.arg1 + "  msg.arg2-->" +
                            msg.arg2 + " 比例-->" + results[0] + "%/" + "100%");
                }
            }
        }
    };
}
