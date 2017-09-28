package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @Author Jliuer
 * @Date 2017/07/24/17:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CollectGroupDyanmciListBean {

    /**
     * id : 19
     * post_id : 24
     * updated_at : 2017-07-24 07:24:28
     * post : {"id":24,"title":"addBtnAnimation","content":"😁😁😁😁😁😁😁😁😁😔😁😁😁😔😔😁😁😁😁😁😁😁😁😁","group_id":1,"views":1,"diggs":0,"collections":0,"comments":0,"user_id":31,"is_audit":1,"created_at":"2017-07-21 07:43:43","updated_at":"2017-07-21 07:43:43","group_post_mark":311500623022151,"images":[]}
     */

    private int id;
    private int post_id;
    private String updated_at;
    private GroupDynamicListBean post;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public GroupDynamicListBean getPost() {
        return post;
    }

    public void setPost(GroupDynamicListBean post) {
        this.post = post;
    }
}
