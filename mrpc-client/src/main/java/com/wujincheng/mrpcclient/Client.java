package com.wujincheng.mrpcclient;

import com.wujincheng.mrpccommon.context.RpcContext;
import com.wujincheng.mrpccommon.init.CacheData;
import com.wujincheng.mrpccommon.init.MRPCInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

public class Client {

    private static ExecutorService executor =new ThreadPoolExecutor(200, 200, 5, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("mrpc-test-netty-work");
            return t;
        }
    });

    private static boolean init=false;

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception{
        MRPCInit.init();
        DemoServiceNative demoServiceNative=(DemoServiceNative) CacheData.interfaceServiceInstance.get(DemoServiceNative.class.getName());

        final String msg=getRandomString(1024);
//        RpcContext.setValue("test","test123");
//        System.out.println(msg);
//        RpcContext.setValue(Common.SIMPLE_CLIENT,"true");
//        System.out.println(demoServiceNative.echo(msg));
//        new CountDownLatch(1).await();

        for(;;){
            try {
                demoServiceNative.sayHello("mrpc-client01");
                init=true;
                int size=100000;

                for(int j=1;j<=20;j++){
                    final CountDownLatch countDownLatch=new CountDownLatch(size);
                    final Semaphore semaphore=new Semaphore(300,false);
                    long startTime=System.currentTimeMillis();
                    for(int i=0;i<size;i++){
                        semaphore.acquire();
                        try {
                            executor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        RpcContext.setValue("test","test123");
                                        //RpcContext.setValue(Common.SIMPLE_CLIENT,true);
                                        demoServiceNative.echo(msg);
                                    }catch (Exception e){
                                        logger.error(e.getMessage(),e);
                                    }finally {
                                        semaphore.release();
                                        countDownLatch.countDown();
                                    }
                                }
                            });
                        }catch (Exception e){
                            logger.error(e.getMessage(),e);
                        }
                    }
                    logger.info(j+"-->提交任务完成");
                    countDownLatch.await();
                    logger.info(j+"-->耗时：[{}] ms",System.currentTimeMillis()-startTime);
                    if(j==10){
                        Thread.sleep(10000);
                    }else{
                        Thread.sleep(100);
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(init){
                break;
            }
            Thread.sleep(2000);
        }
        new CountDownLatch(1).await();
    }

    private static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}