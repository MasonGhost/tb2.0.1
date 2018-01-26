package com.zhiyicx.thinksnsplus.modules.circle.detailv2.post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;

public class CirclePostDetailActivity extends TSActivity<CirclePostDetailPresenter, CirclePostDetailFragment> {

    @Override
    protected CirclePostDetailFragment getFragment() {
        return CirclePostDetailFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerCirclePostDetailComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .shareModule(new ShareModule(this))
                .circlePostDetailPresenterModule(new CirclePostDetailPresenterModule(mContanierFragment))
                .build().inject(this);
    }

    public static void startActivity(Context context, long circleId, long postId,boolean isLookMoreComment,boolean canGotoCircle) {
        Intent intent = new Intent(context, CirclePostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong(CirclePostDetailFragment.CIRCLE_ID, circleId);
        bundle.putLong(CirclePostDetailFragment.POST_ID, postId);
        bundle.putBoolean(CirclePostDetailFragment.BAKC2CIRCLE, canGotoCircle);
        bundle.putBoolean(CirclePostDetailFragment.LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, CirclePostListBean circlePostListBean,boolean isLookMoreComment,boolean canGotoCircle) {
        Intent intent = new Intent(context, CirclePostDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(CirclePostDetailFragment.POST,circlePostListBean);
        bundle.putBoolean(CirclePostDetailFragment.BAKC2CIRCLE, canGotoCircle);
        bundle.putBoolean(CirclePostDetailFragment.LOOK_COMMENT_MORE, isLookMoreComment);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
