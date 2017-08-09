package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.dynamic.topdynamic_comment.DynamicCommentTopContract;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.topinfo_comment.InfoCommentTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/06/02/11:07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoCommentTopRepository implements InfoCommentTopContract.Repository {

    InfoMainClient mInfoMainClient;


    @Inject
    public InfoCommentTopRepository(ServiceManager serviceManager) {
        mInfoMainClient = serviceManager.getInfoMainClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(long news_id, long comment_id, double amount, int day) {
        return mInfoMainClient.stickTopInfoComment(news_id, comment_id, (int) amount, day)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
