package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter;

import android.content.Context;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.DigListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/09/11/14:20
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseDigItem implements ItemViewDelegate<BaseListBean> {

    public static final String TYPE_INFO = "info";
    public static final String TYPE_DYNAMIC = "dynamic";
    public static final String TYPE_POST = "post";

    public enum DigTypeEnum {
        INFO(TYPE_INFO),
        DYNAMIC(TYPE_DYNAMIC),
        POST(TYPE_POST);
        public String value;

        DigTypeEnum(String value) {
            this.value = value;
        }
    }

    private DigListContract.Presenter mPresenter;

    protected Context mContext;

    public BaseDigItem(Context context, DigListContract.Presenter presenter) {
        mContext = context;
        this.mPresenter = presenter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dig_list;
    }

    @Override
    public abstract boolean isForViewType(BaseListBean item, int position);

    @Override
    public abstract void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts);

    /**
     * 前往用户个人中心
     */
    protected void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

    protected void handleFollowUser(int position, UserInfoBean followFansBean) {
        mPresenter.handleFollowUser(position, followFansBean);
    }
}
