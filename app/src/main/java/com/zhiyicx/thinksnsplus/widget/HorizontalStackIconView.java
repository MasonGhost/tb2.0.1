package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.List;

/**
 * @author Catherine
 * @describe 只有五个头像的横向列表
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public class HorizontalStackIconView extends FrameLayout {

    private Context mContext;
    private UserAvatarView[] mImageViews;
    private TextView mTvExpertCount;
    private int mExpertCount;

    public HorizontalStackIconView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HorizontalStackIconView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalStackIconView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.view_horizontal_icon_view, this);
        mImageViews = new UserAvatarView[5];
        mImageViews[0] = (UserAvatarView) findViewById(R.id.iv_dig_head1);
        mImageViews[1] = (UserAvatarView) findViewById(R.id.iv_dig_head2);
        mImageViews[2] = (UserAvatarView) findViewById(R.id.iv_dig_head3);
        mImageViews[3] = (UserAvatarView) findViewById(R.id.iv_dig_head4);
        mImageViews[4] = (UserAvatarView) findViewById(R.id.iv_dig_head5);
        mTvExpertCount = (TextView) findViewById(R.id.tv_expert_count);
    }

    /**
     * 设置点赞的人的头像，最多五个
     */
    public void setDigUserHeadIcon(List<UserInfoBean> expertList) {
        if (expertList == null || expertList.size() == 0) {
            setVisibility(GONE);
        } else {
            for (int i = 0; i < mImageViews.length; i++) {
                // 需要显示的图片控件
                if (i < expertList.size()) {
                    UserInfoBean userInfoBean = expertList.get(i);
                    ImageUtils.loadCircleUserHeadPic(userInfoBean,mImageViews[i]);
                    mImageViews[i].setVisibility(VISIBLE);
                } else {// 没有显示的图片控件隐藏
                    mImageViews[i].setVisibility(GONE);
                }
            }
        }

    }

    /**
     * 设置专家数量
     *
     * @param expertCount 专家数量
     */
    public void setExpertCount(int expertCount) {
        this.mExpertCount = expertCount;
        mTvExpertCount.setText(String.format(mContext.getString(R.string.qa_topic_expert_count), expertCount));
    }

}
