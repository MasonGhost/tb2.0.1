package com.zhiyicx.baseproject.base;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/6
 * @Contact master.jungle68@gmail.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TSWebFragmentTest {
    TSWebFragment mTSWebFragment;

    @Before
    public void init() {
        mTSWebFragment = new TSWebFragment() {
            @Override
            protected void onWebImageClick(String clickUrl, List<String> images) {

            }

            @Override
            protected void onWebImageLongClick(String longClickUrl) {

            }
        };
    }

    /**
     * 测试网页图片解析
     */
    @Test
    public void getAllImageUrlFromHtml() {
        String html = "<html class=\"\"><head><meta name=\"viewport\" content=\"width=device-width, minimum-scale=0.1\"><title>20150821150205086 (368×364)</title><style type=\"text/css\">.fancybox-margin{margin-right:0px;}</style><style id=\"style-1-cropbar-clipper\">/* Copyright 2014 Evernote Corporation. All rights reserved. */\n" +
                ".en-markup-crop-options {\n" +
                "    top: 18px !important;\n" +
                "    left: 50% !important;\n" +
                "    margin-left: -100px !important;\n" +
                "    width: 200px !important;\n" +
                "    border: 2px rgba(255,255,255,.38) solid !important;\n" +
                "    border-radius: 4px !important;\n" +
                "}\n" +
                "\n" +
                ".en-markup-crop-options div div:first-of-type {\n" +
                "    margin-left: 0px !important;\n" +
                "}\n" +
                "</style></head><body style=\"margin: 0px;\"><img style=\"-webkit-user-select: none\" src=\"http://img.blog.csdn.net/20150821150205086\"></body></html>";
        List<String> images = mTSWebFragment.getAllImageUrlFromHtml(html);
        Assert.assertFalse(images.isEmpty());

    }

}