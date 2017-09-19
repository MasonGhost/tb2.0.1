package com.jess.camerafilters.filter;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import android.content.Context;
import android.opengl.GLES20;

import com.jess.camerafilters.filter.guiFilter.GPUImageFilter;
import com.jess.camerafilters.filter.guiFilter.GpuImageGrayFilter;
import com.jess.camerafilters.util.GlUtil;


public class MagicBaseGroupFilter extends GPUImageFilter {
    protected static int[] frameBuffers = null;
    protected static int[] frameBufferTextures = null;
    private int frameWidth = -1;
    private int frameHeight = -1;
    protected List<GPUImageFilter> filters = Arrays.asList(new CameraFilterBeautyT(context,1.0f),new GpuImageGrayFilter(context));
public MagicBaseGroupFilter(Context context){
    super(context);
};
    public MagicBaseGroupFilter(Context context,List<GPUImageFilter> filters) {
        super(context);
    }

    @Override
    public int createProgram(Context applicationContext) {
        for (GPUImageFilter filter : filters) {
           return  filter.createProgram(applicationContext);
        }
        return super.createProgram(applicationContext);
    }

    @Override
    public void getGLSLValues() {
        for (GPUImageFilter filter : filters) {
            filter.getGLSLValues();
        }
    }

    @Override
    public void releaseProgram() {
        for (GPUImageFilter filter : filters) {
            filter.releaseProgram();
        }
        destroyFramebuffers();
    }

    @Override
    public void setTextureSize(int width, int height) {
        super.setTextureSize(width, height);
        int size = filters.size();
        for (int i = 0; i < size; i++) {
            filters.get(i).setTextureSize(width, height);
        }
        if (frameBuffers != null && (frameWidth != width || frameHeight != height || frameBuffers.length != size - 1)) {
            destroyFramebuffers();
            frameWidth = width;
            frameHeight = height;
        }
        if (frameBuffers == null) {
            frameBuffers = new int[size - 1];
            frameBufferTextures = new int[size - 1];

            for (int i = 0; i < size - 1; i++) {
                GLES20.glGenFramebuffers(1, frameBuffers, i);

                GLES20.glGenTextures(1, frameBufferTextures, i);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[i]);
                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                        GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            }
        }

    }

//    @Override
//    public void onInputSizeChanged(final int width, final int height) {
//        super.onInputSizeChanged(width, height);
//        int size = filters.size();
//        for (int i = 0; i < size; i++) {
//            filters.get(i).onInputSizeChanged(width, height);
//        }
//        if (frameBuffers != null && (frameWidth != width || frameHeight != height || frameBuffers.length != size - 1)) {
//            destroyFramebuffers();
//            frameWidth = width;
//            frameHeight = height;
//        }
//        if (frameBuffers == null) {
//            frameBuffers = new int[size - 1];
//            frameBufferTextures = new int[size - 1];
//
//            for (int i = 0; i < size - 1; i++) {
//                GLES20.glGenFramebuffers(1, frameBuffers, i);
//
//                GLES20.glGenTextures(1, frameBufferTextures, i);
//                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTextures[i]);
//                GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
//                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
//                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                        GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                        GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                        GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
//                GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
//                        GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
//                GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
//                        GLES20.GL_TEXTURE_2D, frameBufferTextures[i], 0);
//
//                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
//                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//            }
//        }
//    }


    @Override
    public void onDraw(float[] mvpMatrix, FloatBuffer vertexBuffer, int firstVertex, int vertexCount, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int textureId, int texStride) {
        //super.onDraw(mvpMatrix, vertexBuffer, firstVertex, vertexCount, coordsPerVertex, vertexStride, texMatrix, texBuffer, textureId, texStride);
        int size = filters.size();
        int previousTexture = textureId;
        for (int i = 0; i < size; i++) {
            GPUImageFilter filter = filters.get(i);
            boolean isNotLast = i < size - 1;
            if (isNotLast) {
                GLES20.glViewport(0, 0, mIncomingWidth, mIncomingHeight);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
                GLES20.glClearColor(0, 0, 0, 0);
                filter.onDraw(mvpMatrix, vertexBuffer, firstVertex,vertexCount,coordsPerVertex,vertexStride,texMatrix,texBuffer,textureId,texStride);
                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                previousTexture = frameBufferTextures[i];
            } else {
                GLES20.glViewport(0, 0, mIncomingWidth, mIncomingHeight);
                filter.onDraw(mvpMatrix, vertexBuffer, firstVertex,vertexCount,coordsPerVertex,vertexStride,texMatrix,texBuffer,textureId,texStride);
            }
        }
    }

//
//    public int onDrawFrame(final int textureId, final FloatBuffer cubeBuffer,
//                           final FloatBuffer textureBuffer) {
//        if (frameBuffers == null || frameBufferTextures == null) {
//            return GlUtil.NO_INIT;
//        }
//        int size = filters.size();
//        int previousTexture = textureId;
//        for (int i = 0; i < size; i++) {
//            GPUImageFilter filter = filters.get(i);
//            boolean isNotLast = i < size - 1;
//            if (isNotLast) {
//                GLES20.glViewport(0, 0, mIntputWidth, mIntputHeight);
//                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[i]);
//                GLES20.glClearColor(0, 0, 0, 0);
//                filter.onDrawFrame(previousTexture, mGLCubeBuffer, mGLTextureBuffer);
//                GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
//                previousTexture = frameBufferTextures[i];
//            } else {
//                GLES20.glViewport(0, 0, mOutputWidth, mOutputHeight);
//                filter.onDrawFrame(previousTexture, cubeBuffer, textureBuffer);
//            }
//        }
//        return OpenGlUtils.ON_DRAWN;
//    }


    private void destroyFramebuffers() {
        if (frameBufferTextures != null) {
            GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);
            frameBufferTextures = null;
        }
        if (frameBuffers != null) {
            GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
            frameBuffers = null;
        }
    }

    public int getSize() {
        return filters.size();
    }
}
