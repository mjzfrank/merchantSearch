package com.pb.data.image;

import com.pb.data.MerchantImageData;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by pb on 2017/5/4.
 */
public class ImageWriter {
    public static final  String IMG_SAVE_PATH="D:/merchantimages/新疆/";
    public static void write(MerchantImageData data) throws URISyntaxException, IOException {
        HttpGet httpGet = new HttpGet(data.getImageUrl());
        HttpClient httpClient=HttpClients.createDefault();
        String fileName=data.getImageUrl().substring(data.getImageUrl().lastIndexOf("/")+1);
        String IMG_SAVE_PATH_NE2 = IMG_SAVE_PATH + data.getArea();
        String path=String.format("%s/%d_%s",IMG_SAVE_PATH_NE2,data.getMerchantId(),data.getName().replace("*","").replace("/","").replace("|",""));

        String fullPath=String.format("%s/%s",path,fileName);
        File outFile=new File(path);
        FileOutputStream fos=null;
        if(!outFile.exists()) {
            outFile.mkdirs();
        }
        outFile=new File(fullPath);
        try {
            HttpResponse response=httpClient.execute(httpGet);
            InputStream inputStream = response.getEntity().getContent();
            int len;
            byte[] bytes= new byte[1024];
            fos=new FileOutputStream(outFile);
            while ((len = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
            }
        } catch (IOException e) {
           throw e;
        }
        finally {
            try {
            if(fos!=null){
                    fos.close();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
public static void main(String[] args){
    try {
       // write("1","test","http://qcloud.dpfile.com/pc/kS9CL8vw_tCefyHPvpIzJ8FW4D2nDQipzHz4RVcoIEt2Z2NWo8mGzuxwarQuIULZTYGVDmosZWTLal1WbWRW3A.jpg");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
//    public static String constructSavePath(String merchantId,String merchanName){
//        String path
//    }
}
