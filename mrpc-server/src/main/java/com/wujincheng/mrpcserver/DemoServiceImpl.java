package com.wujincheng.mrpcserver;

import com.wujincheng.mrpccommon.annotation.MRPCService;
import com.wujincheng.mrpccommon.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MRPCService
public class DemoServiceImpl implements DemoService {
    private static final Logger logger= LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    public void sayHello(String msg) {
        logger.info(msg);
    }

    @Override
    public String echo(String str) {
        return str;
    }
}