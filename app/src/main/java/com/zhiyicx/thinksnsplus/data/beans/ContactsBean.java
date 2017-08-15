package com.zhiyicx.thinksnsplus.data.beans;

import com.github.tamir7.contacts.Contact;

import java.io.Serializable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */
public class ContactsBean implements Serializable
{
    private static final long serialVersionUID = -4986452838740429568L;
    private Contact contact;
    private UserInfoBean user;
    private String phone;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ContactsBean{" +
                "contact=" + contact +
                ", user=" + user +
                '}';
    }
}
