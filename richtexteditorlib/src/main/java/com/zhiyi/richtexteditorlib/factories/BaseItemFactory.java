package com.zhiyi.richtexteditorlib.factories;

import android.content.Context;

import com.zhiyi.richtexteditorlib.view.api.IBottomMenuItem;
import com.zhiyi.richtexteditorlib.view.menuitem.AbstractBottomMenuItem;

public abstract class BaseItemFactory<T extends AbstractBottomMenuItem> implements IItemFactory<T> {
    @Override
    public abstract T generateItem(Context context, Long id, IBottomMenuItem.OnBottomItemClickListener listener) ;

    public abstract T generateItem(Context context, Long id) ;
}
