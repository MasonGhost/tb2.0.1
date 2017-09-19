package com.jess.camerafilters.base;

/**
 * Created by jerikc on 16/2/23.
 */

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Environment;

import com.jess.camerafilters.filter.IFilter;
import com.jess.camerafilters.util.Drawable2d;
import com.jess.camerafilters.util.GlUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.IntBuffer;

/**
 * This class essentially represents a viewport-sized sprite that will be rendered with
 * a texture, usually from an external source like the camera.
 */
public class FullFrameRect {
    private final Drawable2d mRectDrawable = new Drawable2d();
    private IFilter mFilter;
    public final float[] IDENTITY_MATRIX = new float[16];
    public boolean isScreenShot;
    private Bitmap mShotBitmap;
    private OnGlSurfaceShotListener mOnGlSurfaceListener;
    private File mShotFile;
    private String mDirPath;

    /**
     * Prepares the object.
     *
     * @param program The program to use.  FullFrameRect takes ownership, and will release
     *                the program when no longer needed.
     */
    public FullFrameRect(IFilter program) {
        mFilter = program;
        isScreenShot = false;
        Matrix.setIdentityM(IDENTITY_MATRIX, 0);
        initFilePath();
    }

    public void setOnGlSurfaceShotListener(OnGlSurfaceShotListener listener) {
        mOnGlSurfaceListener = listener;
    }

    private void initFilePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhibo";
        } else {
            mDirPath = Environment.getDataDirectory().getAbsolutePath() + "/zhibo";
        }
        File dirFile = new File(mDirPath);
        if (!dirFile.exists())
            dirFile.mkdirs();
    }

    /**
     * Releases resources.
     * <p/>
     * This must be called with the appropriate EGL context current (i.e. the one that was
     * current when the constructor was called).  If we're about to destroy the EGL context,
     * there's no value in having the caller make it current just to do this cleanup, so you
     * can pass a flag that will tell this function to skip any EGL-context-specific cleanup.
     */
    public void release(boolean doEglCleanup) {
        if (mFilter != null) {
            if (doEglCleanup) {
                mFilter.releaseProgram();
            }
            mFilter = null;
        }
    }

    /**
     * Returns the program currently in use.
     */
    public IFilter getFilter() {
        return mFilter;
    }

    /**
     * Changes the program.  The previous program will be released.
     * <p/>
     * The appropriate EGL context must be current.
     */
    public void changeProgram(IFilter newFilter) {
        mFilter.releaseProgram();
        mFilter = newFilter;
    }

    /**
     * Creates a texture object suitable for use with drawFrame().
     */
    public int createTexture() {
        return GlUtil.createTexture(mFilter.getTextureTarget());
    }

    public int createTexture(Bitmap bitmap) {
        return GlUtil.createTexture(mFilter.getTextureTarget(), bitmap);
    }

    public void scaleMVPMatrix(float x, float y) {
        Matrix.setIdentityM(IDENTITY_MATRIX, 0);
        Matrix.scaleM(IDENTITY_MATRIX, 0, x, y, 1f);
    }


    /**
     * Draws a viewport-filling rect, texturing it with the specified texture object.
     */
    public void drawFrame(int textureId, float[] texMatrix) {

        // Use the identity matrix for MVP so our 2x2 FULL_RECTANGLE covers the viewport.
        if (!isScreenShot)
            mFilter.onDraw(IDENTITY_MATRIX, mRectDrawable.getVertexArray(), 0,
                    mRectDrawable.getVertexCount(), mRectDrawable.getCoordsPerVertex(),
                    mRectDrawable.getVertexStride(), texMatrix, mRectDrawable.getTexCoordArray(),
                    textureId, mRectDrawable.getTexCoordStride());

        drawScreenShotBitmap();
    }


    private void drawScreenShotBitmap() {
        try {

            if (isScreenShot) {
                if (mShotBitmap != null) {
                    mShotBitmap.recycle();
                    mShotBitmap = null;
                }
                mShotFile = new File(mDirPath, System.currentTimeMillis() + ".jpg");

                int w = mFilter.getTextureWidth();
                int h = mFilter.getTextureHeight();
                int b[] = new int[(int) (w * h)];
                int bt[] = new int[(int) (w * h)];
                IntBuffer buffer = IntBuffer.wrap(b);
                buffer.position(0);
                GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
                int offset1, offset2;
                for (int i = 0; i < h; i++) {
                    offset1 = i * w;
                    offset2 = (h - i - 1) * w;
                    for (int j = 0; j < w; j++) { //remember, that OpenGL bitmap is incompatible with Android bitmap
                        //and so, some correction need.
                        //and so, some correction need.
                        int texturePixel = b[offset1 + j];
                        int blue = (texturePixel >> 16) & 0xff;
                        int red = (texturePixel << 16) & 0x00ff0000;
                        int pixel = (texturePixel & 0xff00ff00) | red | blue;
                        bt[offset2 + j] = pixel;
                    }
                }
                if (mShotBitmap == null || !mShotBitmap.isMutable()
                        || mShotBitmap.getWidth() != w || mShotBitmap.getHeight() != h) {
                    mShotBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                }
                mShotBitmap.copyPixelsFromBuffer(buffer);
                mShotBitmap = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);


                //saveBitmap(mShotFile, mShotBitmap);
                if (mOnGlSurfaceListener != null) {
                    mOnGlSurfaceListener.onGlSurfaceShot(mShotBitmap, mShotFile.getAbsolutePath());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            isScreenShot = false;
        }
    }

    /**
     * bitmap到指定地址
     *
     * @param file
     * @param bitmap
     */
    public boolean saveBitmap(File file, Bitmap bitmap) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_TRANSVERSE:
                    degree = -90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
