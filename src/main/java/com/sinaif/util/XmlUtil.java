package com.sinaif.util;


import java.io.File;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {
    private String xml_path;
    public XmlUtil(){}

    public XmlUtil(String xml_path){
        this.xml_path = xml_path;
    }

    public String getPhoneNumTelecom(String num){
        String telecom = "";
        if(num.length() == 0) return telecom;
        String str = num.substring(0,3);
        if(str.equals("170"))str = num.substring(0,4);
        try {
            //InputStream inputStream = this.getClass().getResourceAsStream(this.xml_path);
            File file = new File(this.xml_path);
            SAXReader saxReader = new SAXReader();
            Document doc = saxReader.read(file);
            List<Element> elements = doc.getRootElement().elements();
            for(Element number : elements){
                Attribute attribute = number.attribute("id");
                String id = attribute.getValue();
                if(str.equals(id)){
                    telecom = number.getStringValue();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return telecom;
    }
}
