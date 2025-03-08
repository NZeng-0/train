package com.android.train.utils;

import android.app.Activity;
import android.content.Context;

import com.github.gzuliyujiang.wheelpicker.AddressPicker;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;
import com.github.gzuliyujiang.wheelpicker.utility.AddressJsonParser;
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;

public class AddressPickerUtil {

    public static void showAddressPicker(Context context, OnAddressSelectedListener listener) {
        AddressPicker picker = new AddressPicker((Activity) context);

        picker.setAddressMode("china_address.json", AddressMode.PROVINCE_CITY,
                new AddressJsonParser.Builder()
                        .provinceCodeField("code")
                        .provinceNameField("name")
                        .provinceChildField("")
                        .build());
        picker.show();



        // 监听地址选择事件
        picker.setOnAddressPickedListener(new OnAddressPickedListener() {
            @Override
            public void onAddressPicked(ProvinceEntity province, CityEntity city, CountyEntity county) {
                String selectedAddress = city.getName();
                listener.onAddressSelected(selectedAddress);
            }
        });
    }

    public interface OnAddressSelectedListener {
        void onAddressSelected(String address);
    }
}

