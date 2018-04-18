package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Author MasonGhost
 * @Date 2018/04/17/14:55
 * @Email lx1993m@gmail.com
 * @Description
 */

public class TBMarkDetailHeader {

    private View mHeader;
    private UserAvatarView mIvHeadPic;
    private TextView mTvName;
    private TextView mTvCountAvailable;
    private TextView mTvCountFrozen;

    public TBMarkDetailHeader(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout
                .header_tbmark_detail, null);
        mHeader.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        mIvHeadPic = (UserAvatarView) mHeader.findViewById(R.id.iv_headpic);
        mTvName = (TextView) mHeader.findViewById(R.id.tv_name);
        mTvCountAvailable = (TextView) mHeader.findViewById(R.id.tv_count_available);
        mTvCountFrozen = (TextView) mHeader.findViewById(R.id.tv_count_frozen);

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
