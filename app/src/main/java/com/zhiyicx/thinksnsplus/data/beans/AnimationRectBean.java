package com.zhiyicx.thinksnsplus.data.beans;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.common.utils.log.LogUtils;

import java.io.Serializable;

/**
 * @author LiuChao
 * @describe 用于图片查看时的转场缩放动画实体类，传递的是图片控件的相关属性，以及
 * @date 2017/3/21
 * @contact email:450127106@qq.com
 */
public class AnimationRectBean implements Parcelable,Serializable {
    private static final long serialVersionUID = 336871009;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(scaledBitmapRect, flags);
        dest.writeParcelable(imageViewEntireRect, flags);
        dest.writeParcelable(imageViewVisibleRect, flags);
        dest.writeInt(type);
        dest.writeBooleanArray(new boolean[]{isTotalVisible});
        dest.writeBooleanArray(new boolean[]{isTotalInvisible});
        dest.writeBooleanArray(new boolean[]{isScreenPortrait});
        dest.writeFloat(thumbnailWidthHeightRatio);
        dest.writeInt(thumbnailWidth);
        dest.writeInt(thumbnailHeight);
        dest.writeInt(widgetWidth);
        dest.writeInt(widgetHeight);
        dest.writeFloat(clipByParentRectTop);
        dest.writeFloat(clipByParentRectBottom);
        dest.writeFloat(clipByParentRectLeft);
        dest.writeFloat(clipByParentRectRight);
    }

    public static final Creator<AnimationRectBean> CREATOR =
            new Creator<AnimationRectBean>() {
                public AnimationRectBean createFromParcel(Parcel in) {
                    AnimationRectBean rect = new AnimationRectBean();
                    rect.scaledBitmapRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.imageViewEntireRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.imageViewVisibleRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.type = in.readInt();

                    boolean[] booleans = new boolean[1];
                    in.readBooleanArray(booleans);
                    rect.isTotalVisible = booleans[0];

                    boolean[] isTotalInvisibleBooleans = new boolean[1];
                    in.readBooleanArray(isTotalInvisibleBooleans);
                    rect.isTotalInvisible = isTotalInvisibleBooleans[0];

                    boolean[] isScreenPortraitArray = new boolean[1];
                    in.readBooleanArray(isScreenPortraitArray);
                    rect.isScreenPortrait = isScreenPortraitArray[0];

                    rect.thumbnailWidthHeightRatio = in.readFloat();
                    rect.thumbnailWidth = in.readInt();
                    rect.thumbnailHeight = in.readInt();

                    rect.widgetWidth = in.readInt();
                    rect.widgetHeight = in.readInt();

                    rect.clipByParentRectTop = in.readFloat();
                    rect.clipByParentRectBottom = in.readFloat();
                    rect.clipByParentRectLeft = in.readFloat();
                    rect.clipByParentRectRight = in.readFloat();

                    return rect;
                }

                public AnimationRectBean[] newArray(int size) {
                    return new AnimationRectBean[size];
                }
            };

    public static final int TYPE_CLIP_V = 0;
    public static final int TYPE_CLIP_H = 1;
    public static final int TYPE_EXTEND_V = 2;
    public static final int TYPE_EXTEND_H = 3;

    public float clipByParentRectTop;
    public float clipByParentRectBottom;
    public float clipByParentRectLeft;
    public float clipByParentRectRight;

    public Rect imageViewEntireRect;
    public Rect imageViewVisibleRect;
    public Rect scaledBitmapRect;

    public int type = -1;

    public boolean isTotalVisible;
    public boolean isTotalInvisible;

    public boolean isScreenPortrait;

    public float thumbnailWidthHeightRatio;
    public int thumbnailWidth;
    public int thumbnailHeight;
    public int widgetWidth;
    public int widgetHeight;

    public static AnimationRectBean buildFromImageView(ImageView imageView) {

        AnimationRectBean rect = new AnimationRectBean();
        if (imageView == null) {
            return null;
        }
        // rect.isScreenPortrait = Utility.isDevicePort();
        Drawable drawable = imageView.getDrawable();
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
//        LogUtils.i("animationRect-->drawable width:" + drawable.getIntrinsicWidth() + "bitmap width:" + intrinsicWidth);
        rect.widgetWidth = imageView.getWidth();

        rect.widgetHeight = imageView.getHeight();

        rect.thumbnailWidthHeightRatio = (float) intrinsicWidth / (float) intrinsicHeight;

        rect.thumbnailWidth = intrinsicWidth;

        rect.thumbnailHeight = intrinsicHeight;

        rect.imageViewEntireRect = new Rect();
        int[] location = new int[2];
        // imageView最左边和最上边的位置
        imageView.getLocationOnScreen(location);
        rect.imageViewEntireRect.left = location[0];
        rect.imageViewEntireRect.top = location[1];
        // imageView最右边和最下边的位置
        rect.imageViewEntireRect.right = rect.imageViewEntireRect.left + imageView.getWidth();
        rect.imageViewEntireRect.bottom = rect.imageViewEntireRect.top + imageView.getHeight();

        rect.imageViewVisibleRect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect.imageViewVisibleRect);

        boolean checkWidth = rect.imageViewVisibleRect.width() < imageView.getWidth();
        boolean checkHeight = rect.imageViewVisibleRect.height() < imageView.getHeight();
        // 是否完全可见
        rect.isTotalVisible = isVisible && !checkWidth && !checkHeight;
        // 是否可见
        rect.isTotalInvisible = !isVisible;

        ImageView.ScaleType scaledType = imageView.getScaleType();

        Rect scaledBitmapRect = new Rect(rect.imageViewEntireRect);

        int bitmapWidth = intrinsicWidth;
        int bitmapHeight = intrinsicHeight;

        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();

        float startScale;

        int deltaX;

        int deltaY;

        switch (scaledType) {
            case CENTER_CROP:

                if ((float) imageViewWidth / bitmapWidth
                        > (float) imageViewHeight / bitmapHeight) {

                    startScale = (float) imageViewWidth / bitmapWidth;
                    rect.type = TYPE_CLIP_V;
                } else {
                    startScale = (float) imageViewHeight / bitmapHeight;
                    rect.type = TYPE_CLIP_H;
                }

                bitmapHeight = (int) (bitmapHeight * startScale);
                bitmapWidth = (int) (bitmapWidth * startScale);

                deltaX = (imageViewWidth - bitmapWidth) / 2;
                deltaY = (imageViewHeight - bitmapHeight) / 2;

                scaledBitmapRect.set(scaledBitmapRect.left + deltaX, scaledBitmapRect.top + deltaY,
                        scaledBitmapRect.right - deltaX,
                        scaledBitmapRect.bottom - deltaY);

                break;

            case FIT_CENTER:

                if ((float) imageViewWidth / bitmapWidth
                        > (float) imageViewHeight / bitmapHeight) {
                    // Extend start bounds horizontally
                    startScale = (float) imageViewHeight / bitmapHeight;

                    rect.type = TYPE_EXTEND_V;
                } else {
                    startScale = (float) imageViewWidth / bitmapWidth;
                    rect.type = TYPE_EXTEND_H;
                }

                bitmapHeight = (int) (bitmapHeight * startScale);
                bitmapWidth = (int) (bitmapWidth * startScale);

                deltaX = (imageViewWidth - bitmapWidth) / 2;
                deltaY = (imageViewHeight - bitmapHeight) / 2;

                scaledBitmapRect
                        .set(scaledBitmapRect.left + deltaX, scaledBitmapRect.top + deltaY,
                                scaledBitmapRect.right - deltaX,
                                scaledBitmapRect.bottom - deltaY);

                break;

            default:
                break;
        }

        rect.scaledBitmapRect = scaledBitmapRect;

        return rect;
    }

    public static float getClipLeft(AnimationRectBean animationRectBean, Rect finalBounds) {
        final Rect startBounds = animationRectBean.scaledBitmapRect;
        if (startBounds == null) {
            return 0;
        }
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaRightSize = Math
                .abs(animationRectBean.scaledBitmapRect.width() - oriBitmapScaledWidth);

        float serverClipThumbnailRightSizePercent = (float) thumbnailAndOriDeltaRightSize
                / (float) oriBitmapScaledWidth;

        float deltaH = oriBitmapScaledWidth
                - oriBitmapScaledWidth * serverClipThumbnailRightSizePercent
                - animationRectBean.widgetWidth;

        float deltaLeft = deltaH / 2;

        if (!animationRectBean.isTotalVisible && !animationRectBean.isTotalInvisible) {
            float deltaInvisibleLeft = Math
                    .abs(animationRectBean.imageViewVisibleRect.left
                            - animationRectBean.imageViewEntireRect.left);
            deltaLeft += deltaInvisibleLeft;
        }

        return (deltaLeft) / (float) oriBitmapScaledWidth;
    }

    public static float getClipTop(AnimationRectBean animationRectBean, Rect finalBounds) {

        final Rect startBounds = animationRectBean.scaledBitmapRect;
        if (startBounds == null) {
            return 0;
        }
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        int oriBitmapScaledHeight = (int) (finalBounds.height() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaBottomSize = Math
                .abs(animationRectBean.scaledBitmapRect.height() - oriBitmapScaledHeight);

        float serverClipThumbnailBottomSizePercent = (float) thumbnailAndOriDeltaBottomSize
                / (float) oriBitmapScaledHeight;

        float deltaV = oriBitmapScaledHeight
                - oriBitmapScaledHeight * serverClipThumbnailBottomSizePercent
                - animationRectBean.widgetHeight;

        float deltaTop = deltaV / 2;

        if (!animationRectBean.isTotalVisible && !animationRectBean.isTotalInvisible) {

            float deltaInvisibleTop = Math
                    .abs(animationRectBean.imageViewVisibleRect.top
                            - animationRectBean.imageViewEntireRect.top);

            deltaTop += deltaInvisibleTop;
        }

        return (deltaTop) / (float) oriBitmapScaledHeight;
    }

    public static float getClipRight(AnimationRectBean animationRectBean, Rect finalBounds) {
        final Rect startBounds = animationRectBean.scaledBitmapRect;
        if (startBounds == null) {
            return 0;
        }
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaRightSize = Math
                .abs(animationRectBean.scaledBitmapRect.width() - oriBitmapScaledWidth);

        float serverClipThumbnailRightSizePercent = (float) thumbnailAndOriDeltaRightSize
                / (float) oriBitmapScaledWidth;

        float deltaH = oriBitmapScaledWidth
                - oriBitmapScaledWidth * serverClipThumbnailRightSizePercent
                - animationRectBean.widgetWidth;

        float deltaRight = deltaH / 2;

        if (!animationRectBean.isTotalVisible && !animationRectBean.isTotalInvisible) {
            float deltaInvisibleRight = Math
                    .abs(animationRectBean.imageViewVisibleRect.right
                            - animationRectBean.imageViewEntireRect.right);
            deltaRight += deltaInvisibleRight;
        }

        deltaRight += thumbnailAndOriDeltaRightSize;

        return (deltaRight) / (float) oriBitmapScaledWidth;
    }

    public static float getClipBottom(AnimationRectBean animationRectBean, Rect finalBounds) {
        final Rect startBounds = animationRectBean.scaledBitmapRect;
        if (startBounds == null) {
            return 0;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        int oriBitmapScaledHeight = (int) (finalBounds.height() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaBottomSize = Math
                .abs(animationRectBean.scaledBitmapRect.height() - oriBitmapScaledHeight);

        float serverClipThumbnailBottomSizePercent = (float) thumbnailAndOriDeltaBottomSize
                / (float) oriBitmapScaledHeight;

        float deltaV = oriBitmapScaledHeight
                - oriBitmapScaledHeight * serverClipThumbnailBottomSizePercent
                - animationRectBean.widgetHeight;

        float deltaBottom = deltaV / 2;

        if (!animationRectBean.isTotalVisible && !animationRectBean.isTotalInvisible) {

            float deltaInvisibleBottom = Math
                    .abs(animationRectBean.imageViewVisibleRect.bottom
                            - animationRectBean.imageViewEntireRect.bottom);

            deltaBottom += deltaInvisibleBottom;
        }

        deltaBottom += thumbnailAndOriDeltaBottomSize;
        return (deltaBottom) / (float) oriBitmapScaledHeight;
    }

    public static boolean isTotalVisibleForView(View view) {
        Rect rect = new Rect();
        boolean isVisible = view.getGlobalVisibleRect(rect);
        boolean checkWidth = rect.width() < view.getWidth();
        boolean checkHeight = rect.height() < view.getHeight();
        // 是否完全可见
        return isVisible && !checkWidth && !checkHeight;
    }

    @Override
    public String toString() {
        return "\n" +
                " AnimationRectBean{" +
                "\nclipByParentRectTop=" + clipByParentRectTop +
                ",\n clipByParentRectBottom=" + clipByParentRectBottom +
                ", \nclipByParentRectLeft=" + clipByParentRectLeft +
                ", \nclipByParentRectRight=" + clipByParentRectRight +
                ", \nimageViewEntireRect=" + imageViewEntireRect +
                ", \nimageViewVisibleRect=" + imageViewVisibleRect +
                ", \nscaledBitmapRect=" + scaledBitmapRect +
                ",\n type=" + type +
                ", \nisTotalVisible=" + isTotalVisible +
                ", \nisTotalInvisible=" + isTotalInvisible +
                ",\n isScreenPortrait=" + isScreenPortrait +
                ", \nthumbnailWidthHeightRatio=" + thumbnailWidthHeightRatio +
                ",\n thumbnailWidth=" + thumbnailWidth +
                ",\n thumbnailHeight=" + thumbnailHeight +
                ", \nwidgetWidth=" + widgetWidth +
                ",\n widgetHeight=" + widgetHeight +
                '}' + "\n--------------------------\n";
    }
}
