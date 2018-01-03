package com.zhiyicx.thinksnsplus.utils;

import br.tiagohm.markdownview.css.InternalStyleSheet;
import br.tiagohm.markdownview.css.StyleSheet;
import br.tiagohm.markdownview.css.styles.Github;

/**
 * @author LeiYan
 * @describe MarkDown 规范css 样式
 * @date 2017/12/14
 * @contact email:907238552@qq.com
 */

public class MarkDownRule {

    /**
     * markdown 内容显示规范
     *
     * @return
     */
    public static StyleSheet generateStandardStyle() {
        InternalStyleSheet css = new Github();
        css.addRule(".container", "padding-right:0", ";padding-left:0", "text-align:justify","text-align-last:left",
                "width: 100%","word-wrap:break-word","word-break:break-all","overflow: hidden");
        css.addRule("body", "line-height: 1.59", "padding: 0px", "font-size: 17px", "color: #333333");
        css.addRule("h1", "color: #333333", "size: 25px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h2", "color: #333333", "size: 23px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h3", "color: #333333", "size: 21px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("h4", "color: #333333", "size: 19px", "margin-top: 30px", "magin-bottom: 30px", "text-align: left");
        css.addRule("img", "margin-top: 20px", "margin-bottom: 20px","align:center", "margin: 0 auto","max-width: 100%", "display: block");
        css.addRule("p", "margin:0 0 10px", "width: 100%");
        /*设置 a 标签文字颜色，不知道为什么，要这样混合才能有效*/
        css.addMedia("color: #59b6d7; a:link {color: #59b6d7}");
        css.endMedia();
        css.addRule("a", "font-weight: bold");
        return css;
    }

    /**
     * markdown 摘要/引用规范
     * @return
     */
    public static StyleSheet generateStandardQuoteStyle() {
        InternalStyleSheet css = (InternalStyleSheet) generateStandardStyle();
        css.addRule("body", "line-height: 1.6", "padding: 0px", "background-color: #f4f5f5");
        css.addRule("blockquote", "margin:0px", "padding:0px", "border-left:5px solid #e3e3e3");
        css.addRule("p", "margin:0px", "padding:15px", "font-size: 15px","color: #666666");
        return css;
    }
}
