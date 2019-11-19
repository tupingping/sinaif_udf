package com.sinaif.udf;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SinaifDateUpDown extends UDF {
    public Text evaluate(final Text date, final IntWritable value ){
        if(date==null || value==null ){return null;}
        if(value.get() == 0) {
            return date;
        }else{
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try{
                Date day = dateFormat.parse(date.toString());
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(day);
                calendar.add(Calendar.DATE,value.get());
                return new Text(dateFormat.format(calendar.getTime()));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
