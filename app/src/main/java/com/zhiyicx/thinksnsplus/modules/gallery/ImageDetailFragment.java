package com.zhiyicx.thinksnsplus.modules.gallery;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressListener;
import com.zhiyicx.baseproject.impl.imageloader.glide.progress.ProgressModelLoader;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;
import me.iwf.photopicker.widget.TouchImageView;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class ImageDetailFragment extends TSFragment {
    @BindView(R.id.iv_pager)
    TouchImageView mIvPager;
    @BindView(R.id.pb_progress)
    ProgressBar mPbProgress;
    @BindView(R.id.tv_origin_photo)
    TextView mTvOriginPhoto;
    private String mImageUrl;
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
    protected void initView(View rootView) {
        context = getContext();
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
        // 图片长按，保存
        mIvPager.setOnLongClickListener(new View.OnLongClickListener() {
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
        // 显示图片
        if (TextUtils.isEmpty(mImageUrl)) {
            mIvPager.setImageResource(R.mipmap.ic_launcher);
            return;
        } else {
            boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);
            if (canLoadImage) {

            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.item_gallery_photo;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }


    @OnClick({R.id.iv_pager, R.id.tv_origin_photo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pager:
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
                break;
            case R.id.tv_origin_photo:
                break;
        }
    }

    public void saveImage() {
        //BitmapUtils.saveBitmap(getActivity(), mImageView.getDrawingCache(), StringUtils.getImageNameByUrl(mImageUrl));
    }

    // 加载较小的缩略图
    private void loadSmallImage() {
        loadImage(mImageUrl + "/10");
    }

    // 加载较大缩略图
    private void loadBigImage() {
        loadImage(mImageUrl + "/50");
    }

    // 加载图片不带监听
    private void loadImage(String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .thumbnail(0.1f)
                .override(800, 800)
                .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                .into(new ImageViewTarget<GlideDrawable>(mIvPager) {
                    @Override
                    protected void setResource(GlideDrawable resource) {

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        mPbProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        mIvPager.setImageDrawable(resource);
                        mPbProgress.setVisibility(View.GONE);
                    }
                });
    }

    // 加载原图:
    private void loadOriginImage() {
        Glide.with(context)
                .using(new ProgressModelLoader(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == ProgressListener.SEND_LOAD_PROGRESS) {
                            int totalReadBytes = msg.arg1;
                            int lengthBytes = msg.arg2;
                            int progressResult = (int) (((float) totalReadBytes / (float) lengthBytes) * 100);
                            mTvOriginPhoto.setText(progressResult + "%/" + "100%");
                            LogUtils.i("progress-result:-->" + progressResult + " msg.arg1-->" + msg.arg1 + "  msg.arg2-->" +
                                    msg.arg2 + " 比例-->" + progressResult + "%/" + "100%");
                        }
                    }
                }))
                .load(mImageUrl)
                .thumbnail(0.1f)
                .override(800, 800)
                .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                .into(mIvPager);
    }
}
