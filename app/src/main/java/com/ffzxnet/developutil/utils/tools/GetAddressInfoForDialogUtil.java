package com.ffzxnet.developutil.utils.tools;

import android.text.TextUtils;

import com.ffzxnet.countrymeet.base.net.BaseApiResultData;
import com.ffzxnet.countrymeet.bean.GetAddressInfoResponse;
import com.ffzxnet.countrymeet.constans.Constans;
import com.ffzxnet.countrymeet.net.ApiImp;
import com.ffzxnet.countrymeet.net.ErrorResponse;
import com.ffzxnet.countrymeet.net.IApiSubscriberCallBack;
import com.ffzxnet.countrymeet.utils.ui.ToastUtil;
import com.ffzxnet.countrymeet.utils.ui.city_dialog.CityDialogBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者： feifan.pi 在 2017/7/18.
 */

public class GetAddressInfoForDialogUtil {
    public static void get(final GetAddressInfo getAddressInfo) {
        if (null != Constans.cityDialogBeen && Constans.cityDialogBeen.size() > 0) {
            getAddressInfo.getAddressSuccess(Constans.cityDialogBeen);
        } else {
            String addressJson = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.KEY_CITYS_ADDRESS, "");
            if (TextUtils.isEmpty(addressJson)) {
                getAddressInfoByService(getAddressInfo);
            } else {
                List<CityDialogBean> rs = new ArrayList<>();
                Type type = new TypeToken<ArrayList<CityDialogBean>>() {
                }.getType();
                Constans.cityDialogBeen = (List<CityDialogBean>) GsonUtil.toClass(addressJson, type);
                getAddressInfo.getAddressSuccess(Constans.cityDialogBeen);
            }
        }
    }

    private static List<CityDialogBean> get(GetAddressInfoResponse resultData) {
        List<GetAddressInfoResponse.ProvinceInfo> provinceInfos = resultData.getProvince();
        List<GetAddressInfoResponse.CityInfo> cityInfos = resultData.getCity();
        List<GetAddressInfoResponse.AreaInfo> areaInfos = resultData.getArea();
//        int provinceSize = provinceInfos.size();
//        int citySize = cityInfos.size();
//        int areaSize = areaInfos.size();
        //分区
        List<CityDialogBean> provinceDialogBeens = new ArrayList<>();
        for (GetAddressInfoResponse.ProvinceInfo provinceInfo : provinceInfos) {
            //添加省
            final String provinceId = provinceInfo.getProvinceId();
            CityDialogBean provinceBeen = new CityDialogBean(provinceId, provinceInfo.getProvinceName(), null, 1);
            List<CityDialogBean> cityDialogBeens = new ArrayList<>();
            for (GetAddressInfoResponse.CityInfo cityInfo : cityInfos) {
                //添加市
                if (cityInfo.getProvinceId().equals(provinceId)) {
                    //减少循环次数
//                            cityInfos.remove(cityInfo);
                    String cityId = cityInfo.getCityId();
                    CityDialogBean cityBean = new CityDialogBean(cityId, cityInfo.getCityName(), null, 2);
                    cityDialogBeens.add(cityBean);
                    List<CityDialogBean> areaBeens = new ArrayList<>();
                    //添加县
                    for (GetAddressInfoResponse.AreaInfo areaInfo : areaInfos) {
                        if (areaInfo.getCityId().equals(cityId)) {
                            String areaId = areaInfo.getAreaId();
                            CityDialogBean areBean = new CityDialogBean(areaId, areaInfo.getAreaName(), null, 3);
                            areaBeens.add(areBean);
                            //减少循环次数
//                                    areaInfos.remove(areaInfo);
                        }
                        cityBean.setCityDialogBeens(areaBeens);
                    }
                    provinceBeen.setCityDialogBeens(cityDialogBeens);
                }
            }
            provinceDialogBeens.add(provinceBeen);
        }
        Constans.cityDialogBeen.addAll(provinceDialogBeens);
        return provinceDialogBeens;
    }

    private static void getAddressInfoByService(final GetAddressInfo getAddressInfo) {
        ApiImp.getInstance().getAddressInfo(new IApiSubscriberCallBack<BaseApiResultData<GetAddressInfoResponse>>() {
            @Override
            public void onCompleted() {
                getAddressInfo.getAddressSuccess(null);
            }

            @Override
            public void onError(ErrorResponse error) {
                ToastUtil.showToastShort(error.getMessage());
            }

            @Override
            public void onNext(BaseApiResultData<GetAddressInfoResponse> resultData) {
                List<GetAddressInfoResponse.ProvinceInfo> provinceInfos = resultData.getData().getProvince();
                List<GetAddressInfoResponse.CityInfo> cityInfos = resultData.getData().getCity();
                List<GetAddressInfoResponse.AreaInfo> areaInfos = resultData.getData().getArea();
                //分区
                List<CityDialogBean> provinceDialogBeens = new ArrayList<>();
                for (GetAddressInfoResponse.ProvinceInfo provinceInfo : provinceInfos) {
                    //添加省
                    final String provinceId = provinceInfo.getProvinceId();
                    CityDialogBean provinceBeen = new CityDialogBean(provinceId, provinceInfo.getProvinceName(), null, 1);
                    List<CityDialogBean> cityDialogBeens = new ArrayList<>();
                    for (GetAddressInfoResponse.CityInfo cityInfo : cityInfos) {
                        //添加市
                        if (cityInfo.getProvinceId().equals(provinceId)) {
                            //减少循环次数
//                            cityInfos.remove(cityInfo);
                            String cityId = cityInfo.getCityId();
                            CityDialogBean cityBean = new CityDialogBean(cityId, cityInfo.getCityName(), null, 2);
                            cityDialogBeens.add(cityBean);
                            List<CityDialogBean> areaBeens = new ArrayList<>();
                            //添加县
                            for (GetAddressInfoResponse.AreaInfo areaInfo : areaInfos) {
                                if (areaInfo.getCityId().equals(cityId)) {
                                    String areaId = areaInfo.getAreaId();
                                    CityDialogBean areBean = new CityDialogBean(areaId, areaInfo.getAreaName(), null, 3);
                                    areaBeens.add(areBean);
                                    //减少循环次数
//                                    areaInfos.remove(areaInfo);
                                }
                                cityBean.setCityDialogBeens(areaBeens);
                            }
                            provinceBeen.setCityDialogBeens(cityDialogBeens);
                        }
                    }
                    provinceDialogBeens.add(provinceBeen);
                }
//                JSONObject jsonObject=new JSONObject();
//                try {
//                    jsonObject.put("city",provinceDialogBeens.toString());
                String json = provinceDialogBeens.toString();
                Constans.cityDialogBeen.addAll(provinceDialogBeens);
                getAddressInfo.getAddressSuccess(provinceDialogBeens);
                LogUtil.e("地址信息", json);
                SharedPreferencesUtil.getInstance().putString(SharedPreferencesUtil.KEY_CITYS_ADDRESS, json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    /**
     * 重服务器获取
     */
    public interface GetAddressInfo {
        void getAddressSuccess(List<CityDialogBean> cityDialogBeen);
    }
}
