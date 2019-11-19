package com.sinaif.udf;

import com.sinaif.util.phone.PhoneNumberGeo;
import com.sinaif.util.phone.PhoneNumberInfo;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class SinaifPhoneNumberGeo extends UDF {
    public Text evaluate(final Text phone_number){
        String phone_info = "";
        String province = "";
        String city = "";
        String zipCode = "";
        String areaCode = "";
        String phoneType = "";
        if(phone_number != null){
            PhoneNumberGeo phoneNumberGeo = new PhoneNumberGeo();
            PhoneNumberInfo phoneNumberInfo = phoneNumberGeo.lookup(phone_number.toString());
            if (phoneNumberInfo != null) {
                province = phoneNumberInfo.getProvince();
                city = phoneNumberInfo.getCity();
                zipCode = phoneNumberInfo.getZipCode();
                areaCode = phoneNumberInfo.getAreaCode();
                phoneType = phoneNumberInfo.getPhoneType();
            }
        }
        phone_info = province + "," + city + "," + zipCode + "," + areaCode + "," + phoneType;
        return new Text(phone_info);
    }
}
