package com.zhiyicx.thinksnsplus.modules.tb.exchange;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailActivity;
import com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail.TBMarkDetailFragment;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ExchangeHeader {

    private View mHeader;
    private UserAvatarView mIvHeadPic;
    private TextView mTvName;
    private TextView mTvContent;
    private TextView mTvFollow;
    private TextView mTvSubtraction;
    private TextView mTvCount;
    private TextView mTvPlus;
    private TextView mTvTip;
    private TextView mBtExchange;
    private TextView mTvProjectName;
    private TextView mTvProjectIntro;

    public ExchangeHeader(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout
                .fragment_exchange, null);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mIvHeadPic = (UserAvatarView) mHeader.findViewById(R.id.iv_headpic);
        mTvName = (TextView) mHeader.findViewById(R.id.tv_name);
        mTvContent = (TextView) mHeader.findViewById(R.id.tv_content);
        mTvFollow = (TextView) mHeader.findViewById(R.id.tv_follow);
        mTvSubtraction = (TextView) mHeader.findViewById(R.id.tv_subtraction);
        mTvCount = (TextView) mHeader.findViewById(R.id.tv_count);
        mTvPlus = (TextView) mHeader.findViewById(R.id.tv_plus);
        mTvTip = (TextView) mHeader.findViewById(R.id.tv_tip);
        mBtExchange = (TextView) mHeader.findViewById(R.id.bt_exchange);
        mTvProjectName = (TextView) mHeader.findViewById(R.id.tv_project_name);
        mTvProjectIntro = (TextView) mHeader.findViewById(R.id.tv_project_intro);

        mIvHeadPic.setOnClickListener(view -> {
            Intent intent = new Intent(context, TBMarkDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(TBMarkDetailFragment.BILL_TYPE, null);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    public View getHeader() {
        return mHeader;
    }
}
