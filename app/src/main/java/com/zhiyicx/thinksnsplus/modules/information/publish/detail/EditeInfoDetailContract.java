package com.zhiyicx.thinksnsplus.modules.information.publish.detail;

import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;

/**
 * @Author Jliuer
 * @Date 2018/01/24/11:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface EditeInfoDetailContract {
    interface View extends MarkdownContract.View<Presenter>{}
    interface Presenter extends MarkdownContract.Presenter{}
}
