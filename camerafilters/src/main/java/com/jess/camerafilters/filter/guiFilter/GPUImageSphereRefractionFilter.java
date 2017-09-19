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

public class GPUImageSphereRefractionFilter extends GPUImageFilter {
    public static final String SPHERE_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "\n" +
            "uniform highp vec2 center;\n" +
            "uniform highp float radius;\n" +
            "uniform highp float aspectRatio;\n" +
            "uniform highp float refractiveIndex;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "highp vec2 textureCoordinateToUse = vec2(textureCoordinate.x, (textureCoordinate.y * aspectRatio + 0.5 - 0.5 * aspectRatio));\n" +
            "highp float distanceFromCenter = distance(center, textureCoordinateToUse);\n" +
            "lowp float checkForPresenceWithinSphere = step(distanceFromCenter, radius);\n" +
            "\n" +
            "distanceFromCenter = distanceFromCenter / radius;\n" +
            "\n" +
            "highp float normalizedDepth = radius * sqrt(1.0 - distanceFromCenter * distanceFromCenter);\n" +
            "highp vec3 sphereNormal = normalize(vec3(textureCoordinateToUse - center, normalizedDepth));\n" +
            "\n" +
            "highp vec3 refractedVector = refract(vec3(0.0, 0.0, -1.0), sphereNormal, refractiveIndex);\n" +
            "\n" +
            "gl_FragColor = texture2D(inputImageTexture, (refractedVector.xy + 1.0) * 0.5) * checkForPresenceWithinSphere;     \n" +
            "}\n";

    private PointF mCenter;
    private int mCenterLocation;
    private float mRadius;
    private int mRadiusLocation;
    private float mAspectRatio;
    private int mAspectRatioLocation;
    private float mRefractiveIndex;
    private int mRefractiveIndexLocation;


    public GPUImageSphereRefractionFilter(Context context) {
        super(context);
        mCenter = new PointF(0.5f, 0.5f);//The center about which to apply the distortion, with a default of (0.5, 0.5)
        mRadius = 0.25f;//The radius of the distortion, ranging from 0.0 to 1.0, with a default of 0.25
        mRefractiveIndex = 0.71f;//The index of refraction for the sphere, with a default of 0.71
    }

    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,SPHERE_FRAGMENT_SHADER);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mCenterLocation = GLES20.glGetUniformLocation(mProgramHandle, "center");
        mRadiusLocation = GLES20.glGetUniformLocation(mProgramHandle, "radius");
        mAspectRatioLocation = GLES20.glGetUniformLocation(mProgramHandle, "aspectRatio");
        mRefractiveIndexLocation = GLES20.glGetUniformLocation(mProgramHandle, "refractiveIndex");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        GLES20.glUniform1f(mRadiusLocation,mRadius);
        float[] vec2 = new float[2];
        vec2[0] = mCenter.x;
        vec2[1] = mCenter.y;
        GLES20.glUniform2fv(mCenterLocation, 1, vec2, 0);
        GLES20.glUniform1f(mRefractiveIndexLocation, mRefractiveIndex);
        mAspectRatio = (float) mIncomingHeight / mIncomingWidth;
        GLES20.glUniform1f(mAspectRatioLocation, mAspectRatio);


    }
}
