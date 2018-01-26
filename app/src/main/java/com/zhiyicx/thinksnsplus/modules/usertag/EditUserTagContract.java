package com.zhiyicx.thinksnsplus.modules.usertag;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public interface EditUserTagContract {
    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IBaseView<Presenter> {
        void updateMineTagsFromNet(List<UserTagBean> tags);

        void updateTagsFromNet(List<TagCategoryBean> tagCategoryBeanList);

        void addTagSuccess(int categoryPosition, int tagPosition);

        void deleteTagSuccess(int position);

        TagFrom getCurrentFrom();

        ArrayList<UserTagBean> getChoosedTags();
    }

    interface Presenter extends IBasePresenter {
        /**
         * 获取全部标签
         */
        void getAllTags();

        void handleCategoryTagsClick(UserTagBean userTagBean);

        void addTags(Long id, int categoryPosition, int tagPosition);

        void deleteTag(Long id, int position);
    }

}
