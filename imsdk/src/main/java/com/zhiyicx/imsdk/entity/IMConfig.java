package com.zhiyicx.imsdk.entity;

import com.zhiyicx.imsdk.core.ImService;

import java.io.Serializable;

/**
 * Created by jungle on 16/7/6.
 * com.zhiyicx.imsdk.model
 * zhibo_android
 * email:335891510@qq.com
 */
public class IMConfig implements Serializable {
    private int imUid;
    private String token;
    private int serial = ImService.BIN_MSGPACK;//希望服务器返回的数据类型是json，还是msgpck,默认为msgpack
    private int comprs = ImService.COMPRS_ZLIB;//:0不支持,1:deflate,2:zlib,3:gzip
    private String web_socket_authority;

    @Override
    public String toString() {
        return imUid+"\n"+token+"\n"+web_socket_authority;
    }

    public String getWeb_socket_authority() {
        return web_socket_authority;
    }

    public void setWeb_socket_authority(String web_socket_authority) {
        this.web_socket_authority = web_socket_authority;
    }
    public int getComprs() {
        return comprs;
    }

    public void setComprs(int comprs) {
        this.comprs = comprs;
    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getImUid() {
        return imUid;
    }

    public void setImUid(int imUid) {
        this.imUid = imUid;
    }


}
