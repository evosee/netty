package com.my.nio;

import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/29 9:27
 */
public class NioServer {

    private int port;

    public NioServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            while (true) {
                int i = selector.select();
                if (i == 0) continue;
                Set<SelectionKey> sets = selector.selectedKeys();
                Iterator<SelectionKey> iterable = sets.iterator();
                while (iterable.hasNext()) {
                    SelectionKey selectionKey = iterable.next();
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = channel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                        iterable.remove();
                    }else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        byte[] bytes = byteBuffer.array();
                        String a = new String(bytes);
                        System.out.println(a);
                        socketChannel.write(ByteBuffer.wrap("I am nio".getBytes(CharsetUtil.UTF_8)));
                        socketChannel.close();
                    }

                }

            }
        }


    }

    public static void main(String[] args) throws IOException {
        new NioServer(9999).start();
    }
}
