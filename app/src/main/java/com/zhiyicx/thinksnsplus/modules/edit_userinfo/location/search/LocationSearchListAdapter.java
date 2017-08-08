package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/8
 * @Contact master.jungle68@gmail.com
 */
public class LocationSearchListAdapter extends CommonAdapter<LocationBean> {

    public LocationSearchListAdapter(Context context, int layoutId, List<LocationBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, LocationBean data, int position) {
        setItemData(holder, data, position);
    }

    private void setItemData(final ViewHolder holder, final LocationBean locationBean, final int position) {

//        // 设置用户信息
//        final UserInfoBean userInfoBean = rewardsListBean.getUser();
//        if (userInfoBean == null) {
//            // 这种情况一般不会发生，为了防止崩溃，做处理
//            return;
//        }
//        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(rewardsListBean.getCreated_at()));
//        String result = getContext().getResources().getString(R.string.reward_list_tip, "<" + userInfoBean.getName() + ">");
//        CharSequence charSequence = ColorPhrase.from(result).withSeparator("<>")
//                .innerColor(ContextCompat.getColor(getContext(), R.color.important_for_content))
//                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
//                .format();
//        TextView digCount = holder.getView(R.id.tv_content);
//        digCount.setText(charSequence);
//        // 头像加载
//        ImageUtils.loadCircleUserHeadPic(userInfoBean,  holder.getView(R.id.iv_headpic));
//        // 添加点击事件
//        RxView.clicks(holder.getView(R.id.iv_headpic))
//                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
//                .subscribe(aVoid -> toUserCenter(getContext(), userInfoBean));


    }

}
