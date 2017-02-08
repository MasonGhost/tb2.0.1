package com.zhiyicx.thinksnsplus.modules.gallery;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.thinksnsplus.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */

public class GalleryPhotoAdapter extends PagerAdapter {
    private List<String> paths = new ArrayList<>();
    private ActionPopupWindow mActionPopupWindow;

    public GalleryPhotoAdapter(List<String> paths) {
        this.paths = paths;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(me.iwf.photopicker.R.layout.__picker_picker_item_pager, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_pager);

        final String path = paths.get(position);
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
            uri = Uri.fromFile(new File(path));
        }

        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(context);

        if (canLoadImage) {
            Glide.with(context)
                    .load(uri)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .dontTransform()
                    .override(800, 800)
                    .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                    .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing()) {
                        ((Activity) context).onBackPressed();
                    }
                }
            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
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

        container.addView(itemView);

        return itemView;
    }


    @Override
    public int getCount() {
        return paths.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    // 获取当前位置的图片路径
    public String getPathAtPosition(int position) {
        if (paths == null) {
            return "";
        }
        return paths.get(position);
    }

    public List<String> getPhotos() {
        return paths;
    }
}
