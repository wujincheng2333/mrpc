package com.wujincheng.springbootserver.serviceImpl;

import com.wujincheng.mrpccommon.annotation.MRPCService;
import com.wujincheng.mrpccommon.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("demoService")
@MRPCService
public class DemoServiceImpl implements DemoService {
    private static final Logger logger= LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public void sayHello(String msg) {
        logger.info(msg);
    }
}