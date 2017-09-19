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

package com.jess.camerafilters.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

/**
 * Converts the image to a single-color version, based on the luminance of each pixel
 * intensity: The degree to which the specific color replaces the normal image color (0.0 - 1.0, with 1.0 as the default)
 * color: The color to use as the basis for the effect, with (0.6, 0.45, 0.3, 1.0) as the default.
 */
public class CameraAncientFilter extends CameraFilter {
    public static final String MONOCHROME_FRAGMENT_SHADER = "" +
            "#extension GL_OES_EGL_image_external : require\n" +
            " precision lowp float;\n" +
            "  \n" +
            "  varying highp vec2 vTextureCoord;\n" +
            "  \n" +
            "  uniform samplerExternalOES uTexture;\n" +
            "  uniform float intensity;\n" +
            "  uniform vec3 filterColor;\n" +
            "  \n" +
            "  const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);\n" +
            "  \n" +
            "  void main()\n" +
            "  {\n" +
            " 	//desat, then apply overlay blend\n" +
            " 	lowp vec4 textureColor = texture2D(uTexture, vTextureCoord);\n" +
            " 	float luminance = dot(textureColor.rgb, luminanceWeighting);\n" +
            " 	\n" +
            " 	lowp vec4 desat = vec4(vec3(luminance), 1.0);\n" +
            " 	\n" +
            " 	//overlay\n" +
            " 	lowp vec4 outputColor = vec4(\n" +
            "                                  (desat.r < 0.5 ? (2.0 * desat.r * filterColor.r) : (1.0 - 2.0 * (1.0 - desat.r) * (1.0 - filterColor.r))),\n" +
            "                                  (desat.g < 0.5 ? (2.0 * desat.g * filterColor.g) : (1.0 - 2.0 * (1.0 - desat.g) * (1.0 - filterColor.g))),\n" +
            "                                  (desat.b < 0.5 ? (2.0 * desat.b * filterColor.b) : (1.0 - 2.0 * (1.0 - desat.b) * (1.0 - filterColor.b))),\n" +
            "                                  1.0\n" +
            "                                  );\n" +
            " 	\n" +
            " 	//which is better, or are they equal?\n" +
            " 	gl_FragColor = vec4( mix(textureColor.rgb, outputColor.rgb, intensity), textureColor.a);\n" +
            "  }";

    private int mIntensityLocation;
    private float mIntensity = 0.7f;
    private int mFilterColorLocation;
    private float[] mColor=new float[] {0.58f, 0.45f, 0.3f, 1.0f};//0.6f, 0.45f, 0.3f, 1.0f

    public CameraAncientFilter(Context applicationContext) {
        super(applicationContext);
    }


    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,MONOCHROME_FRAGMENT_SHADER);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mIntensityLocation = GLES20.glGetUniformLocation(mProgramHandle, "intensity");
        mFilterColorLocation = GLES20.glGetUniformLocation(mProgramHandle, "filterColor");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        GLES20.glUniform1f(mIntensityLocation, mIntensity);
        GLES20.glUniform3fv(mFilterColorLocation, 1, FloatBuffer.wrap(mColor));
    }

}
