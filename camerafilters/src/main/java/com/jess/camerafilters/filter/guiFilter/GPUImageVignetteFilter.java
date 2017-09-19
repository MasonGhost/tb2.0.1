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

/**
 * Performs a vignetting effect, fading out the image at the edges
 * x:
 * y: The directional intensity of the vignetting, with a default of x = 0.75, y = 0.5
 */
public class GPUImageVignetteFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            " uniform samplerExternalOES inputImageTexture;\n" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform lowp vec2 vignetteCenter;\n" +
            " uniform lowp vec3 vignetteColor;\n" +
            " uniform highp float vignetteStart;\n" +
            " uniform highp float vignetteEnd;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     /*\n" +
            "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n" +
            "     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n" +
            "     gl_FragColor = vec4(vec3(rgb),1.0);\n" +
            "      */\n" +
            "     \n" +
            "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n" +
            "     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n" +
            "     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n" +
            " }";

    private int mVignetteCenterLocation;
    private PointF mVignetteCenter = new PointF();
    private int mVignetteColorLocation;
    private float[] mVignetteColor = new float[] {0.0f, 0.0f, 0.0f};
    private int mVignetteStartLocation;
    private float mVignetteStart = 0.3f;
    private int mVignetteEndLocation;
    private float mVignetteEnd = 0.75f;
    
    public GPUImageVignetteFilter(Context context) {
        super(context);
    }

    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,VIGNETTING_FRAGMENT_SHADER);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mVignetteCenterLocation = GLES20.glGetUniformLocation(mProgramHandle, "vignetteCenter");
        mVignetteColorLocation = GLES20.glGetUniformLocation(mProgramHandle, "vignetteColor");
        mVignetteStartLocation = GLES20.glGetUniformLocation(mProgramHandle, "vignetteStart");
        mVignetteEndLocation = GLES20.glGetUniformLocation(mProgramHandle, "vignetteEnd");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        float[] vec2 = new float[2];
        vec2[0] = mVignetteCenter.x;
        vec2[1] = mVignetteCenter.y;
        GLES20.glUniform2fv(mVignetteCenterLocation, 1, vec2, 0);
        GLES20.glUniform3fv(mVignetteColorLocation, 1, FloatBuffer.wrap(mVignetteColor));
        GLES20.glUniform1f(mVignetteStartLocation, mVignetteStart);
        GLES20.glUniform1f(mVignetteEndLocation, mVignetteEnd);
    }
}
