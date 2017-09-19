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

import java.nio.FloatBuffer;

/**
 * Creates a swirl distortion on the image.
 */
public class GPUImageSwirlFilter extends GPUImageFilter {
    public static final String SWIRL_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float angle;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "highp vec2 textureCoordinateToUse = textureCoordinate;\n" +
            "highp float dist = distance(center, textureCoordinate);\n" +
            "if (dist < radius)\n" +
            "{\n" +
            "textureCoordinateToUse -= center;\n" +
            "highp float percent = (radius - dist) / radius;\n" +
            "highp float theta = percent * percent * angle * 8.0;\n" +
            "highp float s = sin(theta);\n" +
            "highp float c = cos(theta);\n" +
            "textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));\n" +
            "textureCoordinateToUse += center;\n" +
            "}\n" +
            "\n" +
            "gl_FragColor = texture2D(inputImageTexture, textureCoordinateToUse );\n" +
            "\n" +
            "}\n";

    private float mAngle;
    private int mAngleLocation;
    private float mRadius;
    private int mRadiusLocation;
    private PointF mCenter;
    private int mCenterLocation;


    public GPUImageSwirlFilter(Context context) {
        super(context);
        mRadius = 0.5f;//The radius of the distortion, ranging from 0.0 to 1.0, with a default of 0.5.
        mAngle = 1.0f;//The amount of distortion to apply, with a minimum of 0.0 and a default of 1.0.
        mCenter = new PointF(0.5f, 0.5f);//The center about which to apply the distortion, with a default of (0.5, 0.5).
    }

    @Override
    public int createProgram(Context applicationContext) {
        return super.createProgram(applicationContext);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mAngleLocation = GLES20.glGetUniformLocation(mProgramHandle, "angle");
        mRadiusLocation = GLES20.glGetUniformLocation(mProgramHandle, "radius");
        mCenterLocation = GLES20.glGetUniformLocation(mProgramHandle, "center");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        GLES20.glUniform1f(mRadiusLocation, mRadius);
        GLES20.glUniform1f(mAngleLocation, mAngle);
        float[] vec2 = new float[2];
        vec2[0] = mCenter.x;
        vec2[1] = mCenter.y;
        GLES20.glUniform2fv(mCenterLocation, 1, vec2, 0);
    }

}
