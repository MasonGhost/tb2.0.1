package com.zhiyicx.thinksnsplus.data.beans.report;

import com.zhiyicx.thinksnsplus.modules.report.ReportType;

import java.io.Serializable;

/**
 * @Describe 要举报的内容说明
 * @Author Jungle68
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportResourceBean implements Serializable{
    private static final long serialVersionUID = -8484328463541856394L;
    /**
     * id 要举报资源的 id
     * title 要举报资源的标题
     * img 要举报资源的图片资源
     * des 要举报资源的内容
     */
    private String id;
    private String title;
    private String img;
    private String des;
    private ReportType type;

    public ReportResourceBean() {
    }

    public ReportResourceBean(String id, String title, String img, String des, ReportType type) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.des = des;
        this.type = type;
    }
}
