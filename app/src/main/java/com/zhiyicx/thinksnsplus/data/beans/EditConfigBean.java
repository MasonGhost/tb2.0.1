package com.zhiyicx.thinksnsplus.data.beans;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/22
 * @contact email:450127106@qq.com
 */
@Entity
public class EditConfigBean {
    @Id
    private Long itemId;// 配置项的id
    private String itemField;// 配置项的接口字段，对应用户信息字段
    private String itemName;// 配置项的名称
    private String itemType;// 配置项的ui类型

    public EditConfigBean() {

    }

    @Keep
    public EditConfigBean(Long itemId, String itemField, String itemName, String itemType) {
        this.itemId = itemId;
        this.itemField = itemField;
        this.itemName = itemName;
        this.itemType = itemType;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemField() {
        return itemField;
    }

    public void setItemField(String itemField) {
        this.itemField = itemField;
    }
}
