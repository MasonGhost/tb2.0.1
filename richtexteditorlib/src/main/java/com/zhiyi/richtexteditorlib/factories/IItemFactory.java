package com.zhiyi.richtexteditorlib.factories;

import android.content.Context;

import com.zhiyi.richtexteditorlib.view.api.IBottomMenuItem;
import com.zhiyi.richtexteditorlib.view.menuitem.AbstractBottomMenuItem;

interface IItemFactory<T extends AbstractBottomMenuItem> {
    T generateItem(Context context, Long id, IBottomMenuItem.OnBottomItemClickListener listener);
}
