package com.sinaif.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinaifDateTransform extends UDF {
    public Text evaluate(final Text date){
        if(date == null ) return null;
        DateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        String formatdate = "";
        try{
            Date nowdate = dateFormat1.parse(date.toString());
            formatdate = dateFormat2.format(nowdate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Text(formatdate);
    }
}
