package com.sinaif.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;


public class SinaifReplace extends UDF {
    public Text evaluate(Text input){
        return new Text("Hello " + input.toString());
    }
}
