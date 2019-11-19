package com.sinaif.udf;

import com.sinaif.util.idcard.IdcardInfoExtractor;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class SinaifIDcardInfo extends UDF {

    public Text evaluate(final Text idcard_number){
        String idcard_info = "";
        if(idcard_number != null){
            String idcard = idcard_number.toString().trim();
            IdcardInfoExtractor extractor = new IdcardInfoExtractor(idcard);
            // 省份
            String province = extractor.getProvince();
            // 城市
            String city = extractor.getCity();
            // 区县
            String region = extractor.getRegion();
            // 出生日期
            String birthday = extractor.getBirthday();
            // 星座
            String starsign = extractor.getStarsign();
            // 性别
            String gender = extractor.getGender();
            // 年龄
            String age = extractor.getAge();

            idcard_info = province+","+city+","+region+","+birthday+","+age+","+starsign+","+gender;
        }
        return new Text(idcard_info);
    }
}
