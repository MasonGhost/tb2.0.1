package com.zhiyicx.zhibolibrary.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.ActivityScope;
import com.zhiyicx.zhibolibrary.model.EndStreamModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.FollowInfo;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.presenter.common.BasePresenter;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLEndStreamingActivity;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLLivePlayActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.DefaultAdapter;
import com.zhiyicx.zhibolibrary.ui.adapter.RecommendListAdapter;
import com.zhiyicx.zhibolibrary.ui.view.EndStreamView;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.model.api.ZBApi;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/4/5.
 */
@ActivityScope
public class EndStreamPresenter extends BasePresenter<EndStreamModel, EndStreamView> implements OnShareCallbackListener {
    private ArrayList<SearchResult> mSearchResults;
    private RecommendListAdapter mAdapter;
    private Subscription mQuerySubscribe;
    private String mUserId;
    private UmengSharePolicyImpl mSharePolicy;
    private UserInfo mPresenterInfo;
    private ShareContent shareContent;

    @Inject
    public EndStreamPresenter(EndStreamModel model, EndStreamView rootView) {
        super(model, rootView);
        this.mSharePolicy = new UmengSharePolicyImpl(rootView.getActivity());
        mSharePolicy.setOnShareCallbackListener(this);
    }


    public void getIntent(Intent intent) {
        boolean isAudience = intent.getBooleanExtra("isAudience", true);
        mUserId = intent.getStringExtra("userId");
        if (isAudience) {//是观众
            mRootView.isAudience(true);
            Bundle bundle = intent.getExtras();
            try {
                SearchResult[] info = ((ApiList) bundle.getSerializable("info")).data;
                mPresenterInfo = (UserInfo) bundle.getSerializable("presenter");
                requestSearchResult(bundle);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            initAudienceLayout();//初始化观众结束界面


        } else {//是主播
            mRootView.isAudience(false);
            Bundle bundle = intent.getExtras();
            ZBEndStreamJson.InCome income = (ZBEndStreamJson.InCome) bundle.getSerializable("income");
            mPresenterInfo = (UserInfo) bundle.getSerializable("presenter");
            //因为重连失败弹出直播间显示异常提示
            if (intent.getBooleanExtra("isException", false)) {
                mRootView.showExceptionPrompt(true);
            }
            mRootView.setFans(income.view_count + "");
            mRootView.setStar(income.zan_count + "");
            mRootView.setGold(income.gold + "");
        }
        setShareContent();
    }

    private void setShareContent() {
        shareContent = new ShareContent();
        shareContent.setTitle(mPresenterInfo.uname);
        shareContent.setContent(TextUtils.isEmpty(mPresenterInfo.intro) ? UiUtils.getString(R.string.intro_default) : mPresenterInfo.intro);
        shareContent.setUrl(String.format(ApiConfig.APP_DOMAIN + ApiConfig.APP_PATH_SHARE_USERINFO, mPresenterInfo.uid));
        if (mPresenterInfo.avatar == null || mPresenterInfo.avatar.getOrigin() == null) {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(UiUtils.getResources(), R.mipmap
                    .pic_default_secret)));
        } else {
            shareContent.setImage(mPresenterInfo.avatar.getOrigin());
        }
        mSharePolicy.setShareContent(shareContent);
    }


    /**
     * 分享朋友圈
     *
     * @param endStreamingActivity
     */
    public void shareMoment(final ZBLEndStreamingActivity endStreamingActivity) {
        // 友盟不支持重定向图片
        if (shareContent == null || shareContent.getImage() == null) {
            mSharePolicy.shareMoment(endStreamingActivity, this);

        } else {
            Glide.with(UiUtils.getContext()).load(shareContent.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareContent.setBitmap(resource);
                    mSharePolicy.setShareContent(shareContent);
                    mSharePolicy.shareMoment(endStreamingActivity, EndStreamPresenter.this);

                }
            });
        }

    }


    /**
     * 分享微信
     *
     * @param endStreamingActivity
     */
    public void shareWechat(final ZBLEndStreamingActivity endStreamingActivity) {
        if (shareContent == null || shareContent.getImage() == null) {
            mSharePolicy.shareWechat(endStreamingActivity, this);


        } else {
            Glide.with(UiUtils.getContext()).load(shareContent.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareContent.setBitmap(resource);
                    mSharePolicy.setShareContent(shareContent);
                    mSharePolicy.shareWechat(endStreamingActivity, EndStreamPresenter.this);

                }
            });
        }
    }

    /**
     * 分享微博
     *
     * @param endStreamingActivity
     */
    public void shareWeibo(final ZBLEndStreamingActivity endStreamingActivity) {
        if (shareContent == null || shareContent.getImage() == null) {
            mSharePolicy.shareWeibo(endStreamingActivity, this);


        } else {
            Glide.with(UiUtils.getContext()).load(shareContent.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareContent.setBitmap(resource);
                    mSharePolicy.setShareContent(shareContent);
                    mSharePolicy.shareWeibo(endStreamingActivity, EndStreamPresenter.this);

                }
            });
        }
    }

    /**
     * 分享qq
     *
     * @param endStreamingActivity
     */
    public void shareQQ(final ZBLEndStreamingActivity endStreamingActivity) {
        if (shareContent == null || shareContent.getImage() == null) {
            mSharePolicy.shareQQ(endStreamingActivity, this);


        } else {
            Glide.with(UiUtils.getContext()).load(shareContent.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareContent.setBitmap(resource);
                    mSharePolicy.setShareContent(shareContent);
                    mSharePolicy.shareQQ(endStreamingActivity, EndStreamPresenter.this);

                }
            });
        }
    }

    /**
     * 分享qq空间
     *
     * @param endStreamingActivity
     */
    public void shareZone(final ZBLEndStreamingActivity endStreamingActivity) {
        if (shareContent == null || shareContent.getImage() == null) {
            mSharePolicy.shareZone(endStreamingActivity, this);


        } else {
            Glide.with(UiUtils.getContext()).load(shareContent.getImage()).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    shareContent.setBitmap(resource);
                    mSharePolicy.setShareContent(shareContent);
                    mSharePolicy.shareZone(endStreamingActivity, EndStreamPresenter.this);

                }
            });
        }
    }

    private void requestSearchResult(Bundle bundle) {
        SearchResult[] datas = ((ApiList) bundle.getSerializable("info")).data;
        mRootView.setFans(bundle.getInt("view_count") + "");
        initAdapter();//初始化
        for (SearchResult data : datas) {//添加数据
            mSearchResults.add(data);
        }
        if (datas.length == 2) {//如果只有两个填充到三个,为了展示效果好看
            mSearchResults.add(datas[datas.length - 1]);
        }
        mRootView.setAdapter(mAdapter);//设置适配器
    }


    /**
     * 初始化adapter
     */
    private void initAdapter() {
        if (mSearchResults == null) {//如果list为空说明第一次拉取列表
            mSearchResults = new ArrayList<>();
            mAdapter = new RecommendListAdapter(mSearchResults);
            initItemListener();
        }
    }

    private void initItemListener() {
        mAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener<SearchResult>() {
            @Override
            public void onItemClick(View view, SearchResult data) {
                startPlay(data);
            }
        });
    }


    /**
     * 关注用户
     */
    public void follow() {
        mModel.followUser(UserService.STATUS_FOLLOW + "", mUserId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribe(new Action1<BaseJson<FollowInfo>>() {
            @Override
            public void call(BaseJson<FollowInfo> json) {
                if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                    mRootView.showMessage(UiUtils.getString("str_follow_success"));
                    mRootView.setFollowStatus(true);//更改关注按钮状态
                } else {
                    mRootView.showMessage(json.message);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
                mRootView.showMessage(UiUtils.getString("str_net_erro"));
            }
        });
    }

    /**
     * 查询关注状态
     */
    public void queryFollow() {
        if (mUserId == null || ZhiboApplication.getUserInfo() == null) {
            return;

        }
        mQuerySubscribe = mModel.followUser(UserService.STATUS_FOLLOW_QUERY + "", mUserId
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<FollowInfo>>() {
                    @Override
                    public void call(BaseJson<FollowInfo> json) {
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS)) {
                            mRootView.setFollowStatus(isFollow(json.data.is_follow));//更改关注按钮状态
                        } else {
                            mRootView.showMessage(json.message);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    /**
     * 是否关注
     *
     * @param i
     * @return
     */
    public static boolean isFollow(int i) {
        if (i == 1) {
            return true;
        }
        return false;
    }


    private void startPlay(SearchResult data) {
        Intent intent = new Intent(UiUtils.getContext(), ZBLLivePlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data);
        bundle.putBoolean("isVideo", false);
        intent.putExtras(bundle);
        mRootView.launchActivity(intent);
    }


    /**
     * 初始化观众结束页面的布局
     */
    private void initAudienceLayout() {
        mRootView.setGoldVisible(false);//隐藏金币
        mRootView.setStarVisible(false);//隐藏赞
        mRootView.setRecycleViewVisible(true);//显示recycleview
        mRootView.initListener();
        queryFollow();//查询关注状态
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe(mQuerySubscribe);//解除订阅
    }


    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showMessage(UiUtils.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showMessage(UiUtils.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showMessage(UiUtils.getString(R.string.share_cancel));

    }
}
