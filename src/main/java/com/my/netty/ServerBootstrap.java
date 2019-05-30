package com.my.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 15:51
 */
public class ServerBootstrap {

    private int port;
    private MyNioChannel myNioChannel;
    private MyThreadGroup myThreadGroup;

    public ServerBootstrap(int port, MyNioChannel myNioChannel, MyThreadGroup myThreadGroup) {
        this.port = port;
        this.myNioChannel = myNioChannel;
        this.myThreadGroup = myThreadGroup;
    }

    public void start() throws Exception {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            myNioChannel.handler(selector,myThreadGroup);
        }
    }

    public static void main(String[] args) throws Exception {
        MyNioServerChannel nioServerChannel = new MyNioServerChannel();
        MyThreadGroup threadGroup = new MyThreadGroup(1);
        ServerBootstrap serverBootstrap  = new ServerBootstrap(9991,nioServerChannel,threadGroup);
        serverBootstrap.start();
    }
}
