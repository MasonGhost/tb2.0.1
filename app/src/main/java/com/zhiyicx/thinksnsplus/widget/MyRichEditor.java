package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.util.AttributeSet;

import jp.wasabeef.richeditor.RichEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe 添加一个可以获取添加的图片集合的方法
 * @date 2017/7/24
 * @contact email:648129313@qq.com
 */

public class MyRichEditor extends RichEditor {

    private List<String> mImgUrlList;

    public MyRichEditor(Context context) {
        super(context);
        mImgUrlList = new ArrayList<>();
    }

    public MyRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        mImgUrlList = new ArrayList<>();
    }

    public MyRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImgUrlList = new ArrayList<>();
    }

    @Override
    public void insertImage(String url, String alt) {
        super.insertImage(url, alt);
        mImgUrlList.add(url);
    }

    /**
     * 对外提供追加的图片的方法
     * @return 图片集合
     */
    public List<String> getImgUrlList() {
        return mImgUrlList;
    }
}
