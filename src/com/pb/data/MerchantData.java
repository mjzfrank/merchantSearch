package com.pb.data;

import com.pb.data.jdbc.JDBCReader;
import com.pb.data.jdbc.JDBCWriter;

import java.util.Date;

/**
 * Created by pb on 2017/5/4.
 */
public  class MerchantData {
    private int merchantId;
    private String name;
    private String address;
    private String tel;
    private String stars;
    private String businessQualification;
    private String businessTime;
    private String averagePrice;
    private String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public byte getImageNum() {
        return imageNum;
    }

    public void setImageNum(byte imageNum) {
        this.imageNum = imageNum;
    }

    private byte imageNum;

    public MerchantData(){

    }

    public MerchantData(int merchantId, String name, String address, String tel, String stars, String businessQualification, String businessTime, String averagePrice) {
        this.merchantId = merchantId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.stars = stars;
        this.businessQualification = businessQualification;
        this.businessTime = businessTime;
        this.averagePrice = averagePrice;
    }
    public void write(){

        try {
            JDBCWriter.write(this);
        }catch (Exception ex){
            System.err.println(String.format("添加商铺：%s,号码：%d，失败", name, merchantId)+"_"+new Date());
            ex.printStackTrace();
        }
    }
    public static MerchantData read(int merchantId){
return JDBCReader.read(merchantId);
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getBusinessQualification() {
        return businessQualification;
    }

    public void setBusinessQualification(String businessQualification) {
        this.businessQualification = businessQualification;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(String averagePrice) {
        this.averagePrice = averagePrice;
    }
}
