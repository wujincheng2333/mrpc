package com.wujincheng.mrpcclient;

import com.wujincheng.mrpccommon.annotation.MRPCReference;
import com.wujincheng.mrpccommon.annotation.MRPCServiceNative;
import com.wujincheng.mrpccommon.service.DemoService;

@MRPCServiceNative
public class DemoServiceNative {
    @MRPCReference
    private DemoService demoService;

    public void sayHello(String msg){
        demoService.sayHello(msg);
    }

    public String echo(String str){
        return demoService.echo(str);
    }
}