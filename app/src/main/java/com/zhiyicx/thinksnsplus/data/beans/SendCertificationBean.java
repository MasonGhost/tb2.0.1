package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe 组装发布申请认证的model
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class SendCertificationBean implements Parcelable{

    /*type	String	必须, 认证类型，必须是 user 或者 org。
    files	Array|Object	必须, 认证材料文件。必须是数组或者对象，value 为 文件ID。
    name	String	必须, 如果 type 是 org 那么就是负责人名字，如果 type 是 user 则为用户真实姓名。
    phone	String	必须, 如果 type 是 org 则为负责人联系方式，如果 type 是 user 则为用户联系方式。
    number	String	必须, 如果 type 是 org 则为营业执照注册号，如果 type 是 user 则为用户身份证号码。
    desc	String	必须，认证描述。
    org_name	String	如果 type 为 org 则必须，企业或机构名称。
    org_address	String	如果 type 为 org 则必须，企业或机构地址。*/

    public static final String USER = "user";
    public static final String ORG = "org";

    private String type;
    private List<Integer> files;
    private String name;
    private String phone;
    private String number;
    private String desc;
    private String org_name;
    private String org_address;
    private List<ImageBean> picList;
    private boolean isUpdate; // 是否更新

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getFiles() {
        return files;
    }

    public void setFiles(List<Integer> files) {
        this.files = files;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    public String getOrg_address() {
        return org_address;
    }

    public void setOrg_address(String org_address) {
        this.org_address = org_address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ImageBean> getPicList() {
        return picList;
    }

    public void setPicList(List<ImageBean> picList) {
        this.picList = picList;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    @Override
    public String toString() {
        return "SendCertificationBean{" +
                "type='" + type + '\'' +
                ", files=" + files +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", number='" + number + '\'' +
                ", desc='" + desc + '\'' +
                ", org_name='" + org_name + '\'' +
                ", org_address='" + org_address + '\'' +
                ", picList=" + picList +
                ", isUpdate=" + isUpdate +
                '}';
    }

    public SendCertificationBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeList(this.files);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.number);
        dest.writeString(this.desc);
        dest.writeString(this.org_name);
        dest.writeString(this.org_address);
        dest.writeTypedList(this.picList);
        dest.writeByte(this.isUpdate ? (byte) 1 : (byte) 0);
    }

    protected SendCertificationBean(Parcel in) {
        this.type = in.readString();
        this.files = new ArrayList<Integer>();
        in.readList(this.files, Integer.class.getClassLoader());
        this.name = in.readString();
        this.phone = in.readString();
        this.number = in.readString();
        this.desc = in.readString();
        this.org_name = in.readString();
        this.org_address = in.readString();
        this.picList = in.createTypedArrayList(ImageBean.CREATOR);
        this.isUpdate = in.readByte() != 0;
    }

    public static final Creator<SendCertificationBean> CREATOR = new Creator<SendCertificationBean>() {
        @Override
        public SendCertificationBean createFromParcel(Parcel source) {
            return new SendCertificationBean(source);
        }

        @Override
        public SendCertificationBean[] newArray(int size) {
            return new SendCertificationBean[size];
        }
    };
}
