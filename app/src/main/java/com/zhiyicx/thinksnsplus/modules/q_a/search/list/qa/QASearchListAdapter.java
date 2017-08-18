package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */

public class QASearchListAdapter extends CommonAdapter<QAListInfoBean> {

    public QASearchListAdapter(Context context, int layoutId, List<QAListInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QAListInfoBean data, int position) {
        setItemData(holder, data, position);
    }

    private void setItemData(final ViewHolder holder, final QAListInfoBean data, final int position) {
        holder.setText(R.id.tv_content, data.getSubject());
    }


}
