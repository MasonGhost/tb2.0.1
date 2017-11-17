package com.lu.richtexteditorlib.factories;

import android.content.Context;

import com.lu.richtexteditorlib.view.api.IBottomMenuItem;
import com.lu.richtexteditorlib.view.menuitem.AbstractBottomMenuItem;

/**
 * Created by 陆正威 on 2017/9/29.
 */

interface IItemFactory<T extends AbstractBottomMenuItem> {
    T generateItem(Context context, Long id, IBottomMenuItem.OnBottomItemClickListener listener);
}
