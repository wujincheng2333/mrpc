package com.wujincheng.mrpcclient;

import com.wujincheng.mrpccommon.init.CacheData;
import com.wujincheng.mrpccommon.init.MRPCInit;

import java.util.concurrent.CountDownLatch;

public class Client {

    public static void main(String[] args) throws Exception{
        MRPCInit.init();
        DemoServiceNative demoServiceNative=(DemoServiceNative) CacheData.interfaceServiceInstance.get(DemoServiceNative.class.getName());
        for(;;){
            try {
                demoServiceNative.sayHello("mrpc-client01");
            }catch (Exception e){
                //
                e.printStackTrace();
            }

            Thread.sleep(2000);
        }

        //new CountDownLatch(1).await();
    }
}