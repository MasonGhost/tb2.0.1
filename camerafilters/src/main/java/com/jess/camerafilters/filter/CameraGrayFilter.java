package com.jess.camerafilters.filter;

import android.content.Context;

import com.jess.camerafilters.R;
import com.jess.camerafilters.util.GlUtil;

/**
 * Created by lei on 2016/9/9.
 */
public class CameraGrayFilter extends CameraFilter {
    public final String GRAY_FRAGMENT_SHADER = "#extension GL_OES_EGL_image_external : require\n" +
            "\n" +
            "precision highp float;\n" +
            "\n" +
            "varying vec2 vTextureCoord;\n" +
            "\n" +
            "uniform samplerExternalOES uTexture;\n" +
            "\n" +
            "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
            "\n" +
            "void main(){\n" +
            "\n" +
            "lowp vec4 textureColor = texture2D(uTexture, vTextureCoord);\n" +
            "float luminance = dot(textureColor.rgb, W);\n" +
            "\n" +
            "gl_FragColor = vec4(vec3(luminance), textureColor.a);\n" +
            "}";
    public CameraGrayFilter(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public int createProgram(Context applicationContext) {
        return GlUtil.createProgram(NO_FILTER_VERTEX_SHADER,
                GRAY_FRAGMENT_SHADER);
    }
}
