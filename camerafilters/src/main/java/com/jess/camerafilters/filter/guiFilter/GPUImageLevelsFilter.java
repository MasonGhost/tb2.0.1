package com.jess.camerafilters.filter.guiFilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

/**
 * Created by vashisthg 30/05/14.
 */
public class GPUImageLevelsFilter extends GPUImageFilter {

    private static final String LOGTAG = GPUImageLevelsFilter.class.getSimpleName();

    public static final String LEVELS_FRAGMET_SHADER =

            "#extension GL_OES_EGL_image_external : require\n" +
                    " varying highp vec2 textureCoordinate;\n" +
                    " \n" +
                    " uniform samplerExternalOES inputImageTexture;\n" +
                    " uniform mediump vec3 levelMinimum;\n" +
                    " uniform mediump vec3 levelMiddle;\n" +
                    " uniform mediump vec3 levelMaximum;\n" +
                    " uniform mediump vec3 minOutput;\n" +
                    " uniform mediump vec3 maxOutput;\n" +
                    " \n" +
                    " void main()\n" +
                    " {\n" +
                    "     mediump vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                    "     \n" +
                    "     gl_FragColor = vec4( mix(minOutput, maxOutput, pow(min(max(textureColor.rgb -levelMinimum, vec3(0.0)) / (levelMaximum - levelMinimum  ), vec3(1.0)), 1.0 /levelMiddle)) , textureColor.a);\n" +
                    " }\n";

    private int mMinLocation;
    private float[] mMin;
    private int mMidLocation;
    private float[] mMid;
    private int mMaxLocation;
    private float[] mMax;
    private int mMinOutputLocation;
    private float[] mMinOutput;
    private int mMaxOutputLocation;
    private float[] mMaxOutput;

    public GPUImageLevelsFilter(Context context) {
        super(context);

        mMin = new float[]{0.0f, 0.0f, 0.0f};
        mMid = new float[]{1.0f, 1.0f, 1.0f};
        mMax = new float[]{1.0f, 1.0f, 1.0f};
        mMinOutput = new float[]{0.0f, 0.0f, 0.0f};
        mMaxOutput = new float[]{1.0f, 1.0f, 1.0f};
        setMin(0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER, LEVELS_FRAGMET_SHADER);
    }

    @Override
    public void getGLSLValues() {
        super.getGLSLValues();
        mMinLocation = GLES20.glGetUniformLocation(mProgramHandle, "levelMinimum");
        mMidLocation = GLES20.glGetUniformLocation(mProgramHandle, "levelMiddle");
        mMaxLocation = GLES20.glGetUniformLocation(mProgramHandle, "levelMaximum");
        mMinOutputLocation = GLES20.glGetUniformLocation(mProgramHandle, "minOutput");
        mMaxOutputLocation = GLES20.glGetUniformLocation(mProgramHandle, "maxOutput");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex, int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix, texBuffer, texStride);
        GLES20.glUniform3fv(mMinLocation, 1, FloatBuffer.wrap(mMin));
        GLES20.glUniform3fv(mMidLocation, 1, FloatBuffer.wrap(mMid));
        GLES20.glUniform3fv(mMaxLocation, 1, FloatBuffer.wrap(mMax));
        GLES20.glUniform3fv(mMinOutputLocation, 1, FloatBuffer.wrap(mMinOutput));
        GLES20.glUniform3fv(mMaxOutputLocation, 1, FloatBuffer.wrap(mMaxOutput));


    }


    public void setMin(float min, float mid, float max, float minOut, float maxOut) {
        setRedMin(min, mid, max, minOut, maxOut);
        setGreenMin(min, mid, max, minOut, maxOut);
        setBlueMin(min, mid, max, minOut, maxOut);
    }

    public void setMin(float min, float mid, float max) {
        setMin(min, mid, max, 0.0f, 1.0f);
    }

    public void setRedMin(float min, float mid, float max, float minOut, float maxOut) {
        mMin[0] = min;
        mMid[0] = mid;
        mMax[0] = max;
        mMinOutput[0] = minOut;
        mMaxOutput[0] = maxOut;
    }

    public void setRedMin(float min, float mid, float max) {
        setRedMin(min, mid, max, 0, 1);
    }

    public void setGreenMin(float min, float mid, float max, float minOut, float maxOut) {
        mMin[1] = min;
        mMid[1] = mid;
        mMax[1] = max;
        mMinOutput[1] = minOut;
        mMaxOutput[1] = maxOut;
    }

    public void setGreenMin(float min, float mid, float max) {
        setGreenMin(min, mid, max, 0, 1);
    }

    public void setBlueMin(float min, float mid, float max, float minOut, float maxOut) {
        mMin[2] = min;
        mMid[2] = mid;
        mMax[2] = max;
        mMinOutput[2] = minOut;
        mMaxOutput[2] = maxOut;
    }

    public void setBlueMin(float min, float mid, float max) {
        setBlueMin(min, mid, max, 0, 1);
    }
}
