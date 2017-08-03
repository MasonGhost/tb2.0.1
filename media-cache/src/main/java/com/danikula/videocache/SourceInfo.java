package com.danikula.videocache;

/**
 * Stores source's info.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public class SourceInfo {

    public final String url;
    public final String token;
    public final int length;
    public final String mime;

    public SourceInfo(String url, String token,int length, String mime) {
        this.url = url;
        this.length = length;
        this.mime = mime;
        this.token = token;
    }

    @Override
    public String toString() {
        return "SourceInfo{" +
                "url='" + url + '\'' +
                "token='" + token + '\'' +
                ", length=" + length +
                ", mime='" + mime + '\'' +
                '}';
    }
}
