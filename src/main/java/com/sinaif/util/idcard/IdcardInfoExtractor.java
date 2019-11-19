package com.sinaif.util.idcard;



import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class IdcardInfoExtractor {
    // 省份
    private String province;
    // 城市
    private String city;
    // 区县
    private String region;
    // 出生日期
    private String birthday;
    //年龄
    private String age;
    //星座
    private String starsign;
    // 性别
    private String gender;

    private IdcardValidator validator = null;

    private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
    private final static String[] constellationArr = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };


    /**
     * 通过构造方法初始化各个成员属性
     */
    public IdcardInfoExtractor(String idcard) {
        try {
            validator = new IdcardValidator();
            if (validator.isValidatedAllIdcard(idcard)) {
                if (idcard.length() == 15) {
                    idcard = validator.convertIdcarBy15bit(idcard);
                }
                // 获取省份
                String provinceId = idcard.substring(0, 2);
                String cityId = idcard.substring(0, 4);
                String areaId = idcard.substring(0, 6);
                this.province = IdcardUtil.getProvince(provinceId);
                this.city = IdcardUtil.getCity(cityId);
                this.region = IdcardUtil.getArea(areaId);
                // 获取性别
                String id17 = idcard.substring(16, 17);
                if (Integer.parseInt(id17) % 2 != 0) {
                    this.gender = "男";
                } else {
                    this.gender = "女";
                }

                // 获取出生日期
                String birthday = idcard.substring(6, 14);
                this.birthday = birthday;

                //获取年龄，星座
                   //当前日期
                Calendar cal = Calendar.getInstance();
                int yearNow = cal.get(Calendar.YEAR);
                int monthNow = cal.get(Calendar.MONTH)+1;
                int dayNow = cal.get(Calendar.DAY_OF_MONTH);

                  //出生日期
                Date birthdate = new SimpleDateFormat("yyyyMMdd").parse(birthday);
                cal.setTime(birthdate);
                int yearBirth = cal.get(Calendar.YEAR);
                int monthBirth = cal.get(Calendar.MONTH)+1;
                int dayBirth = cal.get(Calendar.DAY_OF_MONTH);
                int age = yearNow - yearBirth;

                if (monthNow <= monthBirth) {
                    if (monthNow == monthBirth) {
                        if (dayNow < dayBirth) age--;
                    }else{
                        age--;
                    }
                }
                this.age = age+"";

                this.starsign = dayBirth < dayArr[monthBirth-1] ? constellationArr[monthBirth-1] : constellationArr[monthBirth];


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @return the birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @return the birthday
     */
    public String getStarsign() {
        return starsign;
    }

    /**
     * @return the gender
     */
    public String getGender() { return gender; }

    /**
     * @return the age
     */
    public String getAge() { return age; }


    @Override
    public String toString() {
        return "省份：" + this.province + "，城市：" + this.city + "，地区：" + this.region + ",性别：" + this.gender + ",出生日期："
                + this.birthday+"，年龄：" + this.age +", 星座："+this.starsign;
    }

    public static void main(String[] args) {

         //String idcard = "612501199010134679";
        //String idcard = "362427199605190035";
        String idcard = null;
        IdcardInfoExtractor extractor = new IdcardInfoExtractor(idcard);
        System.out.println(extractor.toString());
    }
}
