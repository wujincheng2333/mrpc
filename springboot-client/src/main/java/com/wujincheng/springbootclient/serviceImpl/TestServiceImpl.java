package com.wujincheng.springbootclient.serviceImpl;

import com.wujincheng.mrpccommon.annotation.MRPCReference;
import com.wujincheng.mrpccommon.service.DemoService;
import com.wujincheng.springbootclient.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("testService")
public class TestServiceImpl implements TestService {

    @MRPCReference
    private DemoService demoService;

    @PostConstruct
    private void init(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(;;){
                        try {
                            sayHello("springboot-client01");
                        }catch (Exception e){
                            //
                            e.printStackTrace();
                        }

                        Thread.sleep(2000);
                    }
                }catch (Exception e){
                    //
                }

            }
        }).start();
    }

    @Override
    public void sayHello(String msg) {
        demoService.sayHello(msg);
    }
}