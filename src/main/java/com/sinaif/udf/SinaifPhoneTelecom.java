package com.sinaif.udf;

import com.sinaif.util.PhoneTelecom;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.util.Map;

public class SinaifPhoneTelecom extends UDF {
    public Text evaluate(final Text phone){
        String telecom = "";
        if(phone != null){
            String phonenum = phone.toString().trim();
            if(phonenum.length() >=4 ) {
                String str = phonenum.substring(0,3);
                if(str.equals("170"))str = phonenum.substring(0,4);

                PhoneTelecom phoneTelecom = new PhoneTelecom();
                Map<String,String> phoneMap = phoneTelecom.getPhoneData();
                telecom = phoneMap.get(str);
            }
        }
        return new Text(telecom);
    }
}
