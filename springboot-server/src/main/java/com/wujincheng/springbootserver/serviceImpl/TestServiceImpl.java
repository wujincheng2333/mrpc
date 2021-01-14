package com.wujincheng.springbootserver.serviceImpl;

import com.wujincheng.springbootserver.service.TestService;
import com.wujincheng.mrpccommon.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestServiceImpl implements TestService {

    @Autowired
    private DemoService demoService;

    @Override
    public void sayHello(String msg) {
        demoService.sayHello(msg);
    }
}