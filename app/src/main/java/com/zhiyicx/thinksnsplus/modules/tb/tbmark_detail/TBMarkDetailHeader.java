package com.zhiyicx.thinksnsplus.modules.tb.tbmark_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
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
    private LayoutParams mLp;

    public TBMarkDetailHeader(Context context) {
        mHeader = LayoutInflater.from(context).inflate(R.layout
                .header_tbmark_detail, null);
        mLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mLp.setMargins(0, 0, 0, 4);
        mHeader.setLayoutParams(mLp);
        mIvHeadPic = (UserAvatarView) mHeader.findViewById(R.id.iv_headpic);
        mTvName = (TextView) mHeader.findViewById(R.id.tv_name);
        mTvCountAvailable = (TextView) mHeader.findViewById(R.id.tv_count_available);
        mTvCountFrozen = (TextView) mHeader.findViewById(R.id.tv_count_frozen);

    }

    public View getHeader() {
        return mHeader;
    }
}
