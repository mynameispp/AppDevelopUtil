package com.ffzxnet.developutil.utils.ui.city_dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.application.MyApplication;
import com.ffzxnet.developutil.constans.MyConstans;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 创建者： feifan.pi 在 2017/6/22.
 */

public class CityDialog extends Dialog implements CityDialogAdapter.ItemClickListen, View.OnClickListener {
    //省，市，县，列表
    private RecyclerView province, city, county;
    //市，县数据适配器 ，因为省不会变，所以不用做处理
    private CityDialogAdapter cityAdpter, countyAdapter;
    //包含省市县数据
    private List<CityDialogBean> cityDialogBeanList;
    //记录已选的数据
    private String selectProvince = "";
    private String selectCity = "";
    private String selectCounty = "";
    private String areaId;//县区ID
    private SelectListen selectListen;
    //判断哪个级别没有选
    private boolean selectedProvince, selectedCity, selectedCounty;

    public void setCityDialogBeanList(List<CityDialogBean> cityDialogBeanList) {
        this.cityDialogBeanList = cityDialogBeanList;
    }

    public CityDialog(@NonNull Context context, SelectListen selectListen) {
        this(context, R.style.DialogStyle);
        this.selectListen = selectListen;
    }

    public CityDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected CityDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_city);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.DialogAnimation);
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.width = MyConstans.Screen_Width; // 宽度
        lp.height = (int) (MyConstans.Screen_Height * 0.3); // 高度
        // lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);

        initView();
    }

    /**
     * 初始化数据
     */
    private void initView() {
        //省
        province = (RecyclerView) findViewById(R.id.dialog_city_province);
        //市
        city = (RecyclerView) findViewById(R.id.dialog_city_city);
        //县
        county = (RecyclerView) findViewById(R.id.dialog_city_county);

        LinearLayoutManager provinceManager = new LinearLayoutManager(getContext());
        province.setLayoutManager(provinceManager);
        LinearLayoutManager cityManager = new LinearLayoutManager(getContext());
        city.setLayoutManager(cityManager);
        LinearLayoutManager countyManager = new LinearLayoutManager(getContext());
        county.setLayoutManager(countyManager);

        //设置省级别数据
        if (null != cityDialogBeanList) {
            province.setAdapter(new CityDialogAdapter(cityDialogBeanList, this));
        }

    }

    @Override
    public void itemCityClcik(CityDialogBean bean) {
        switch (bean.getLevel()) {
            case 1:
                //省
                if (!bean.getName().equals(selectProvince)) {
                    selectProvince = bean.getName();
                    selectedProvince = true;
                    selectedCity = false;
                    selectedCounty = false;
                    if (null != bean.getCityDialogBeens()) {
                        if (null == cityAdpter) {
                            cityAdpter = new CityDialogAdapter(bean.getCityDialogBeens(), this);
                            city.setAdapter(cityAdpter);
                        } else {
                            cityAdpter.changeDatas(bean.getCityDialogBeens());
                            selectCity = "";
                            //改变数据时，自动回滚到第一行
                            city.scrollToPosition(0);
                            if (null != countyAdapter) {
                                //如果已经选到了县级则清空
                                countyAdapter.clearDatas();
                                selectCounty = "";
                            }
                        }
                    } else {
                        //只有一级行政区
                        selectedCity = true;
                        selectedCounty = true;
                        if (null != cityAdpter) {
                            //如果已经选到了市级则清空
                            cityAdpter.clearDatas();
                            cityAdpter = null;
                            selectCity = "";
                        }
                        if (null != countyAdapter) {
                            //如果已经选到了县级则清空
                            countyAdapter.clearDatas();
                            selectCounty = "";
                        }
                    }
                }
                break;
            case 2:
                //市
                if (!bean.getName().equals(selectCity)) {
                    selectCity = bean.getName();
                    selectedCity = true;
                    selectedCounty = false;
                    if (null != bean.getCityDialogBeens()) {
                        if (null == countyAdapter) {
                            countyAdapter = new CityDialogAdapter(bean.getCityDialogBeens(), this);
                            county.setAdapter(countyAdapter);
                        } else {
                            countyAdapter.changeDatas(bean.getCityDialogBeens());
                            //改变数据时，自动回滚到第一行
                            county.scrollToPosition(0);
                            selectCounty = "";
                        }
                    } else {
                        selectedCounty = true;
                        selectCounty = "";
                    }
                }
                break;
            case 3:
                //县
                if (!bean.getName().equals(selectCounty)) {
                    selectCounty = bean.getName();
                    selectedCounty = true;
                    areaId = bean.getId();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
//        if (R.id.include_dialog_title_cancle == v.getId()) {
//            //取消
//            dismiss();
//        } else if (R.id.include_dialog_title_ok == v.getId()) {
//            //确定
//            if (!selectedProvince) {
//                Toast.makeText(getContext(), "请选择省份", Toast.LENGTH_LONG).show();
//            } else if (!selectedCity) {
//                Toast.makeText(getContext(), "请选择市", Toast.LENGTH_LONG).show();
//            } else if (!selectedCounty) {
//                Toast.makeText(getContext(), "请选择县/区", Toast.LENGTH_LONG).show();
//            } else {
////                ToastUtil.showToastShort(selectProvince + selectCity + selectCounty);
//                selectListen.selectAddressInfo(selectProvince + selectCity + selectCounty, selectCounty, areaId);
//                dismiss();
//            }
//        }
    }

    public interface SelectListen {
        void selectAddressInfo(String addressInfo, String areaName, String id);
    }
}
