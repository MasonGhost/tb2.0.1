package com.zhiyicx.zhibosdk.utils;

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
public class ZBDataHelper {
    private static SharedPreferences mSharedPreferences;
    public static final String SP_NAME = "sdk_abconfig";
    public static final String CONFIG_NAME = "sdk_abconfig";
    public static final String API_VERSION = "sdk_abapiVersion";
    public static final String CACHE_FILE_PATH = "zhibosdk_cache";
    public static final String LAST_USE_TIME = "sdk_last_use_time";
    public static final String FILTER_WORD_VERSION = "filter_word_version";
    public static final String FILTER_WORD = "filter_word";
    public static final String FILTER_WORD_UNZIP = "filter_word_unzip";
    private static String FILE_PATH = "zhibosdk";

    /**
     * 存储重要信息到sharedPreferences；
     *
     * @param key
     * @param value
     */
    public static void SetStringSF(Context context, String key, String value) {
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
    public static String getStringSF(Context context, String key) {
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
    public static void SetIntergerSF(Context context, String key, int value) {
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
    public static int getIntergerSF(Context context, String key) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return mSharedPreferences.getInt(key, -1);
    }

    /**
     * 清除某个内容
     */
    public static void removeSF(Context context, String key) {
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
    public static <T> boolean saveDeviceData(Context context, String key, T device) {
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
    public static <T> T getDeviceData(Context context, String key) {
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
            context.getApplicationContext().getExternalCacheDir();
            String filepath = null;
            File file = null;
            filepath = context.getApplicationContext().getExternalCacheDir() + CACHE_FILE_PATH + File.separator;//获取系统管理的sd卡缓存文件
            file = new File(filepath);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        else {
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
            }
            else if (file.isDirectory()) {
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
            }
            else if (file.isDirectory()) {
                getDirSize(file); // 递归调用继续统计
            }
        }
        return true;
    }

    /**
     * 获得存储在本地assets文件夹下的地区信息
     *
     * @return
     *
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
