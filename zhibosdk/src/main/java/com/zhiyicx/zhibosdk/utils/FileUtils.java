package com.zhiyicx.zhibosdk.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.ResponseBody;

/**
 * Created by jungle on 16/9/12.
 * com.zhiyicx.zhibosdk.utils
 * zhibo_android
 * email:335891510@qq.com
 */
public class FileUtils {

    public static String FILE_PATH = "zycxZhiboSdk";
    public static String NO_SDCARD_FILEPATH = FILE_PATH + File.separator;
    public static String SDCARD_FILEPATH = File.separator + NO_SDCARD_FILEPATH;

    /**
     * 缓存文件
     *
     * @param body
     * @param savaName
     * @return
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String savaName) {
        try {
            // todo change the file location/name according to your needs
            String filepath = getRootPath();
            File parentFile = new File(filepath);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            File futureStudioIconFile = new File(parentFile, savaName);
            try {
                if (!futureStudioIconFile.exists()) {
                    if (!parentFile.exists())
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
     * 从项目assets文件夹拷贝到手机sd卡
     *
     * @param context
     */
    public static void CopyAssets(Context context, String zipName) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(zipName);
            out = new FileOutputStream(
                    android.os.Environment.getExternalStorageDirectory() + "/"
                            + zipName);
            CopyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("Test", e.getMessage());
        }
    }

    private static void CopyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        int n;
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
    }


    /**
     * 解压缩文件到指定的目录.
     *
     * @param unZipfileName 需要解压缩的文件
     * @param mDestPath     解压缩后存放的路径
     */
    public static void unZip(String unZipfileName, String mDestPath) {

        String filepath = getRootPath();


        mDestPath = filepath + mDestPath;

        unZipfileName = filepath + unZipfileName;
        FileOutputStream fileOut = null;
        ZipInputStream zipIn = null;
        ZipEntry zipEntry = null;
        File file = null;
        int readedBytes = 0;
        byte buf[] = new byte[4096];
        try {
            zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(unZipfileName)));
            while ((zipEntry = zipIn.getNextEntry()) != null) {
//                file = new File(mDestPath + zipEntry.getName());
                file = new File(mDestPath);
                if (zipEntry.isDirectory()) {
                    file.mkdirs();
                }
                else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    fileOut = new FileOutputStream(file);
                    while ((readedBytes = zipIn.read(buf)) > 0) {
                        fileOut.write(buf, 0, readedBytes);
                    }
                    fileOut.close();
                }
                zipIn.closeEntry();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static String getRootPath() {
        String filepath = null;
        if (DeviceUtils.isSdcardReady())
            filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + SDCARD_FILEPATH;
        else
            filepath = NO_SDCARD_FILEPATH;
        File parent = new File(filepath);
        if (!parent.exists()) {
            boolean isSuccess = parent.mkdirs();
            System.out.println("isSuccess = " + isSuccess);
        }
        return filepath;
    }

    /**
     * 读取本地文件
     *
     * @param path
     * @return
     */
    public static String readFile(String path) {
        String filepath = getRootPath();
        path = filepath + path;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(new FileInputStream(path), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
