package com.zhiyicx.zhibolibrary.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by zhiyicx on 2016/3/15.
 */
public class DataHelper {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "ablib_config";
    public static final String USER_INFO = "ablib_userinfo";
    public static final String USER_PERMISSION = "ablib_user_permission";
    public static final String USER_TICKET= "ablib_user_ticket";
    public static final String CONFIG_NAME = "ablib_config";
    public static final String API_VERSION = "ablib_apiVersion";
    public static final String CACHE_FILE_PATH = "/mnt/sdcard/ablib_cache";
    public static final String CROP_CACHE_NAME = "ablib_cropped.jpg";
    public static final String CROP_AVATAR_NAME = "ablib_avatar.jpg";
    public static final String AREA_POINT1 = "ablib_area_point1";
    public static final String AREA_POINT2 = "ablib_area_point2";
    public static final String CAMERA_CAPTURE_TEMP_PICTURE = "ablib_tmp_pic.jpg";

    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void SetStringSF(String key, String value,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static String getStringSF(String key,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getString(key, null);
    }

    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void SetIntergerSF(String key, int value,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 返回存在sharedPreferences的信息
     *
     * @param key
     * @return
     */
    public static int getIntergerSF(String key,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * 清除某个内容
     */
    public static void removeSF(String key,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().remove(key).commit();
    }

    /**
     * 清除Shareprefrence
     */
    public static void clearShareprefrence(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        mSharedPreferences.edit().clear().commit();
    }

    /**
     * 将对象储存到sharepreference
     *
     * @param key
     * @param device
     * @param <T>
     */
    public static <T> boolean saveDeviceData(String key, T device,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(device);
            // 将字节流编码成base64的字符串
            String oAuth_Base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            mSharedPreferences.edit().putString(key, oAuth_Base64).commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将对象从shareprerence中取出来
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getDeviceData(String key,Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        T device = null;
        String productBase64 = mSharedPreferences.getString(key, null);

        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);

            // 读取对象
            device = (T) bis.readObject();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return device;
    }

    /**
     * 返回缓存文件夹
     */
    public static File getCacheFile(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getApplicationContext().getExternalCacheDir();//获取系统管理的sd卡缓存文件
            if (file == null) {//如果获取的为空,就是用自己定义的缓存文件夹做缓存路径
                file = new File(CACHE_FILE_PATH);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            return file;
        } else {
            return context.getApplicationContext().getCacheDir();
        }
    }

    /**
     * 使用递归获取目录文件大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 使用递归删除文件夹
     *
     * @param dir
     * @return
     */
    public static boolean DeleteDir(File dir) {
        if (dir == null) {
            return false;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                DeleteDir(file); // 递归调用继续删除
            }
        }
        return true;
    }

    /**
     * 获得存储在本地assets文件夹下的地区信息
     *
     * @return
     * @throws IOException
     */
    public static String getAreaData(Context context) {
        InputStream area = null;
        ByteArrayOutputStream outputStream = null;
        try {
            area = context.getApplicationContext().getAssets().open("area.txt");//读取本地assets数据
            outputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = area.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            return outputStream.toString("UTF-8");//本地数据
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            okhttp3.internal.Util.closeQuietly(area);
            okhttp3.internal.Util.closeQuietly(outputStream);
        }
        return null;
    }


    public static String BytyToString(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int num = 0;
        while ((num = in.read(buf)) != -1) {
            out.write(buf, 0, buf.length);
        }
        String result = out.toString();
        out.close();
        return result;
    }

}
