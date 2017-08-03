package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * @author Catherine
 * @describe 组装发布申请认证的model
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class SendCertificationBean {

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
}
