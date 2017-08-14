package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
public class ContactsContainerBean {

    private String  title;
    private List<ContactsBean> contacts;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ContactsBean> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactsBean> contacts) {
        this.contacts = contacts;
    }
}
