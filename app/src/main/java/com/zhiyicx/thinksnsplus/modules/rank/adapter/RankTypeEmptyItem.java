package com.zhiyicx.thinksnsplus.modules.rank.adapter;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeEmptyItem extends EmptyItem<UserInfoBean>{
    @Override
    public boolean isForViewType(UserInfoBean item, int position) {
        return item.getUser_id() == 0L;
    }
}
