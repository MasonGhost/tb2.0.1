package com.zhiyicx.thinksnsplus.data.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
public class ContactsContainerBean implements Serializable {

    private static final long serialVersionUID = 2735557939775887100L;
    private String  title;
    private ArrayList<ContactsBean> contacts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContactsBean> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactsBean> contacts) {
        this.contacts = contacts;
    }

}
