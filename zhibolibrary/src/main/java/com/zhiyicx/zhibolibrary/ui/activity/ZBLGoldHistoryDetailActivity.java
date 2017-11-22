package com.zhiyicx.zhibolibrary.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.GoldHistoryJson;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseActivity;
import com.zhiyicx.zhibolibrary.util.ColorPhrase;
import com.zhiyicx.zhibolibrary.util.TimeHelper;

import butterknife.BindView;
import butterknife.OnClick;

public class ZBLGoldHistoryDetailActivity extends ZBLBaseActivity implements View.OnClickListener {
    private final int STATUS_WAIT_DEAL = 2;//0:处理失败 1:处理成功 2:待处理
    private final int STATUS_SUCCESS = 1;
    private final int STATUS_FAIL = 0;
    public static final String KEY_DATA = "DATA";
    ImageView ivStatus;
    TextView tvStatus;
    TextView tvDealInfo;
    TextView tvCtime;
    TextView tvNum;
    private GoldHistoryJson mData;
    @Override
    protected void initView() {
        setContentView(R.layout.zb_activity_gold_history_detail);
         ivStatus= (ImageView) findViewById(R.id.iv_status);
        tvStatus= (TextView) findViewById(R.id.tv_status);
        tvDealInfo= (TextView) findViewById(R.id.tv_deal_info);
        tvCtime= (TextView) findViewById(R.id.tv_ctime);
        tvNum= (TextView) findViewById(R.id.tv_num);
        findViewById(R.id.rl_gold_history_back).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (getIntent() == null || getIntent().getExtras() == null) return;
        Bundle bundle = getIntent().getExtras();
        mData = (GoldHistoryJson) bundle.getSerializable(KEY_DATA);
        updateUI();
    }

    /**
     * 更新UI
     */
    private void updateUI() {

        switch (mData.save_status) {
            case STATUS_WAIT_DEAL:
                tvStatus.setText(getString(R.string.wait_deal));
                ivStatus.setImageResource(R.mipmap.ico_tish);
                break;
            case STATUS_SUCCESS:
                tvStatus.setText(getString(R.string.deal_success));
                ivStatus.setImageResource(R.mipmap.ico_right);
                break;

            case STATUS_FAIL:
                tvStatus.setText(getString(R.string.deal_fail));
                ivStatus.setImageResource(R.mipmap.ic_loseattention);
                break;
        }
        String num="暂无数据";
        try {
             num=Integer.valueOf(mData.num)>0?"+"+mData.num:mData.num;
        }catch (Exception e){
            e.printStackTrace();
        }

        tvNum.setText(num);
        CharSequence charSequence = ColorPhrase.from(mData.desction)
                .withSeparator("<>").innerColor(0xff3e6ea0).outerColor(0xff666666).format();
        tvDealInfo.setText(charSequence);
        tvCtime.setText(TimeHelper.getTime(mData.ctime, "yyyy/MM/dd HH:mm"));

    }
    @Override
    public void onBackPressed() {
        killMyself();
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rl_gold_history_back) {
                killMyself();
        }
    }

    public void killMyself() {
        super.onBackPressed();
    }

}
