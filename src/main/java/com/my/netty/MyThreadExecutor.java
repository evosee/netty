package com.my.netty;


import io.netty.util.CharsetUtil;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/30 9:20
 */
public class MyThreadExecutor<T> {
    private Selector selector;
    private BlockingQueue<T> blockingQueue;


    public MyThreadExecutor() throws Exception {
        selector = Selector.open();
        blockingQueue = new LinkedBlockingQueue();

       /* Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(null);
        Field selectedKeysField = Class.forName("sun.nio.ch.SelectorImpl").getDeclaredField("selectedKeys");
       // selectedKeysField.setAccessible(true);
        long offset = unsafe.objectFieldOffset(selectedKeysField);
        unsafe.putObject(selector, offset, new HashSet<SelectionKey>());*/

        Runnable r = () -> {
            for (; ; ) {
                Runnable t = (Runnable) getTask();
                if (t != null) {
                    t.run();
                }

                try {
                    int i = selector.select(1000);
                    if (i == 0) continue;
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        if(selectionKey.isReadable()){
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            socketChannel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array(), CharsetUtil.UTF_8));
                            socketChannel.write(ByteBuffer.wrap(("I am nio " + Thread.currentThread().getId()).getBytes(CharsetUtil.UTF_8)));
                            socketChannel.close();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(r);
        thread.start();

    }

    public void addTask(T t) {
        blockingQueue.add(t);
    }

    public T getTask() {
        return blockingQueue.poll();
    }

    public void register(SelectionKey selectionKey) throws IOException {
        Runnable r = () -> {
            ServerSocketChannel s = (ServerSocketChannel) selectionKey.channel();

            try {
                SocketChannel channel = s.accept();
                channel.configureBlocking(false);
                channel.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                e.printStackTrace();
            }

        };

        addTask((T) r);

    }


}
