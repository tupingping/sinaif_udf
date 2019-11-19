package com.sinaif.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SinaifDateDiff extends UDF {

    public IntWritable evaluate(final Text date1, final Text date2 ){
        if(date1==null || date2==null) return null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        long day = 0;
        try{
            Date start_day = dateFormat.parse(date1.toString());
            Date end_day = dateFormat.parse(date2.toString());
            day = (end_day.getTime()-start_day.getTime())/(24*60*60*1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new IntWritable((int)day);
    }
}
