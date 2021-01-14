package com.wujincheng.mrpcserver;

import com.wujincheng.mrpccommon.init.MRPCInit;

import java.util.concurrent.CountDownLatch;

public class Server {

    public static void main(String[] args) throws Exception{
        MRPCInit.init();
        new CountDownLatch(1).await();
    }
}