package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.thinksnsplus.data.beans.WalletConfigBean;

import retrofit2.http.GET;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PAHT_WALLET_CONFIG;


/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/27
 * @Contact master.jungle68@gmail.com
 */
public interface WalletClient {

    @GET(APP_PAHT_WALLET_CONFIG)
    Observable<WalletConfigBean> getWalletConfig();
}
