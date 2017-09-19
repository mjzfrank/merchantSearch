package com.pb.data.executor;

import com.pb.data.MerchantImageData;
import com.pb.data.image.ImageWriter;
import com.pb.data.jdbc.JDBCWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pb on 2017/5/4.
 */
public class ImageExecutor {
    public static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(4);
    public static void execute(final MerchantImageData data){
        FIXED_THREAD_POOL.execute(new Runnable() {
            public void run() {
                try {
                    System.out.println(String.format("开始下载图片：%d,%s,url:%s",data.getMerchantId(),data.getName(),data.getImageUrl(),data.getArea())+"_"+new Date());
                    ImageWriter.write(data);
                    //JDBCWriter.writeImage(data);
                    JDBCWriter.updateImageNum(data.getMerchantId());
                    System.out.println(String.format("已下载图片：%d,%s,url:%s",data.getMerchantId(),data.getName(),data.getImageUrl(),data.getArea())+"_"+new Date());
                } catch (Exception e) {
                    System.err.println(String.format("下载图片失败：%d,%s,url:%s",data.getMerchantId(),data.getName(),data.getImageUrl())+"_"+new Date());
                    data.writeFail();
                    e.printStackTrace();
                }
            }
        });
    }
}
