package com.zhiyicx.zhibolibrary.ui.view;

/**
 * Created by jess on 16/4/25.
 */
public interface StarExchangeView extends BaseView {
    void showPrompt(int zan, int gold);

    void updatedGold();

    void updateStar();
    void initRegular();
}
