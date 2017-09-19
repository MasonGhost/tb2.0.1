package com.jess.camerafilters.filter.guiFilter;

import android.content.Context;
import android.opengl.GLES20;

import com.jess.camerafilters.R;
import com.jess.camerafilters.filter.CameraFilter;
import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

public  class CameraFilterGaussianSingleBlur extends CameraFilter {

    private int muTexelWidthOffset;
    private int muTexelHeightOffset;

    private float mBlurRatio;
    private boolean mWidthOrHeight;

    public String V = "#define SAMPLES 9\n" +
            "\n" +
            "uniform mat4 uMVPMatrix;  // MVP 的变换矩阵（整体变形）\n" +
            "uniform mat4 uTexMatrix;  // Texture 的变换矩阵 （只对texture变形）\n" +
            "\n" +
            "uniform float uTexelWidthOffset;\n" +
            "uniform float uTexelHeightOffset;\n" +
            "\n" +
            "attribute vec4 aPosition;\n" +
            "attribute vec4 aTextureCoord;\n" +
            "\n" +
            "varying vec2 vTextureCoord;\n" +
            "varying vec2 vBlurTextureCoord[SAMPLES];\n" +
            "\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = uMVPMatrix * aPosition;\n" +
            "    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n" +
            "\n" +
            "    int multiplier = 0;\n" +
            "    vec2 blurStep;\n" +
            "    vec2 offset = vec2(uTexelHeightOffset, uTexelWidthOffset);\n" +
            "\n" +
            "    for (int i = 0; i < SAMPLES; i++)\n" +
            "    {\n" +
            "       multiplier = (i - ((SAMPLES-1) / 2));\n" +
            "       // ToneCurve in x (horizontal)\n" +
            "       blurStep = float(multiplier) * offset;\n" +
            "       vBlurTextureCoord[i] = vTextureCoord + blurStep;\n" +
            "    }\n" +
            "}";

    public String F = "#extension GL_OES_EGL_image_external : require\n" +
            "#define SAMPLES 9\n" +
            "precision highp float;\n" +
            "uniform samplerExternalOES uTexture;\n" +
            "\n" +
            "varying vec2 vTextureCoord;\n" +
            "varying vec2 vBlurTextureCoord[SAMPLES];\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    vec3 sum = vec3(0.0);\n" +
            "    vec4 fragColor = texture2D(uTexture,vTextureCoord);\n" +
            "\n" +
            "\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[0]).rgb * 0.05;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[1]).rgb * 0.09;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[2]).rgb * 0.12;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[3]).rgb * 0.15;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[4]).rgb * 0.18;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[5]).rgb * 0.15;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[6]).rgb * 0.12;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[7]).rgb * 0.09;\n" +
            "    sum += texture2D(uTexture, vBlurTextureCoord[8]).rgb * 0.05;\n" +
            "\n" +
            "    gl_FragColor = vec4(sum,fragColor.a);\n" +
            "}";

    public CameraFilterGaussianSingleBlur(Context applicationContext, float blurRatio,
                                          boolean widthOrHeight) {
        super(applicationContext);
        mBlurRatio = blurRatio;
        mWidthOrHeight = widthOrHeight;
    }

    @Override
    protected int createProgram(Context applicationContext) {
        return GlUtil.createProgram(V,
                F);
    }

    @Override
    protected void getGLSLValues() {
        super.getGLSLValues();

        muTexelWidthOffset = GLES20.glGetUniformLocation(mProgramHandle, "uTexelWidthOffset");
        muTexelHeightOffset = GLES20.glGetUniformLocation(mProgramHandle, "uTexelHeightOffset");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex,
                                  int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix,
                texBuffer, texStride);

        if (mWidthOrHeight) {
            GLES20.glUniform1f(muTexelWidthOffset,
                    mIncomingWidth == 0 ? 0f : mBlurRatio / mIncomingWidth);
        } else {
            GLES20.glUniform1f(muTexelHeightOffset,
                    mIncomingHeight == 0 ? 0f : mBlurRatio / mIncomingHeight);
        }
    }
}
