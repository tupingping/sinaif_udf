package com.sinaif.udf;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.junit.Test;


public class SinaifDateUpDownTest {

    @Test
    public void evaluate() {
        SinaifDateUpDown sf = new SinaifDateUpDown();
        sf.evaluate(new Text("123456778"), new IntWritable(4));
    }
}