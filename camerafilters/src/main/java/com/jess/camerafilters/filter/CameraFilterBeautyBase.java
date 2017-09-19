package com.jess.camerafilters.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.jess.camerafilters.filter.guiFilter.GPUImageFilter;
import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

/**
 * Created by shengwenhui on 16/3/3.
 */
public class CameraFilterBeautyBase extends GPUImageFilter {

    private int mSingleStepOffsetLocation;
    private int mParamsLocation;
    private int beautyHandle;

    public static final String F = "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "\n" +
            "varying mediump vec2 textureCoordinate;\n" +
            "\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "uniform vec2 singleStepOffset;\n" +
            "uniform mediump float params;\n" +
            "\n" +
            "const highp vec3 W = vec3(0.299,0.587,0.114);\n" +
            "vec2 blurCoordinates[20];\n" +
            "\n" +
            "float hardLight(float color)\n" +
            "{\n" +
            "\tif(color <= 0.5)\n" +
            "\t\tcolor = color * color * 2.0;\n" +
            "\telse\n" +
            "\t\tcolor = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);\n" +
            "\treturn color;\n" +
            "}\n" +
            "\n" +
            "void main(){\n" +
            "\n" +
            "    vec3 centralColor = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "    blurCoordinates[0] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -10.0);\n" +
            "    blurCoordinates[1] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 10.0);\n" +
            "    blurCoordinates[2] = textureCoordinate.xy + singleStepOffset * vec2(-10.0, 0.0);\n" +
            "    blurCoordinates[3] = textureCoordinate.xy + singleStepOffset * vec2(10.0, 0.0);\n" +
            "    blurCoordinates[4] = textureCoordinate.xy + singleStepOffset * vec2(5.0, -8.0);\n" +
            "    blurCoordinates[5] = textureCoordinate.xy + singleStepOffset * vec2(5.0, 8.0);\n" +
            "    blurCoordinates[6] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, 8.0);\n" +
            "    blurCoordinates[7] = textureCoordinate.xy + singleStepOffset * vec2(-5.0, -8.0);\n" +
            "    blurCoordinates[8] = textureCoordinate.xy + singleStepOffset * vec2(8.0, -5.0);\n" +
            "    blurCoordinates[9] = textureCoordinate.xy + singleStepOffset * vec2(8.0, 5.0);\n" +
            "    blurCoordinates[10] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, 5.0);\n" +
            "    blurCoordinates[11] = textureCoordinate.xy + singleStepOffset * vec2(-8.0, -5.0);\n" +
            "    blurCoordinates[12] = textureCoordinate.xy + singleStepOffset * vec2(0.0, -6.0);\n" +
            "    blurCoordinates[13] = textureCoordinate.xy + singleStepOffset * vec2(0.0, 6.0);\n" +
            "    blurCoordinates[14] = textureCoordinate.xy + singleStepOffset * vec2(6.0, 0.0);\n" +
            "    blurCoordinates[15] = textureCoordinate.xy + singleStepOffset * vec2(-6.0, 0.0);\n" +
            "    blurCoordinates[16] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, -4.0);\n" +
            "    blurCoordinates[17] = textureCoordinate.xy + singleStepOffset * vec2(-4.0, 4.0);\n" +
            "    blurCoordinates[18] = textureCoordinate.xy + singleStepOffset * vec2(4.0, -4.0);\n" +
            "    blurCoordinates[19] = textureCoordinate.xy + singleStepOffset * vec2(4.0, 4.0);\n" +
            "\n" +
            "    float sampleColor = centralColor.g * 20.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[0]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[1]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[2]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[3]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[4]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[5]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[6]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[7]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[8]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[9]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[10]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[11]).g;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[12]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[13]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[14]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[15]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[16]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[17]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[18]).g * 2.0;\n" +
            "    sampleColor += texture2D(inputImageTexture, blurCoordinates[19]).g * 2.0;\n" +
            "\n" +
            "    sampleColor = sampleColor / 48.0;\n" +
            "\n" +
            "    float highPass = centralColor.g - sampleColor + 0.5;\n" +
            "\n" +
            "    for(int i = 0; i < 5;i++)\n" +
            "    {\n" +
            "        highPass = hardLight(highPass);\n" +
            "    }\n" +
            "    float luminance = dot(centralColor, W);\n" +
            "\n" +
            "    float alpha = pow(luminance, params);\n" +
            "\n" +
            "    vec3 smoothColor = centralColor + (centralColor-vec3(highPass))*alpha*0.1;\n" +
            "\n" +
            "    gl_FragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centralColor), alpha), 1.0);\n" +
            "}";


    private float mLeval = 1.0f;

    public CameraFilterBeautyBase(Context context) {
        this.context = context;
        beautyHandle = GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,
                F);
        mProgramHandle = createProgram(context);
        if (mProgramHandle == 0) {
            throw new RuntimeException("Unable to create program");
        }
        getGLSLValues();

    }

    @Override
    public int createProgram(Context applicationContext) {

        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,
                F);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(beautyHandle, "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(beautyHandle, "params");

    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex,
                                  int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix,
                texBuffer, texStride);
        GLES20.glUniform2fv(mSingleStepOffsetLocation, 1, FloatBuffer.wrap(new float[]{2.0f / mIncomingWidth, 2.0f / mIncomingHeight}));
        GLES20.glUniform1f(mParamsLocation, mLeval);
    }
}

