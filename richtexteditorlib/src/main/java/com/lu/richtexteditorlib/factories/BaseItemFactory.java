package com.lu.richtexteditorlib.factories;

import android.content.Context;

import com.lu.richtexteditorlib.view.api.IBottomMenuItem;
import com.lu.richtexteditorlib.view.menuitem.AbstractBottomMenuItem;

public abstract class BaseItemFactory<T extends AbstractBottomMenuItem> implements IItemFactory<T> {
    @Override
    public abstract T generateItem(Context context, Long id, IBottomMenuItem.OnBottomItemClickListener listener) ;

    public abstract T generateItem(Context context, Long id) ;
}
