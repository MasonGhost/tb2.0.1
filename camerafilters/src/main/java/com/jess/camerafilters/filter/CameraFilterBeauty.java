package com.jess.camerafilters.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.jess.camerafilters.R;
import com.jess.camerafilters.util.GlUtil;

import java.nio.FloatBuffer;

/**
 * Created by shengwenhui on 16/3/3.
 */
public class CameraFilterBeauty extends CameraFilter {
    private int singleStepOffset;

    private static final float offset_array[] = {
            2, 2,
    };
    private int mSingleStepOffsetLocation;
    private int mParamsLocation;

    public CameraFilterBeauty(Context context) {
        super(context);
        offset_array[0] = offset_array[0] / mIncomingWidth;
        offset_array[1] = offset_array[1] / mIncomingHeight;
    }

    @Override
    protected int createProgram(Context applicationContext) {
        return GlUtil.createProgram(applicationContext, R.raw.vertex_shader,
                R.raw.fragment_shader_beauty);
    }

    @Override
    protected void getGLSLValues() {
        super.getGLSLValues();

        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(mProgramHandle, "singleStepOffset");
        mParamsLocation = GLES20.glGetUniformLocation(mProgramHandle, "params");
      //  singleStepOffset = GLES20.glGetUniformLocation(mProgramHandle, "singleStepOffset");
    }

    @Override
    protected void bindGLSLValues(float[] mvpMatrix, FloatBuffer vertexBuffer, int coordsPerVertex,
                                  int vertexStride, float[] texMatrix, FloatBuffer texBuffer, int texStride) {
        super.bindGLSLValues(mvpMatrix, vertexBuffer, coordsPerVertex, vertexStride, texMatrix,
                texBuffer, texStride);

       // GLES20.glUniform2fv(singleStepOffset, 1, offset_array, 0);
        GLES20.glUniform2fv(mSingleStepOffsetLocation, 1, FloatBuffer.wrap(new float[]{2.0f / mIncomingWidth, 2.0f / mIncomingHeight}));
        GLES20.glUniform1f(mParamsLocation, 0.6f);
    }
}

