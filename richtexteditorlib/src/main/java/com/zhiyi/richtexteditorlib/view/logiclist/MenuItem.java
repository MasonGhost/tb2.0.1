package com.zhiyi.richtexteditorlib.view.logiclist;

import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 17/11/15 14:21
 * @Email Jliuer@aliyun.com
 * @Description 底部操作栏按钮
 */
public class MenuItem implements Serializable {

    /**
     * 子目录
     */
    private List<MenuItem> nextLevel;

    /**
     * 父目录
     */
    private MenuItem parent;

    /**
     * 当前阶级
     */
    protected int deep;

    /**
     * 唯一 id , 在 ItemIndex 中有定义
     */
    protected Long Id;

    /**
     * transient 不参与序列话
     */
    private transient View contentView;

    /**
     * transient 不参与序列化，记录选中状态
     */
    private transient boolean isSelected;

    MenuItem(Long Id, View contentView) {
        this(null, Id, contentView);
    }

    MenuItem(List<MenuItem> nextLevel, Long Id, View contentView) {
        this(nextLevel, null, Id, contentView);
    }

    MenuItem(List<MenuItem> nextLevel, MenuItem parent, Long Id, View contentView) {
        this.nextLevel = nextLevel;
        this.parent = parent;
        this.Id = Id;
        this.contentView = contentView;
        if (parent != null) {
            this.deep = parent.getDeep() + 1;
        } else {
            this.deep = -1;
        }
    }

    public List<MenuItem> getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(List<MenuItem> nextLevel) {
        this.nextLevel = nextLevel;
        for (MenuItem item : nextLevel) {
            item.setParent(this);
        }
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        if (this.parent != parent && parent != null) {
            this.parent = parent;
            this.deep = parent.getDeep() + 1;
        }
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        this.Id = id;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * 递归所有下级目录
     * @return
     */
    public ArrayList<MenuItem> getJuniors() {
        ArrayList<MenuItem> juniors = new ArrayList<>();
        ArrayList<MenuItem> childList = (ArrayList<MenuItem>) nextLevel;
        if (childList == null) {
            return juniors;
        } else {
            for (MenuItem item : childList) {
                juniors.add(item);
                juniors.addAll(item.getJuniors());
            }
            return juniors;
        }

    }

    /**
     * 单节点，没有下级目录
     * @return
     */
    public boolean isLeafNode() {
        return nextLevel == null || nextLevel.isEmpty();
    }


    public boolean isElderOf(MenuItem testItem) {
        MenuItem start = testItem.getParent();
        while (start != null) {
            if (start.equals(this)) {
                return true;
            }
            start = start.getParent();
        }
        return false;
    }


    public MenuItem getMenuItemById(Long id) {
        ArrayList<MenuItem> childList = (ArrayList<MenuItem>) nextLevel;

        if (this.Id.compareTo(id) == 0) {
            return this;
        }

        if (nextLevel == null || nextLevel.isEmpty()) {
            return null;
        } else {
            for (MenuItem item :
                    childList) {
                MenuItem rs = item.getMenuItemById(id);
                if (rs != null) {
                    return rs;
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuItem MenuItem = (MenuItem) o;

        return Id.equals(MenuItem.Id);

    }

    @Override
    public int hashCode() {
        return Id.hashCode();
    }

    public int getDeep() {
        if (deep == -1 && parent == null) {
            throw new RuntimeException("three node has no parent");
        } else if (deep == -1) {
            deep = parent.getDeep() + 1;
        }
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "deep=" + deep +
                ", Id=" + Id +
                '}';
    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeList(this.nextLevel);
//        dest.writeParcelable(this.parent, flags);
//        dest.writeInt(this.deep);
//        dest.writeValue(this.Id);
//    }
//
//    protected MenuItem(Parcel in) {
//        this.nextLevel = new ArrayList<>();
//        in.readList(this.nextLevel, MenuItem.class.getClassLoader());
//        this.parent = in.readParcelable(MenuItem.class.getClassLoader());
//        this.deep = in.readInt();
//        this.Id = (Long) in.readValue(Long.class.getClassLoader());
//    }
//
//    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
//        @Override
//        public MenuItem createFromParcel(Parcel source) {
//            return new MenuItem(source);
//        }
//
//        @Override
//        public MenuItem[] newArray(int size) {
//            return new MenuItem[size];
//        }
//    };

    public View getContentView() {
        return contentView;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }
}
