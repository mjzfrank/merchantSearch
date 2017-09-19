package com.pb.data.executor;

import com.pb.data.MerchantData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pb on 2017/5/4.
 */
public class JDBCExecutor{
    public static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(4);
    public static void execute(final MerchantData data){
        FIXED_THREAD_POOL.execute(new Runnable() {
            public void run() {

                data.write();
            }
        });
    }

}
