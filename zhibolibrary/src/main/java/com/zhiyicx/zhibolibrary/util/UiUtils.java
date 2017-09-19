package com.zhiyicx.zhibolibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.FutureTarget;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.LoadingDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;

/**
 * Created by jess on 2015/11/23.
 */
public class UiUtils {
    static public Toast mToast;
    public static String FILE_PATH = "zycxZhibolib";
    public static String NO_SDCARD_FILEPATH = FILE_PATH + File.separator;
    public static String SDCARD_FILEPATH = File.separator + NO_SDCARD_FILEPATH;


    /**
     * 设置hint大小
     *
     * @param size
     * @param v
     * @param res
     */
    public static void setViewHintSize(int size, TextView v, int res) {
        SpannableString ss = new SpannableString(getResources().getString(
                res));
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        v.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }


    /**
     * dip转pix
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获得资源
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到字符数组
     */
    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    /**
     * pix转dip
     */
    public static int pix2dip(int pix) {
        final float densityDpi = getResources().getDisplayMetrics().density;
        return (int) (pix / densityDpi + 0.5f);
    }

    /**
     * 获得上下文
     *
     * @return
     */
    public static Context getContext() {
        return ZhiboApplication.getContext();
    }


    /**
     * 从dimens中获得尺寸
     *
     * @param homePicHeight
     * @return
     */

    public static int getDimens(int homePicHeight) {
        return (int) getResources().getDimension(homePicHeight);
    }

    /**
     * 从dimens中获得尺寸
     *
     * @param
     * @return
     */

    public static float getDimens(String dimenNmae) {
        return getResources().getDimension(getResources().getIdentifier(dimenNmae, "dimen", getContext().getPackageName()));
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */

    public static String getString(int stringID) {
        return UiUtils.getContext().getResources().getString(stringID);
    }

    /**
     * 从String 中获得字符
     *
     * @return
     */

    public static String getString(String strName) {
        return getString(getResources().getIdentifier(strName, "string", getContext().getPackageName()));
    }

    /**
     * findview
     *
     * @param view
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(View view, String viewName) {
        int id = getResources().getIdentifier(viewName, "id", getContext().getPackageName());
        T v = (T) view.findViewById(id);
        return v;
    }

    /**
     * findview
     *
     * @param activity
     * @param viewName
     * @param <T>
     * @return
     */
    public static <T extends View> T findViewByName(Activity activity, String viewName) {
        int id = getResources().getIdentifier(viewName, "id", getContext().getPackageName());
        T v = (T) activity.findViewById(id);
        return v;
    }

    /**
     * 根据lauout名字获得id
     *
     * @param layoutName
     * @return
     */
    public static int findLayout(String layoutName) {
        int id = getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());
        return id;
    }

    /**
     * 填充view
     *
     * @param detailScreen
     * @return
     */
    public static View inflate(int detailScreen) {
        return View.inflate(getContext(), detailScreen, null);
    }

    /**
     * 单列toast
     *
     * @param string
     */

    public static void makeText(String string) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 用snackbar显示
     *
     * @param text
     */
    public static void SnackbarText(String text) {
        Intent intent = new Intent(ZBLBaseActivity.ACTION_RECEIVER_ACTIVITY);
        intent.putExtra("type", "showSnackbar");
        intent.putExtra("content", text);
        intent.putExtra("long", false);
        getContext().sendBroadcast(intent);
    }

    /**
     * 用snackbar长时间显示
     *
     * @param text
     */
    public static void SnackbarTextWithLong(String text) {
        Intent intent = new Intent(ZBLBaseActivity.ACTION_RECEIVER_ACTIVITY);
        intent.putExtra("type", "showSnackbar");
        intent.putExtra("content", text);
        intent.putExtra("long", true);
        getContext().sendBroadcast(intent);
    }


    /**
     * 通过资源id获得drawable
     *
     * @param rID
     * @return
     */
    public static Drawable getDrawablebyResource(int rID) {
        return getResources().getDrawable(rID);
    }

    /**
     * 跳转界面
     *
     * @param activity
     * @param homeActivityClass
     */
    public static void startActivity(Activity activity, Class homeActivityClass) {
        Intent intent = new Intent(getContext(), homeActivityClass);
        activity.startActivity(intent);
        Anim.in(activity);
    }

    /**
     * 跳转界面3
     *
     * @param
     * @param homeActivityClass
     */
    public static void startActivity(Class homeActivityClass) {
        Intent intent = new Intent(getContext(), homeActivityClass);
        startActivity(intent);
    }

    /**
     * 跳转界面3
     *
     * @param
     */
    public static void startActivity(Intent content) {
        Intent intent = new Intent(ZBLBaseActivity.ACTION_RECEIVER_ACTIVITY);
        intent.putExtra("type", "startActivity");
        Bundle bundle = new Bundle();
        bundle.putParcelable("content", content);
        intent.putExtras(bundle);
        getContext().sendBroadcast(intent);
    }

    /**
     * 跳转界面4
     *
     * @param
     */
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        Anim.in(activity);
    }

    public static int getLayoutId(String layoutName) {
        return getResources().getIdentifier(layoutName, "layout", getContext().getPackageName());
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeidth() {
        return getResources().getDisplayMetrics().heightPixels;
    }


    /**
     * 显示对话框提示
     *
     * @param text
     */

    public static void showDialog(String text, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setNegativeButton("确定", null);
        builder.setMessage(text);
        builder.show();
    }

    /**
     * 显示对话框提示
     *
     * @param text
     */

    public static void showDialogWithMethod(String text, Activity activity, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("提示");
        builder.setNegativeButton("确定", listener);
        builder.setMessage(text);
        builder.show();
    }

    /**
     * 获得颜色
     */
    public static int getColor(int rid) {
        return getResources().getColor(rid);
    }

    /**
     * 获得颜色
     */
    public static int getColor(String colorName) {
        return getColor(getResources().getIdentifier(colorName, "color", getContext().getPackageName()));
    }

    /**
     * 移除孩子
     *
     * @param view
     */
    public static void removeChild(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(view);
        }
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        return false;
    }

    private static int mCount;


    /**
     * MD5
     *
     * @param string
     * @return
     *
     * @throws Exception
     */
    public static String MD5encode(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 使用glide显示图片
     *
     * @param url
     * @param imageView
     */
    public static void glideDisplay(String url, ImageView imageView) {
        Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .placeholder(R.mipmap.pic_photo_340)
                .into(imageView);
    }

    /**
     * 使用glide显示图片不使用默认图片
     */
    public static void glideDisplayNotPlaceholder(String url, ImageView imageView) {
        Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    /**
     * 使用glide显示，并裁剪图片
     *
     * @param url
     * @param imageView
     * @param transformation
     */
    public static void glideDisplayWithTrasform(String url, ImageView imageView, BitmapTransformation transformation) {
        Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop()
                .transform(transformation)
                .placeholder(R.mipmap.pic_touxiang_150)
                .into(imageView);
    }

    public static DrawableRequestBuilder glideWrap(String url) {
        return Glide.with(getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop();
    }

    /**
     * 通过glide下载图片
     *
     * @param url
     */
    public static void gidleDownload(String url) throws ExecutionException, InterruptedException {

        FutureTarget<File> future = Glide.with(getContext())
                .load(url)
                .downloadOnly(200, 200);
        File cacheFile = future.get();

    }


    /**
     * 缓存图片
     *
     * @param body
     * @param savaName
     * @return
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String savaName) {
        try {
            // todo change the file location/name according to your needs
            String filepath = null;
            if (DeviceUtils.isSdcardReady())
                filepath = Environment.getExternalStorageDirectory() + SDCARD_FILEPATH;
            else
                filepath = NO_SDCARD_FILEPATH;
            File parentFile = new File(filepath);

            InputStream inputStream = null;
            OutputStream outputStream = null;
            File futureStudioIconFile = new File(parentFile, MD5encode(savaName));
            try {
                if (!futureStudioIconFile.exists()) {
                    parentFile.mkdirs();
                    futureStudioIconFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                byte[] fileReader = new byte[1024];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.w("saveFile", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) throws Exception {
        // todo change the file location/name according to your needs
        String filepath = null;
        if (DeviceUtils.isSdcardReady())
            filepath = Environment.getExternalStorageDirectory() + SDCARD_FILEPATH;
        else
            filepath = NO_SDCARD_FILEPATH;
        FileInputStream fis = new FileInputStream(filepath + MD5encode(url));
        return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
    }


    private static LoadingDialog dialog;

    /**
     * 进度条
     *
     * @param activity
     */
    public static void showLoading(Activity activity) {
        dismiss();
        if (dialog == null) {
            dialog = new LoadingDialog(activity);
        }
        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /**
     * 全屏，并且沉侵式状态栏
     *
     * @param activity
     */
    public static void statuInScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }


    /**
     * 退出提示dialog
     */
    public static AlertDialog.Builder getExitDialog(AlertDialog.Builder builder,
                                                    DialogInterface.OnClickListener listener) {

        return getDialog(builder, listener, UiUtils.getString(R.string.exit), UiUtils.getString(R.string.is_exit), UiUtils.getString(R.string.cancel), UiUtils.getString(R.string.str_ok));

    }

    public static AlertDialog.Builder getDialog(AlertDialog.Builder builder,
                                                DialogInterface.OnClickListener listener, String title, String message, String nagativeMessage, String positiveMessage) {
        builder.setTitle(title);
        builder.setMessage(message);
        if (!TextUtils.isEmpty(nagativeMessage))
            builder.setNegativeButton(nagativeMessage, null);
        builder.setPositiveButton(positiveMessage, listener);
        return builder;
    }


}
