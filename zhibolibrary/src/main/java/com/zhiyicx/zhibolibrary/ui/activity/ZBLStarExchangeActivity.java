package com.zhiyicx.zhibolibrary.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerStarExchangeComponent;
import com.zhiyicx.zhibolibrary.di.module.StarExchangeModule;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.GoldService;
import com.zhiyicx.zhibolibrary.model.entity.StarExchangeList;
import com.zhiyicx.zhibolibrary.presenter.StarExchangePresenter;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.ui.components.ExchangeItemView;
import com.zhiyicx.zhibolibrary.ui.view.StarExchangeView;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import javax.inject.Inject;

/**
 * Created by jess on 16/4/25.
 */
public class ZBLStarExchangeActivity extends ZBLBaseActivity implements StarExchangeView, View.OnClickListener {
    TextView mGoldTV;
    TextView mStarTV;
    LinearLayout mRegularLL;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private int mCostStar;
    private ProgressDialog mProgressBuilder;

    @Inject
    StarExchangePresenter mPresenter;

    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_star_exchange);
        mGoldTV = (TextView) findViewById(R.id.tv_gold_exchange_count);
        mStarTV = (TextView) findViewById(R.id.tv_star_count);
        mRegularLL = (LinearLayout) findViewById(R.id.ll_star_exchange_regular);
        findViewById(R.id.rl_star_exchange_back).setOnClickListener(this);
        // 记录和ts+合并故不需要了
        findViewById(R.id.rl_star_history).setVisibility(View.GONE);
//        findViewById(R.id.rl_star_history).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        DaggerStarExchangeComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .starExchangeModule(new StarExchangeModule(this))
                .build()
                .inject(this);
        mPresenter.obtainToken();//获得用于交易的token
        updatedGold();//刷新金币信息
        updateStar();//刷新赞的信息
        if (ZBLApi.EXCHANGE_TYPE_LIST == null || ZBLApi.EXCHANGE_TYPE_LIST.zan_list == null) {
            mPresenter.getConfig();
        } else {
            initRegular();
        }
        initDialog();
    }


    /**
     * 初始化兑换的规则
     */
    @Override
    public void initRegular() {
        for (int x = 0; x < ZBLApi.EXCHANGE_TYPE_LIST.zan_list.length; x++) {
            final StarExchangeList.StarExchange s = ZBLApi.EXCHANGE_TYPE_LIST.zan_list[x];
            ExchangeItemView view = new ExchangeItemView(UiUtils.getContext());
            mRegularLL.addView(view);
            view.setIcon1(R.mipmap.ic_me_heart);
            view.setIcon2(R.mipmap.ic_me_goldcoins);
            view.setTitle(s.zan + "");
            view.setSubtitle(s.gold + "");
            if (x == ZBLApi.EXCHANGE_TYPE_LIST.zan_list.length - 1) {
                view.cleanDivider(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkStar(s);
                }
            });
        }
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        mBuilder = new AlertDialog.Builder(ZBLStarExchangeActivity.this);
        mBuilder.setNegativeButton(UiUtils.getString("cancel"), null);
        mBuilder.setPositiveButton(UiUtils.getString("str_ok"), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.exchange(mCostStar);//请求服务器兑换
            }
        });
        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        //进度条
        mProgressBuilder = new ProgressDialog(this);
        mProgressBuilder.setMessage(UiUtils.getString(R.string.str_exchanging));
        mProgressBuilder.setCanceledOnTouchOutside(false);
    }

    /**
     * 检查赞的数量,提示用户
     *
     * @param s
     */
    private void checkStar(StarExchangeList.StarExchange s) {
        if (ZhiboApplication.userInfo.zan_remain < s.zan) {
            showMessage("您的赞数量不足~");
            return;
        }
        showPrompt(s.zan, s.gold);
    }

    /**
     * 提示用户兑换的金币数
     *
     * @param zan
     * @param gold
     */
    @Override
    public void showPrompt(int zan, int gold) {
        mCostStar = zan;
        mDialog.setTitle(getString(R.string.str_is_exchange));
        mDialog.setMessage(String.format(UiUtils.getString(R.string.str_exchange_detail), zan, gold));
        mDialog.show();
    }

    @Override
    public void updatedGold() {
        mGoldTV.setText(ZhiboApplication.userInfo.getDisPlayGold());
    }

    @Override
    public void updateStar() {
        mStarTV.setText(ZhiboApplication.userInfo.zan_remain + "");
    }


    @Override
    public void onBackPressed() {
        killMyself();
    }

    @Override
    public void showLoading() {
        mProgressBuilder.show();
    }

    @Override
    public void hideLoading() {
        mProgressBuilder.dismiss();
    }

    @Override
    public void showMessage(String message) {
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(this, intent);
    }

    @Override
    public void killMyself() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_star_exchange_back) {
            killMyself();
        } else if (v.getId() == R.id.rl_star_history) {
            Intent intent = new Intent(ZBLStarExchangeActivity.this, ZBLGoldHistoryActivity.class);
            intent.putExtra("type", GoldService.HISTORY_TYPE_ZAN);
            startActivity(intent);
        }
    }
}
