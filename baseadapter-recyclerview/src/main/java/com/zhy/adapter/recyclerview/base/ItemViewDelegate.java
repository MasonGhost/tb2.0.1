<<<<<<< HEAD
package com.zhy.adapter.recyclerview.base;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, T lastT, int position);

}
=======
package com.zhy.adapter.recyclerview.base;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, T lastT, int position);

}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
