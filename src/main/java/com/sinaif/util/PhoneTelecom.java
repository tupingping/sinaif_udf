package com.sinaif.util;

import java.util.HashMap;
import java.util.Map;

public class PhoneTelecom {
    public PhoneTelecom(){}
    public Map<String,String> getPhoneData(){
        Map<String,String> phoneMap = new HashMap<String,String>();
        //移动: 134 135 136 137 138 139 147 148 150 151 152 157 158 159 172 178 182 183 184 187 188 198 -->
        String[] cmcc = {"134","135","136","137","138","139","147","148","150","151","152","157","158","159","172","178","182","183","184","187","188","198",
                "1703","1705","1706"};
        for(String item:cmcc){
            phoneMap.put(item,"cmcc");
        }
        //联通: 130 131 132 145 146 155 156 166 171 175 176 185 186
        String[] unicom = {"130","131","132","145","146","155","156","166","171","175","176","185","186",
                "1704","1707","1708","1709"};
        for(String item:unicom){
            phoneMap.put(item,"unicom");
        }
        //电信: 133 149 153 173 174 177 180 181 189 199
        String[] telcom = {"133","149","153","173","174","177","180","181","189","199",
                "1700","1701","1702"};
        for(String item:telcom){
            phoneMap.put(item,"telcom");
        }
        //虚拟运营商170号段 移动:1703、1705、1706  联通: 1704,1707,1708,1709  电信:1700、1701、1702

        return phoneMap;
    }
}
