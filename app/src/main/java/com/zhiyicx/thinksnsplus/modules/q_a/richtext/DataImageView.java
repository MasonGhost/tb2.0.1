package com.zhiyicx.thinksnsplus.modules.q_a.richtext;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.widget.imageview.FilterImageView;

/**
 * @Author Jliuer
 * @Date 2017/9/19 10:46
 * @Email Jliuer@aliyun.com
 * @Description 纯点数据
 */
public class DataImageView extends FilterImageView {

    private String absolutePath;
    private Bitmap bitmap;
    private int id;

    public DataImageView(Context context) {
        this(context, null);
    }

    public DataImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DataImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

}
