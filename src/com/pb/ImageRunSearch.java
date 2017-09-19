package com.pb;

import com.pb.data.MerchantData;
import com.pb.data.executor.ImageExecutor;
import com.pb.data.jdbc.JDBCReader;

import java.util.List;

/**
 * Created by pb on 2017/5/5.
 */
public class ImageRunSearch {

    public static void main(String[] args){
      List<MerchantData> datas= JDBCReader.readUnDownloadImage();
        RunSearch.area ="";
        for(final MerchantData data :datas) {
            RunSearch.PAGE_THREAD_POOL.execute(new Runnable() {
                public void run() {
                    RunSearch.parseMerchantImage(String.valueOf(data.getMerchantId()),data.getName(),data.getArea());
                }
            });
        }
        while (!ImageExecutor.FIXED_THREAD_POOL.isTerminated()){
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
