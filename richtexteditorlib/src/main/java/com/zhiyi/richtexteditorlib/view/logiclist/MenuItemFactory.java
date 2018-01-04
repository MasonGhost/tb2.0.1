package com.zhiyi.richtexteditorlib.view.logiclist;

import android.content.Context;
import android.view.View;

import com.zhiyi.richtexteditorlib.view.menuitem.ImageViewButtonItem;
import com.zhiyi.richtexteditorlib.view.menuitem.TextViewItem;
/**
 * @Author Jliuer
 * @Date 17/11/15 14:11
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MenuItemFactory {

    public static MenuItem generateMenuItem(long id, View contentView){
        return new MenuItem(id,contentView);
    }

    public static ImageViewButtonItem generateImageItem(Context context, long id, int uri, boolean b){
        return new ImageViewButtonItem(context,generateMenuItem(id,null),uri,b);
    }

    public static ImageViewButtonItem generateImageItem(Context context, long id, int uri){
        return new ImageViewButtonItem(context,generateMenuItem(id,null),uri);
    }

    /**
     *
     * @param context
     * @param uri 资源路径
     * @param b 是否自动 填充颜色
     * @return
     */
    public static ImageViewButtonItem generateImageItem(Context context, int uri, boolean b){
        return new ImageViewButtonItem(context,generateMenuItem(0x00,null),uri,b);
    }

    public static TextViewItem generateTextItem(Context context, long id,String text){
        return new TextViewItem(context,generateMenuItem(id,null), text);
    }

//    public static ToggleButtonItem generateToggleButtonItem(Context context, long id, int idres){
//        return new ToggleButtonItem(context,generateMenuItem(id,null), idres);
//    }

}
