package com.zhiyicx.thinksnsplus.data.beans;

import java.io.Serializable;

public class ImageAdvert implements Serializable {
    private static final long serialVersionUID = 124L;
    private String link;
    private String image;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}