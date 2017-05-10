package com.zhiyicx.imsdk.entity;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/12
 * @Contact master.jungle68@gmail.com
 */

public class AuthData {

    /**
     * ping : 200
     * seqs : [{"cid":1,"seq":99},{"cid":2,"seq":8}]
     */

    private int ping;
    /**
     * cid : 1
     * seq : 99
     */

    private List<SeqsBean> seqs;

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public List<SeqsBean> getSeqs() {
        return seqs;
    }

    public void setSeqs(List<SeqsBean> seqs) {
        this.seqs = seqs;
    }

    public static class SeqsBean {
        private int cid;
        private int seq;

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        @Override
        public String toString() {
            return "SeqsBean{" +
                    "cid=" + cid +
                    ", seq=" + seq +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AuthData{" +
                "ping=" + ping +
                ", seqs=" + seqs +
                '}';
    }
}
