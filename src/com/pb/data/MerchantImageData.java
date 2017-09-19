package com.pb.data;

import com.pb.data.jdbc.JDBCReader;
import com.pb.data.jdbc.JDBCWriter;

import java.util.List;

/**
 * Created by pb on 2017/5/4.
 */
public  class MerchantImageData {
    private int merchantId;
    private String name;
    private String imageUrl;
    private String area;
    public MerchantImageData(){

    }
    public MerchantImageData(int merchantId, String name, String imageUrl, String area) {
        this.merchantId = merchantId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void writeFail(){
        JDBCWriter.writeFailImage(this);
    }
    public static List<MerchantImageData> read(){
        return JDBCReader.readFailImage();
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
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
