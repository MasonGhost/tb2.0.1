package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.circle.publish.PublishPostActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.news.PublishInfoActivityV2;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.news.PublishQuestionActivityV2;

import dagger.Component;

/**
 * @Author Jliuer
 * @Date 2017/11/17/17:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MarkdownPresenterModule.class)
public interface MarkdownComponent extends InjectComponent<PublishPostActivity> {
    void inject(PublishInfoActivityV2 infoActivity);
    void inject(PublishQuestionActivityV2 questionActivity);
}
