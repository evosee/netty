package com.my.netty;

import java.io.IOException;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 20:40
 */
public class MyThreadGroup {
    private int threads;
    private MyThreadExecutor[] executors;
    private int next;

    public MyThreadGroup(int threads) {
        this.threads = threads;
        executors = new MyThreadExecutor[threads];
    }


    public MyThreadExecutor next() throws Exception {

        MyThreadExecutor t = executors[(next++)&executors.length-1];
        if(t==null){
            t =  new MyThreadExecutor();
            int i = next-1;
            executors[i] = t;
        }
        return t;
    }
}
