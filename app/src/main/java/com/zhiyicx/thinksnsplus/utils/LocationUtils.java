package com.zhiyicx.thinksnsplus.utils;


import android.content.Context;

import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/16
 * @Contact master.jungle68@gmail.com
 */
public class LocationUtils {

    public static void getLatlon(String address, Context context,GeocodeSearch.OnGeocodeSearchListener l) {

        GeocodeSearch geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(l);
//        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//                if (i == 1000) {
//                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null &&
//                            geocodeResult.getGeocodeAddressList().size() > 0) {
//
//                        GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
//                        double latitude = geocodeAddress.getLatLonPoint().getLatitude();//纬度
//                        double longititude = geocodeAddress.getLatLonPoint().getLongitude();//经度
//                        String adcode = geocodeAddress.getAdcode();//区域编码
//
//
//                        LogUtils.e("地理编码"+ geocodeAddress.getAdcode() + "");
//                        LogUtils.e("纬度latitude"+latitude + "");
//                        LogUtils.e("经度longititude"+ longititude + "");
//
//                    } else {
//                        LogUtils.e("地址名出错");
//                    }
//                }
//            }
//        });
        String cityNmae = address;
        try {
            String[] data = address.split(" ");
            cityNmae = data[data.length - 1];
        } catch (Exception e) {

        }


        GeocodeQuery geocodeQuery = new GeocodeQuery(address.trim(), cityNmae);
        geocodeSearch.getFromLocationNameAsyn(geocodeQuery);


    }

}
