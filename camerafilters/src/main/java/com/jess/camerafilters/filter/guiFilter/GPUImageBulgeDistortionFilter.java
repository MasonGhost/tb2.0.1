/*
 * Copyright (C) 2012 CyberAgent
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jess.camerafilters.filter.guiFilter;

import android.content.Context;
import android.graphics.PointF;
import android.opengl.GLES20;

import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

public class GPUImageBulgeDistortionFilter extends GPUImageFilter {
    public static final String BULGE_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require \n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "\n" +
            "uniform highp float aspectRatio;\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float scale;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n" +
            "highp float dist = distance(center, textureCoordinateToUse);\n" +
            "textureCoordinateToUse = textureCoordinate;\n" +
            "\n" +
            "if (dist < radius)\n" +
            "{\n" +
            "textureCoordinateToUse -= center;\n" +
            "highp float percent = 1.0 - ((radius - dist) / radius) * scale;\n" +
            "percent = percent * percent;\n" +
            "\n" +
            "textureCoordinateToUse = textureCoordinateToUse * percent;\n" +
            "textureCoordinateToUse += center;\n" +
            "}\n" +
            "\n" +
            "gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );    \n" +
            "}\n";

    private float mScale = 0.5f;
    private int mScaleLocation;
    private float mRadius = 0.25f;
    private int mRadiusLocation;
    private PointF mCenter = new PointF(0.5f,0.5f);
    private int mCenterLocation;
    private float mAspectRatio;
    private int mAspectRatioLocation;

    public GPUImageBulgeDistortionFilter(Context applicationContext) {
        super(applicationContext);
    }


    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,BULGE_FRAGMENT_SHADER);
    }



    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mScaleLocation = GLES20.glGetUniformLocation(mProgramHandle, "scale");
        mRadiusLocation = GLES20.glGetUniformLocation(mProgramHandle, "radius");
        mCenterLocation = GLES20.glGetUniformLocation(mProgramHandle, "center");
        mAspectRatioLocation = GLES20.glGetUniformLocation(mProgramHandle, "aspectRatio");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        GLES20.glUniform1f(mRadiusLocation, mRadius);
        GLES20.glUniform1f(mScaleLocation, mScale);
        float[] vec2 = new float[2];
        vec2[0] = mCenter.x;
        vec2[1] = mCenter.y;
        GLES20.glUniform2fv(mCenterLocation, 1, vec2, 0);


        GLES20.glUniform1f(mAspectRatioLocation, mAspectRatio);//??
    }



    @Override
    public void setTextureSize(int width, int height) {
        super.setTextureSize(width, height);
        mAspectRatio = (float) height / width;
        //setAspectRatio(mAspectRatio);??
    }

}
