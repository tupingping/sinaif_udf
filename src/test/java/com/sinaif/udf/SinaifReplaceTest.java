package com.sinaif.udf;

import org.apache.hadoop.io.Text;
import org.junit.Test;


public class SinaifReplaceTest {

    @Test
    public void evaluate() {
        SinaifReplace sr = new SinaifReplace();
        System.out.println(sr.evaluate(new Text("world!")));
    }
}