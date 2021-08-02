package com.ffzxnet.developutil.utils.ui.city_dialog;

import com.ffzxnet.developutil.base.net.BaseResponse;

import java.util.List;

/**
 * 创建者： feifan.pi 在 2017/6/22.
 */

public class CityDialogBean extends BaseResponse{
    private String id;
    private String name;
    private List<CityDialogBean> cityDialogBeens;
    //区分省，市，县 =  1,2,3
    private int level;
    //在适配器里面的第几个
    private int adapterPosition;

    public CityDialogBean(String id, String name, List<CityDialogBean> cityDialogBeens, int level) {
        this.id = id;
        this.name = name;
        this.cityDialogBeens = cityDialogBeens;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityDialogBean> getCityDialogBeens() {
        return cityDialogBeens;
    }

    public void setCityDialogBeens(List<CityDialogBean> cityDialogBeens) {
        this.cityDialogBeens = cityDialogBeens;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public void setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
    }
}
