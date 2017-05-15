package com.zhiyicx.thinksnsplus.strategy;

/**
 * @Author Jliuer
 * @Date 2017/05/15/9:50
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class LoginStrategy implements ILoginStrategy {

    private boolean isTourist;

    public LoginStrategy(boolean isTourist) {
        this.isTourist = isTourist;
    }

    @Override
    public boolean isTourist() {
        return isTourist;
    }



}
