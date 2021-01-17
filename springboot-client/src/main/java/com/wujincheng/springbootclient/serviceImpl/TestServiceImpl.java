package com.wujincheng.springbootclient.serviceImpl;

import com.wujincheng.mrpccommon.annotation.MRPCReference;
import com.wujincheng.mrpccommon.common.Common;
import com.wujincheng.mrpccommon.context.RpcContext;
import com.wujincheng.mrpccommon.init.CacheData;
import com.wujincheng.mrpccommon.service.DemoService;
import com.wujincheng.mrpccommon.serviceImpl.MyNettyCommonService1207Native;
import com.wujincheng.springbootclient.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.*;

@Service("testService")
public class TestServiceImpl implements TestService {

    private static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);

    @MRPCReference
    private DemoService demoService;



    private ExecutorService executor =new ThreadPoolExecutor(200, 200, 5, TimeUnit.SECONDS
            , new ArrayBlockingQueue<>(1000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("mrpc-test-netty-work");
            return t;
        }
    });

    private boolean init=false;

    @PostConstruct
    private void init(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(;;){
                        try {
                            sayHello("springboot-client01");
                            MyNettyCommonService1207Native myNettyCommonService1207=(MyNettyCommonService1207Native)CacheData.interfaceServiceInstance.get(MyNettyCommonService1207Native.class.getName());
                            init=true;
                            int size=100000;
                            final String msg=getRandomString(1024);
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
                                                    demoService.echo(msg);
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

//                    final String msg=getRandomString(1024);
//                    while (true){
//                        demoService.echo(msg);
//                        Thread.sleep(20000);
//                    }
                }catch (Exception e){
                    //
                }

            }
        }).start();
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @Override
    public void sayHello(String msg) {
        demoService.sayHello(msg);
    }
}