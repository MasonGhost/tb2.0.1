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


        holder.setText(R.id.tv_content, LocationBean.getlocation(locationBean));

    }


}
