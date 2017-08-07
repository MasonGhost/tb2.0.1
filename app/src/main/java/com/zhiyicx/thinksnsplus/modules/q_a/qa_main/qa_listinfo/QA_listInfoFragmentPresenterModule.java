package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import com.zhiyicx.thinksnsplus.data.source.repository.QA_ListInfoRepostory;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/07/25/13:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class QA_listInfoFragmentPresenterModule {
    QA_ListInfoConstact.View mView;

    public QA_listInfoFragmentPresenterModule(QA_ListInfoConstact.View view) {
        mView = view;
    }

    @Provides
    QA_ListInfoConstact.View provideQA$ListInfoConstactView(){
        return mView;
    }

    @Provides
    QA_ListInfoConstact.Repository provideQA$ListInfoConstactRepository(QA_ListInfoRepostory qa_listInfoRepostory){
        return qa_listInfoRepostory;
    }
}
